package br.uefs.forkeazando.persistencia;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Configuracoes;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfiguracoesAdmin {
    private static final String PASTA = "saves";
    private static final String ARQUIVO = PASTA + "/configuracao.json";
    private static final Gson gson = new Gson();
    private static Configuracoes instancia;
    private ConfiguracoesAdmin(){

    }

    public static void salvar()
            throws SaveException, DadosInvalidosException {
        if (instancia == null) {
            throw new DadosInvalidosException("Configurações não inicializadas");
        }
        salvar(instancia);
    }

    public static void salvar(Configuracoes c)
            throws SaveException, DadosInvalidosException {
        if (c == null) {
            throw new DadosInvalidosException("Configurações nulas");
        }

        String json = gson.toJson(c);

        try {
            Files.createDirectories(Paths.get(PASTA));
            Files.writeString(Paths.get(ARQUIVO), json);
        } catch (IOException e) {
            throw new SaveException("Falha ao salvar " + ARQUIVO, e);
        }

        instancia = c;
    }

    public static Configuracoes get() throws CarregamentoException, DadosInvalidosException {
        if (instancia == null){
            instancia = carregar();
        }
        return instancia;
    }

    private static Configuracoes carregar()
            throws CarregamentoException, DadosInvalidosException {

        Path arquivo = Paths.get(ARQUIVO);

        if (!Files.exists(arquivo)) {
            return new Configuracoes();
        }

        String json;
        try {
            json = Files.readString(arquivo);
        } catch (IOException e) {
            throw new CarregamentoException("Falha ao ler config.json", e);
        }

        Configuracoes lida;
        try {
            lida = gson.fromJson(json, Configuracoes.class);
        } catch (JsonSyntaxException e) {
            throw new CarregamentoException(ARQUIVO + "inválido", e);
        }

        if (lida == null) {
            throw new DadosInvalidosException(ARQUIVO + "vazio ou nulo");
        }

        return lida;
    }




}
