package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Configuracoes;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// persiste as configurações personalizaveis
public class ConfiguracoesAdmin {
    private static final String PASTA = "saves";
    private static final String ARQUIVO = PASTA + "/configuracao.json";
    private static final Gson gson = new Gson();
    private static Configuracoes instancia;
    private ConfiguracoesAdmin(){

    }

    public static void salvarConfigPersonalizadas()
            throws SaveException, DadosInvalidosException {
        if (instancia == null) {
            throw new DadosInvalidosException("Configurações não inicializadas");
        }
        salvarConfigPersonalizadas(instancia);
    }

    public static void salvarConfigPersonalizadas(Configuracoes c)
            throws SaveException, DadosInvalidosException {
        if (c == null) {
            throw new DadosInvalidosException("Configurações nulas");
        }

        Path arquivo = Paths.get(ARQUIVO);
        Path temporario = arquivo.resolveSibling(
                arquivo.getFileName() + ".tmp"
        );

        String json = gson.toJson(c);

        try {
            Files.createDirectories(arquivo.getParent());
            Files.writeString(temporario, json);

            try {
                Files.move(
                        temporario,
                        arquivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            apagarTemporario(temporario);

            throw new SaveException("Falha ao salvar " + ARQUIVO, e);
        }

        instancia = c;
    }

    private static void apagarTemporario(Path temporario) {
        try {
            Files.deleteIfExists(temporario);
        } catch (IOException ignored) {
        }
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
            throw new CarregamentoException("Falha ao ler " + ARQUIVO, e);
        }

        Configuracoes lida;
        try {
            lida = gson.fromJson(json, Configuracoes.class);
        } catch (JsonSyntaxException e) {
            throw new CarregamentoException(ARQUIVO + " inválido", e);
        }

        if (lida == null) {
            throw new DadosInvalidosException(ARQUIVO + " vazio ou nulo");
        }

        Configuracoes normalizada = new Configuracoes();
        normalizada.setVelocidadeTexto(lida.getVelocidadeTexto());
        normalizada.setConfirmarSaida(lida.isConfirmarSaida());
        normalizada.setVolume(lida.getVolume());
        normalizada.setDicasAtalho(lida.isDicasAtalho());
        return normalizada;
    }




}
