package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class CarregadorRoteiroJson {
    private static final int VERSAO_SCHEMA = 2;
    private static final Gson GSON = new Gson();

    private static final List<String> ARQUIVOS_CAPITULOS = List.of(
            "abertura.json",
            "capitulo1.json",
            "capitulo2.json",
            "capitulo3.json",
            "capitulo4.json",
            "capitulo5.json",
            "capitulo5a.json",
            "capitulo5b.json",
            "capitulo5c.json",
            "capitulo6.json",
            "capitulo7.json",
            "capitulo8.json"
    );
    private static final String ARQUIVO_APAGAO = "apagao.json";

    private CarregadorRoteiroJson() {}

    static List<Cena> carregar(boolean incluirApagao) {
        List<String> arquivos = new ArrayList<>(ARQUIVOS_CAPITULOS);
        if (incluirApagao) {
            arquivos.add(ARQUIVO_APAGAO);
        }

        try {
            List<CenaOrdenada> ordenadas = new ArrayList<>();
            Set<Integer> ordensGlobais = new HashSet<>();
            for (String arquivo : arquivos) {
                Documento documento = lerDocumento(arquivo);
                if (documento.totalCenasArquivo != documento.cenas.size()) {
                    throw new IOException("O total de cenas declarado não confere em " + arquivo + ".");
                }
                for (CenaJson dadosCena : documento.cenas) {
                    if (!ordensGlobais.add(dadosCena.ordemGlobal)) {
                        throw new IOException("ordemGlobal duplicada: " + dadosCena.ordemGlobal);
                    }
                    ordenadas.add(new CenaOrdenada(dadosCena.ordemGlobal, converter(dadosCena, arquivo)));
                }
            }

            // ordemGlobal pode ter lacunas para permitir inserir cenas sem renumerar todo o roteiro.
            ordenadas.sort(Comparator.comparingInt(CenaOrdenada::ordemGlobal));
            List<Cena> cenas = ordenadas.stream().map(CenaOrdenada::cena).toList();
            validarIntegridade(cenas, incluirApagao);
            return new ArrayList<>(cenas);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível carregar o roteiro JSON", e);
        }
    }

    private static Documento lerDocumento(String nomeArquivo) throws IOException {
        try (InputStream entrada = abrirArquivo(nomeArquivo)) {
            if (entrada == null) {
                throw new IOException("Arquivo de roteiro não encontrado: " + nomeArquivo);
            }

            JsonElement raiz = JsonParser.parseReader(new InputStreamReader(entrada, StandardCharsets.UTF_8));
            if (!raiz.isJsonObject()) {
                throw new IOException("A raiz de " + nomeArquivo + " precisa ser um objeto JSON.");
            }
            JsonObject objeto = raiz.getAsJsonObject();
            validarEstruturaMinima(objeto, nomeArquivo);

            Documento documento = GSON.fromJson(objeto, Documento.class);
            if (documento == null || documento.cenas == null || documento.cenas.isEmpty()) {
                throw new IOException("O arquivo não contém cenas: " + nomeArquivo);
            }
            return documento;
        }
    }

    private static void validarEstruturaMinima(JsonObject documento, String nomeArquivo) throws IOException {
        exigirCampos(documento, Set.of("schemaVersion", "totalCenasArquivo", "cenas"),
                "documento " + nomeArquivo);
        if (!documento.has("schemaVersion")
                || documento.get("schemaVersion").getAsInt() != VERSAO_SCHEMA) {
            throw new IOException("Versão de esquema JSON incompatível em " + nomeArquivo + ".");
        }
        if (documento.get("totalCenasArquivo").getAsInt() < 1) {
            throw new IOException("O total de cenas declarado é inválido em " + nomeArquivo + ".");
        }
        if (!documento.has("cenas") || !documento.get("cenas").isJsonArray()) {
            throw new IOException("O campo 'cenas' está ausente ou inválido em " + nomeArquivo + ".");
        }

        JsonArray cenas = documento.getAsJsonArray("cenas");
        for (int i = 0; i < cenas.size(); i++) {
            JsonElement elementoCena = cenas.get(i);
            if (!elementoCena.isJsonObject()) {
                throw new IOException("Cena inválida no índice " + i + " de " + nomeArquivo + ".");
            }
            JsonObject cena = elementoCena.getAsJsonObject();
            exigirCampos(cena, Set.of("ordemGlobal", "id", "capitulo", "personagemFalando",
                    "textoNarrativo", "escolhas"), "cena " + i + " de " + nomeArquivo);
            if (!cena.get("escolhas").isJsonArray()) {
                throw new IOException("O campo 'escolhas' precisa ser um array em " + nomeArquivo + ".");
            }
            JsonArray escolhas = cena.getAsJsonArray("escolhas");
            for (int j = 0; j < escolhas.size(); j++) {
                JsonElement elementoEscolha = escolhas.get(j);
                if (!elementoEscolha.isJsonObject()) {
                    throw new IOException("Escolha inválida na cena " + cena.get("id") + ".");
                }
                exigirCampos(elementoEscolha.getAsJsonObject(), camposDaEscolha(),
                        "escolha " + j + " da cena " + cena.get("id"));
            }
        }
    }

    private static Set<String> camposDaEscolha() {
        return Set.of(
                "textoAlternativa", "cenaDestinoId", "flagsConcedidas", "flagRequerida",
                "scoreMinimo", "participacaoMinima", "participacaoMaxima", "scoreGanho",
                "participacaoGanha", "custoEnergia", "permiteEnergiaInsuficiente", "energiaAbaixoDe",
                "confiancaMinima", "confiancaMaxima", "vidaSocialMinima", "vidaSocialMaxima",
                "carismaMinimo", "carismaMaximo", "logicaMinima", "logicaMaxima",
                "sanidadeMinima", "sanidadeMaxima", "relacionamentoMinimo", "relacionamentoMaximo",
                "interesseAlvo", "impactoRelacionamento", "atributosAlterados");
    }

    private static void exigirCampos(JsonObject objeto, Set<String> campos, String contexto) throws IOException {
        for (String campo : campos) {
            if (!objeto.has(campo)) {
                throw new IOException("Campo '" + campo + "' ausente em " + contexto + ".");
            }
        }
    }

    private static Cena converter(CenaJson dados, String arquivo) throws IOException {
        if (dados.capitulo == null || dados.capitulo.isBlank() || dados.personagemFalando == null || dados.textoNarrativo == null || dados.escolhas == null || dados.ordemGlobal < 0) {
            throw new IOException("Dados obrigatórios ausentes na cena " + dados.id + " de " + arquivo + ".");
        }

        Cena cena = new Cena(dados.id, dados.capitulo, dados.personagemFalando, dados.textoNarrativo);
        for (EscolhaJson dadosEscolha : dados.escolhas) {
            if (dadosEscolha == null || dadosEscolha.textoAlternativa == null
                    || dadosEscolha.flagsConcedidas == null || dadosEscolha.relacionamentoMinimo == null
                    || dadosEscolha.relacionamentoMaximo == null || dadosEscolha.impactoRelacionamento == null
                    || dadosEscolha.atributosAlterados == null) {
                throw new IOException("Escolha com dados ausentes na cena " + dados.id + ".");
            }
            cena.adicionarEscolha(converterEscolha(dadosEscolha));
        }
        return cena;
    }

    private static Escolha converterEscolha(EscolhaJson dados) throws IOException {
        Escolha.Builder builder = new Escolha.Builder(dados.textoAlternativa, dados.cenaDestinoId)
                .comScoreMinimo(dados.scoreMinimo)
                .comParticipacaoMinima(dados.participacaoMinima)
                .comParticipacaoMaxima(dados.participacaoMaxima)
                .comScoreGanho(dados.scoreGanho)
                .comParticipacaoGanha(dados.participacaoGanha)
                .comCustoEnergia(dados.custoEnergia)
                .comEnergiaAbaixoDe(dados.energiaAbaixoDe)
                .comConfiancaMinima(dados.confiancaMinima)
                .comConfiancaMaxima(dados.confiancaMaxima)
                .comVidaSocialMinima(dados.vidaSocialMinima)
                .comVidaSocialMaxima(dados.vidaSocialMaxima)
                .comCarismaMinima(dados.carismaMinimo)
                .comCarismaMaxima(dados.carismaMaximo)
                .comLogicaMinima(dados.logicaMinima)
                .comLogicaMaxima(dados.logicaMaxima)
                .comSanidadeMinima(dados.sanidadeMinima)
                .comSanidadeMaxima(dados.sanidadeMaxima);

        for (String flag : dados.flagsConcedidas) {
            if (flag == null || flag.isBlank()) {
                throw new IOException("Uma flag concedida está vazia na escolha '" + dados.textoAlternativa + "'.");
            }
            builder.comFlagConcedida(flag);
        }
        if (dados.flagRequerida != null) {
            builder.comFlagRequerida(dados.flagRequerida);
        }
        if (dados.permiteEnergiaInsuficiente) {
            builder.comPermiteEnergiaInsuficiente();
        }
        if (dados.interesseAlvo != null) {
            try {
                builder.comInteresseAlvo(PersonagemSecundario.Interesse.valueOf(dados.interesseAlvo));
            } catch (IllegalArgumentException e) {
                throw new IOException("Interesse desconhecido: " + dados.interesseAlvo, e);
            }
        }

        adicionarRelacionamentos(builder, dados.relacionamentoMinimo, TipoRelacionamento.MINIMO);
        adicionarRelacionamentos(builder, dados.relacionamentoMaximo, TipoRelacionamento.MAXIMO);
        adicionarRelacionamentos(builder, dados.impactoRelacionamento, TipoRelacionamento.IMPACTO);
        dados.atributosAlterados.forEach(builder::comAtributo);
        return builder.build();
    }

    private static void adicionarRelacionamentos(
            Escolha.Builder builder, Map<String, Float> relacionamentos, TipoRelacionamento tipo) throws IOException {
        for (Map.Entry<String, Float> item : relacionamentos.entrySet()) {
            if (item.getKey() == null || item.getValue() == null) {
                throw new IOException("Relacionamento com código ou valor nulo.");
            }
            PersonagemSecundario personagem;
            try {
                personagem = PersonagemSecundario.porCodigo(item.getKey());
            } catch (IllegalArgumentException e) {
                throw new IOException("Personagem desconhecido no JSON: " + item.getKey(), e);
            }
            switch (tipo) {
                case MINIMO -> builder.comRelacionamentoMinimo(personagem, item.getValue());
                case MAXIMO -> builder.comRelacionamentoMaximo(personagem, item.getValue());
                case IMPACTO -> builder.comRelacionamento(personagem, item.getValue());
            }
        }
    }

    private static void validarIntegridade(List<Cena> cenas, boolean incluirApagao) throws IOException {
        Map<Integer, Cena> porId = new LinkedHashMap<>();
        for (Cena cena : cenas) {
            if (porId.putIfAbsent(cena.getId(), cena) != null) {
                throw new IOException("ID de cena duplicado: " + cena.getId());
            }
        }

        Set<Integer> destinosReservados = Set.of(Estado.CENA_ENCERRAR, Estado.CENA_RETOMAR);
        for (Cena cena : cenas) {
            for (Escolha escolha : cena.getEscolhas()) {
                int destino = escolha.getCenaDestinoId();
                if (!porId.containsKey(destino) && !destinosReservados.contains(destino)) {
                    throw new IOException("A cena " + cena.getId() + " aponta para destino inexistente " + destino + ".");
                }
            }
        }
        if (incluirApagao) {
            for (int id = Estado.CENA_APAGAO; id <= Estado.CENA_APAGAO + 2; id++) {
                if (!porId.containsKey(id)) {
                    throw new IOException("Cena de esgotamento ausente: " + id);
                }
            }
        }
    }

    private static InputStream abrirArquivo(String nomeArquivo) {
        return CarregadorRoteiroJson.class
                .getResourceAsStream("/roteiro/" + nomeArquivo);
    }

    private enum TipoRelacionamento { MINIMO, MAXIMO, IMPACTO }
    private record CenaOrdenada(int ordemGlobal, Cena cena) {}

    private static final class Documento { int schemaVersion;String arquivoOrigem; String arquivo;int totalCenasArquivo;List<CenaJson> cenas;}

    private static final class CenaJson { int ordemGlobal;int id;String capitulo;String personagemFalando;String textoNarrativo;List<EscolhaJson> escolhas;}

    private static final class EscolhaJson { String textoAlternativa;int cenaDestinoId;List<String> flagsConcedidas;String flagRequerida;int scoreMinimo;int participacaoMinima;int participacaoMaxima;
        int scoreGanho;
        int participacaoGanha;
        int custoEnergia;
        boolean permiteEnergiaInsuficiente;
        int energiaAbaixoDe;
        int confiancaMinima;
        int confiancaMaxima;
        int vidaSocialMinima;
        int vidaSocialMaxima;
        int carismaMinimo;
        int carismaMaximo;
        int logicaMinima;
        int logicaMaxima;
        int sanidadeMinima;
        int sanidadeMaxima;
        Map<String, Float> relacionamentoMinimo;
        Map<String, Float> relacionamentoMaximo;
        String interesseAlvo;
        Map<String, Float> impactoRelacionamento;
        Map<String, Integer> atributosAlterados;
    }
}