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
// agora os metodos de leitura e gravação não muda a API pública usada pelo resto do jogo
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

        salvarConfigPersonalizadas(c, Paths.get(ARQUIVO));
        instancia = c;
    }
    static void salvarConfigPersonalizadas(Configuracoes c, Path arquivo)
            throws SaveException, DadosInvalidosException {
        if (c == null) {
            throw new DadosInvalidosException("Configurações nulas");
        }

        Path temporario = arquivo.resolveSibling(
                arquivo.getFileName() + ".tmp"
        );

        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }

            Files.writeString(temporario, gson.toJson(c));

            try {
                Files.move(
                        temporario,
                        arquivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporario,
                        arquivo,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            apagarTemporario(temporario);
            throw new SaveException("Falha ao salvar " + arquivo, e);
        }
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
        return carregar(Paths.get(ARQUIVO));
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
