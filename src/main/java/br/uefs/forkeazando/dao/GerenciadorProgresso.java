package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
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

    private static void validar(JsonObject json) throws DadosInvalidosException{
         if (json.has("finais") && !json.get("finais").isJsonObject()){
             throw new DadosInvalidosException("Progresso Inválido: 'finais' deve ser um objeto");
         }

         if (!json.has("finais")){
             return;
         }
         JsonObject finais = json.getAsJsonObject("finais");

         for (String flag : finais.keySet()){
             JsonElement valor = finais.get(flag);
             if (!valor.isJsonPrimitive() || !valor.getAsJsonPrimitive().isString())
                 throw new DadosInvalidosException("Progresso inválido: data inválida para " + flag);
             try {
                 LocalDateTime.parse(valor.getAsString());
             } catch (DateTimeParseException e){
                 throw new DadosInvalidosException("Progresso inválido: data inválida para " + flag);
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


