package br.uefs.forkeazando.persistencia;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GerenciadorSave {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void salvar(Estado estado, Path destino) throws SaveException {

        Path temporario = destino.resolveSibling(
                destino.getFileName() + ".tmp"
        );

        try (Writer w = Files.newBufferedWriter(temporario)) {
            gson.toJson(estado, w);

        } catch (IOException | JsonIOException e) {
            apagarTemporario(temporario);

            throw new SaveException(
                    "Não foi possível salvar o arquivo: " + destino,
                    e
            );
        }

        try {
            Files.move(
                    temporario,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );

        } catch (IOException e) {
            apagarTemporario(temporario);

            throw new SaveException(
                    "Não foi possível substituir o arquivo: " + destino,
                    e
            );
        }
    }

    private static void apagarTemporario(Path temporario) {
        try {
            Files.deleteIfExists(temporario);
        } catch (IOException ignored) {
        }
    }

    private static void validar(JsonObject json)
            throws DadosInvalidosException {

        if (!json.has("protagonista")
                || json.get("protagonista").isJsonNull()) {

            throw new DadosInvalidosException(
                    "Save inválido: protagonista ausente"
            );
        }

        if (!json.get("protagonista").isJsonObject()) {
            throw new DadosInvalidosException(
                    "Save inválido: protagonista inválido"
            );
        }

        JsonObject protagonista =
                json.getAsJsonObject("protagonista");

        if (!protagonista.has("historicoFlags")
                || protagonista.get("historicoFlags").isJsonNull()) {

            throw new DadosInvalidosException(
                    "Save inválido: historicoFlags ausente"
            );
        }

        if (!protagonista.has("relacionamentos")
                || protagonista.get("relacionamentos").isJsonNull()) {

            throw new DadosInvalidosException(
                    "Save inválido: relacionamentos ausente"
            );
        }
    }

    public static Estado carregar(Path origem)
            throws CarregamentoException, DadosInvalidosException {

        try (Reader r = Files.newBufferedReader(origem)) {

            JsonElement elemento = JsonParser.parseReader(r);

            if (elemento == null || elemento.isJsonNull()) {
                throw new CarregamentoException(
                        "Arquivo de save vazio: " + origem
                );
            }

            if (!elemento.isJsonObject()) {
                throw new DadosInvalidosException(
                        "Save inválido: estrutura JSON deve ser um objeto"
                );
            }

            JsonObject json = elemento.getAsJsonObject();

            validar(json);

            return gson.fromJson(json, Estado.class);

        } catch (IOException | JsonParseException e) {
            throw new CarregamentoException(
                    "Não foi possível carregar o arquivo: " + origem,
                    e
            );
        }
    }
}