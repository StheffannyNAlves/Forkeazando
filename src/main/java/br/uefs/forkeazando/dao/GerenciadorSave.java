package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class GerenciadorSave {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final int VERSAO_ATUAL = 1;

    public record SaveCarregado(
            Estado estado,
            Optional<Instant> dataSalvamento
    ) {}

    public static void salvar(Estado estado, Path destino)
            throws SaveException {

        Path temporario = destino.resolveSibling(
                destino.getFileName() + ".tmp"
        );

        JsonObject envelope = new JsonObject();
        envelope.addProperty("versao", VERSAO_ATUAL);
        envelope.addProperty(
                "dataSalvamento",
                Instant.now().toString()
        );
        envelope.add("estado", gson.toJsonTree(estado));

        try (Writer w = Files.newBufferedWriter(temporario)) {
            gson.toJson(envelope, w);

        } catch (IOException | JsonIOException e) {
            apagarTemporario(temporario);

            throw new SaveException(
                    "Não foi possível salvar o arquivo: " + destino,
                    e
            );
        }

        try {
            try {
                Files.move(
                        temporario,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporario,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            apagarTemporario(temporario);

            throw new SaveException(
                    "Não foi possível substituir o arquivo: "
                            + destino,
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

    private static SaveCarregado interpretar(
            JsonObject raiz
    ) throws DadosInvalidosException {

        // Sem "estado" na raiz: considera o formato antigo.
        if (!raiz.has("estado")) {
            validar(raiz);

            return new SaveCarregado(
                    gson.fromJson(raiz, Estado.class),
                    Optional.empty()
            );
        }

        // Com "estado" na raiz: espera o novo envelope.
        if (!raiz.has("versao")
                || !raiz.get("versao").isJsonPrimitive()
                || !raiz.getAsJsonPrimitive("versao").isNumber()) {

            throw new DadosInvalidosException(
                    "Save inválido: versão ausente ou inválida"
            );
        }

        int versao;

        try {
            versao = Integer.parseInt(
                    raiz.get("versao").getAsString()
            );
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException(
                    "Save inválido: versão deve ser um inteiro"
            );
        }

        if (versao != VERSAO_ATUAL) {
            throw new DadosInvalidosException(
                    "Versão de save não suportada: " + versao
            );
        }

        if (!raiz.has("dataSalvamento")
                || !raiz.get("dataSalvamento").isJsonPrimitive()
                || !raiz.getAsJsonPrimitive("dataSalvamento").isString()) {

            throw new DadosInvalidosException(
                    "Save inválido: data de salvamento ausente ou inválida"
            );
        }

        Instant dataSalvamento;

        try {
            dataSalvamento = Instant.parse(
                    raiz.get("dataSalvamento").getAsString()
            );
        } catch (DateTimeParseException e) {
            throw new DadosInvalidosException(
                    "Save inválido: data de salvamento malformada"
            );
        }

        if (raiz.get("estado").isJsonNull()
                || !raiz.get("estado").isJsonObject()) {

            throw new DadosInvalidosException(
                    "Save inválido: estado ausente ou inválido"
            );
        }

        JsonObject jsonEstado = raiz.getAsJsonObject("estado");

        validar(jsonEstado);

        Estado estado = gson.fromJson(jsonEstado, Estado.class);

        return new SaveCarregado(
                estado,
                Optional.of(dataSalvamento)
        );
    }

    public static Estado carregar(Path origem)
            throws CarregamentoException, DadosInvalidosException {

        return carregarComMetadados(origem).estado();
    }

    public static SaveCarregado carregarComMetadados(Path origem)
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

            return interpretar(elemento.getAsJsonObject());

        } catch (IOException | JsonParseException e) {
            throw new CarregamentoException(
                    "Não foi possível carregar o arquivo: " + origem,
                    e
            );
        }
    }
}