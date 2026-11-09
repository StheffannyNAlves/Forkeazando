package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Configuracoes;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Persiste as configurações personalizáveis
// Agora os metodos de leitura e gravação não muda a API pública usada pelo resto do jogo
/** Lê e grava as preferências do jogador em saves/configuracao.json */

public class ConfiguracoesAdmin {
    private static final Path ARQUIVO = GerenciadorSlots.PASTA_PADRAO.resolve("configuracao.json");
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

    public static void salvarConfigPersonalizadas(Configuracoes c) throws SaveException, DadosInvalidosException {
        if (c == null) {
            throw new DadosInvalidosException("Configurações nulas");
        }

        salvarConfigPersonalizadas(c, ARQUIVO);
        instancia = c;
    }
    static void salvarConfigPersonalizadas(Configuracoes c, Path arquivo) throws SaveException, DadosInvalidosException {
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
        } catch (IOException e) {
            throw new SaveException("Falha ao salvar " + arquivo, e);
        }

        ArquivoAtomico.gravar(arquivo, gson.toJson(c));
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
        return carregar(ARQUIVO);
    }

    static Configuracoes carregar(Path arquivo)
            throws CarregamentoException, DadosInvalidosException {

        if (!Files.exists(arquivo)) {
            return new Configuracoes();
        }

        String json;

        try {
            json = Files.readString(arquivo);
        } catch (IOException e) {
            throw new CarregamentoException(
                    "Falha ao ler " + arquivo, e
            );
        }

        Configuracoes lida;

        try {
            lida = gson.fromJson(json, Configuracoes.class);
        } catch (JsonSyntaxException e) {
            throw new CarregamentoException(arquivo + " inválido", e);
        }

        if (lida == null) {
            throw new DadosInvalidosException(arquivo + " vazio ou nulo");
        }

        Configuracoes normalizada = new Configuracoes();
        normalizada.setVelocidadeTexto(lida.getVelocidadeTexto());
        normalizada.setConfirmarSaida(lida.isConfirmarSaida());
        normalizada.setVolume(lida.getVolume());
        normalizada.setDicasAtalho(lida.isDicasAtalho());

        return normalizada;
    }

    // Usa os valores padrão na memória quando o arquivo está inválido
    public static Configuracoes restaurarPadrao() {
        instancia = new Configuracoes();
        return instancia;
    }

}
