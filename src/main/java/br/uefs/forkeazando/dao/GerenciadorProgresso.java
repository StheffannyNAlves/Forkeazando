package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Colecionavel;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Progresso;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


public class GerenciadorProgresso {

     private static final Gson gson = new GsonBuilder()
             .setPrettyPrinting()
             .create();
     private final Path pasta;
     private  boolean arquivoCorrompido;
     static final String NOME_ARQUIVO = "progresso.json";
     Path caminhoArquivo() {
         return pasta.resolve(NOME_ARQUIVO);
     }

    public GerenciadorProgresso(Path pasta) {
        this.pasta = pasta;
    }

    public Progresso carregar() throws CarregamentoException, DadosInvalidosException {
        Path origem = caminhoArquivo();

        if (!Files.exists(origem)) {
            arquivoCorrompido = false;
            return new Progresso();
        }

        try (var r = Files.newBufferedReader(origem)) {
            JsonElement elemento = JsonParser.parseReader(r);
            if (elemento == null || elemento.isJsonNull()) {
                throw new CarregamentoException("Arquivo de progresso vazio: " + origem);
            }
            if (!elemento.isJsonObject()) {
                throw new DadosInvalidosException("Progresso inválido: a raiz deve ser um objeto");
            }

            JsonObject json = elemento.getAsJsonObject();
            validar(json);

            Progresso progresso = gson.fromJson(json, Progresso.class);
            arquivoCorrompido = false;
            return progresso;

        } catch (IOException | JsonParseException e) {
            arquivoCorrompido = true;
            throw new CarregamentoException("Não foi possível carregar o progresso: " + origem, e);
        } catch (CarregamentoException | DadosInvalidosException e) {
            arquivoCorrompido = true;
            throw e;
        }
    }


    private static void validar(JsonObject json) throws DadosInvalidosException {
        validarMapaDeDatas(json, "finais");
        validarMapaDeDatas(json, "colecao");
        validarMapaDeDatas(json, "conquistas");

        validarChavesColecao(json);
        validarChavesConquistas(json);
    }

    private static void validarMapaDeDatas(JsonObject json, String nomeCampo) throws DadosInvalidosException {
        if (!json.has(nomeCampo)) {
            return;
        }
        JsonElement elemento = json.get(nomeCampo);

        if (elemento == null || !elemento.isJsonObject()) {
            throw new DadosInvalidosException(
                    "Progresso inválido: '" + nomeCampo + "' deve ser um objeto"
            );
        }

        JsonObject mapa = elemento.getAsJsonObject();

        for (String chave : mapa.keySet()) {
            JsonElement valor = mapa.get(chave);

            if (valor == null || !valor.isJsonPrimitive()
                    || !valor.getAsJsonPrimitive().isString()) {
                throw new DadosInvalidosException(
                        "Progresso inválido: data inválida para " + chave
                );
            }

            try {
                LocalDateTime.parse(valor.getAsString());
            } catch (DateTimeParseException e) {
                throw new DadosInvalidosException(
                        "Progresso inválido: data inválida para " + chave
                );
            }
        }
    }

    private static void validarChavesColecao(JsonObject json)
            throws DadosInvalidosException {

        if (!json.has("colecao")) {
            return;
        }

        JsonElement elemento = json.get("colecao");
        if (elemento == null || !elemento.isJsonObject()) {
            return;
        }

        JsonObject colecao = elemento.getAsJsonObject();

        for (String flag : colecao.keySet()) {
            if (Colecionavel.porFlag(flag).isEmpty()) {
                throw new DadosInvalidosException(
                        "Progresso inválido: colecionável desconhecido: " + flag
                );
            }
        }
    }

    private static void validarChavesConquistas(JsonObject json)
            throws DadosInvalidosException {

        if (!json.has("conquistas")) {
            return;
        }

        JsonElement elemento = json.get("conquistas");
        if (elemento == null || !elemento.isJsonObject()) {
            return;
        }

        JsonObject conquistas = elemento.getAsJsonObject();

        for (String codigo : conquistas.keySet()) {
            if (Conquista.porCodigo(codigo).isEmpty()) {
                throw new DadosInvalidosException(
                        "Progresso inválido: conquista desconhecida: " + codigo
                );
            }
        }
    }


    public void salvar(Progresso progresso) throws SaveException{
        if (arquivoCorrompido){
            throw new SaveException("Progresso não salvo: o arquivo original está corrompido");
        }
        if (progresso == null){
            throw new SaveException("Progresso não pode ser nulo");}
        try {
            Files.createDirectories(pasta);
            String conteudo = gson.toJson(progresso);
            ArquivoAtomico.gravar(caminhoArquivo(), conteudo);
        } catch (IOException | JsonIOException e) {
            throw new SaveException(
                    "Não foi possível salvar o progresso: " + caminhoArquivo(), e
            );
        }
    }
}


