package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Protagonista;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Expande a presença do outro protagonista sem modificar Cena ou o estado da partida.
 * Identificadores antigos de identidade permanecem estáveis para proteger flags e saves.
 */
public final class NarrativaProtagonistas {
    private static final JsonObject VARIACOES = carregarVariacoes();

    private NarrativaProtagonistas() {}

    public static String resolver(Cena cena, Protagonista jogador) {
        Objects.requireNonNull(cena, "A cena é obrigatória");
        Objects.requireNonNull(jogador, "O protagonista é obrigatório");

        boolean jogandoComTiana = jogador.temFlag("protagonista_stheffanny");
        String outroNome = jogandoComTiana ? "Miles" : "Tiana";
        String chaveOutro = jogandoComTiana ? "protagonista_masculino" : "protagonista_stheffanny";

        String variacao = "";
        JsonObject cenaVariavel = VARIACOES.getAsJsonObject(String.valueOf(cena.getId()));
        if (cenaVariavel != null && cenaVariavel.has(chaveOutro)
                && !cenaVariavel.get(chaveOutro).isJsonNull()) {
            variacao = cenaVariavel.get(chaveOutro).getAsString();
        }
        return cena.getTextoNarrativo()
                .replace("{{OUTRO_CENA}}", variacao)
                .replace("{{NOME_OUTRO_PROTAGONISTA}}", outroNome);
    }

    private static JsonObject carregarVariacoes() {
        try (InputStream arquivo = NarrativaProtagonistas.class
                .getResourceAsStream("/roteiro/variacoes_protagonistas.json")) {
            if (arquivo == null) {
                throw new IllegalStateException("Arquivo de variações narrativas não encontrado.");
            }
            JsonObject raiz = JsonParser.parseReader(
                    new InputStreamReader(arquivo, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject variacoes = raiz.getAsJsonObject("variacoes");
            if (variacoes == null) {
                throw new IllegalStateException("Campo 'variacoes' ausente do arquivo de roteiro.");
            }
            return variacoes;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler variações dos protagonistas.", e);
        }
    }
}
