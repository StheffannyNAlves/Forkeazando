package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado completo de uma partida: protagonista (com atributos, relacionamentos
 * e flags de decisão), trilha acadêmica, capítulo, cena atual e final alcançado.
 * É o objeto que o sistema de saves persiste e recupera.
 */
public class Estado  {

    /** Efeito de uma escolha sobre o vínculo com um personagem, para a view exibir. */
    public record ImpactoRelacionamento(PersonagemSecundario personagem, float valor, Protagonista.NivelRelacionamento nivel) {}

    private Protagonista protagonista;
    private Trilha trilha;
    public enum Trilha {
        IC,
        DTEC,
        INDEP
    }

    public static final int CAPITULO_FINAL = 8;
    private int capituloAtual = 1;
    private int cenaAtualId = 0;
    private int semestresExtras = 0;
    public static final int CENA_ENCERRAR = 999;
    private transient boolean alterado;
    public static final int CENA_RETOMAR = 998;  // sai do apagão e retoma o caminho original
    public static final int CENA_APAGAO = 970;   // 970, 971 e 972
    private int destinoPendente = -1;
    private boolean partidaConcluida;
    private String codigoFinal;
    public void iniciar(Protagonista protagonista) {
        if (protagonista == null) {
            throw new IllegalArgumentException("O protagonista não pode ser null.");
        }
        this.protagonista = protagonista;
        // Migração para partidas antigas que guardavam a flag, mas não o contador.
        if (semestresExtras == 0 && protagonista.temFlag("cap6_trancou")) {
            semestresExtras = 1;
        }
    }

    public static boolean ehCenaDeApagao(int cenaId) {
        return cenaId >= CENA_APAGAO && cenaId <= CENA_APAGAO + 2;
    }

    public boolean temDestinoPendente() {
        return destinoPendente >= 0;
    }

    public void guardarDestinoPendente(int destino) {
        destinoPendente = destino;
        marcarAlterado();
    }

    public int consumirDestinoPendente() {
        if (destinoPendente < 0) {
            throw new IllegalStateException("Não há destino pendente depois do apagão.");
        }
        int destino = destinoPendente;
        destinoPendente = -1;
        marcarAlterado();
        return destino;
    }

    public void avancarCapitulo() {
        capituloAtual++;
    }
    public Trilha getTrilha() {
        return trilha;
    }

    public void definirTrilha(Trilha novaTrilha) {
        if (novaTrilha == null) {
            throw new IllegalArgumentException(
                    "A trilha não pode ser null."
            );
        }

        if (trilha != null && trilha != novaTrilha) {
            throw new IllegalStateException(
                    "A trilha acadêmica não pode ser substituída."
            );
        }

        if (trilha == null) {
            trilha = novaTrilha;
            marcarAlterado();
        }
    }

    public Protagonista getProtagonista() {
        return protagonista;
    }

    public int getCapituloAtual() {
        return capituloAtual;
    }

    public int getCenaAtualId() {
        return cenaAtualId;
    }

    public int getSemestresExtras() {
        // Compatibilidade com saves antigos carregados sem passar por iniciar().
        if (semestresExtras == 0 && protagonista != null && protagonista.temFlag("cap6_trancou")) {
            semestresExtras = 1;
        }
        return semestresExtras;
    }

    public void adicionarSemestreExtra() {
        semestresExtras++;
        marcarAlterado();
    }

    public void setCenaAtualId(int novoid){
        if (this.cenaAtualId != novoid){
            this.cenaAtualId = novoid;
            marcarAlterado();
        }
    }

    public boolean temAlteracoes() {
        return alterado;
    }

    public void marcarAlterado() {
        alterado = true;
    }

    public void marcarSalvo() {
        alterado = false;
    }

    public void concluir(String codigoFinal){
        if (codigoFinal == null || Final.porCodigo(codigoFinal).isEmpty())
            throw new IllegalArgumentException("Final inválido");
        this.codigoFinal = codigoFinal;
        this.partidaConcluida = true;
        marcarAlterado();
    }
    /**
     * Confere se o estado, possivelmente lido de um arquivo editado ou
     * corrompido, é consistente.
     *
     * @return descrição de cada problema encontrado; lista vazia se estiver íntegro
     */
    public List<String> verificarIntegridade() {
        List<String> problemas = new ArrayList<>();

        if (protagonista == null) {
            problemas.add("protagonista ausente");
        } else {
            problemas.addAll(protagonista.problemasDeIntegridade());
        }
        if (capituloAtual < 1 || capituloAtual > CAPITULO_FINAL) {
            problemas.add("capítulo fora da faixa (1 a " + CAPITULO_FINAL + "): " + capituloAtual);
        }
        if (cenaAtualId < 0) {
            problemas.add("cena atual inválida: " + cenaAtualId);
        }
        if (semestresExtras < 0) {
            problemas.add("quantidade de semestres extras negativa: " + semestresExtras);
        }
        if (codigoFinal != null && Final.porCodigo(codigoFinal).isEmpty()) {
            problemas.add("final desconhecido: " + codigoFinal);
        }
        if (partidaConcluida && codigoFinal == null) {
            problemas.add("partida concluída sem código de final");
        }
        return problemas;
    }

    /**
     * Aplica ao estado todos os efeitos de uma escolha: trilha, flags, score,
     * participação, energia, relacionamentos e atributos
     *
     * @return o impacto em cada relacionamento afetado, para a interface exibir
     * @throws IllegalStateException se a escolha tentar trocar a trilha já definida
     */
    public List<ImpactoRelacionamento> aplicarEscolha(Escolha escolha) {
        if (escolha == null) {
            throw new IllegalArgumentException("A escolha não pode ser null.");
        }
        registrarTrilhaConcedida(escolha.getFlagsConcedidas());
        marcarAlterado();

        boolean trancamentoJaRegistrado = protagonista.temFlag("cap6_trancou");
        for (String flag : escolha.getFlagsConcedidas()) {
            protagonista.adicionarFlag(flag);
        }
        if (!trancamentoJaRegistrado && protagonista.temFlag("cap6_trancou")) {
            adicionarSemestreExtra();
        }

        protagonista.ganharScore(escolha.getScoreGanho());
        protagonista.ganharParticipacao(escolha.getParticipacaoGanha());
        protagonista.gastarEnergia(escolha.getCustoEnergia());

        List<ImpactoRelacionamento> impactos = new ArrayList<>();
        for (var entrada : escolha.getImpactosRelacionamento().entrySet()) {
            PersonagemSecundario personagem = entrada.getKey();
            float valor = entrada.getValue();

            if (escolha.getInteresseAlvo() != null
                    && escolha.getInteresseAlvo() == personagem.getInteresse()) {
                valor += Math.signum(valor);
            }
            protagonista.alterarRelacionamento(personagem, valor);
            impactos.add(new ImpactoRelacionamento(personagem, valor,
                    Protagonista.categorizarRelacionamento(
                            protagonista.getRelacionamento(personagem))));
        }

        for (var entrada : escolha.getAtributosAlterados().entrySet()) {
            protagonista.alterarAtributo(entrada.getKey(), entrada.getValue());
        }
        return impactos;
    }

    /**
     * Garante que Estado, histórico de flags e escolha concordem sobre a trilha
     * acadêmica e registra a primeira trilha escolhida.
     */
    public void registrarTrilhaConcedida(List<String> flagsConcedidas) {
        Trilha daEscolha = identificarTrilhaNasFlags(flagsConcedidas);
        Trilha doHistorico = identificarTrilhaNasFlags(protagonista.getHistoricoFlags());

        if (trilha != null && doHistorico != null && trilha != doHistorico) {
            throw new IllegalStateException("A trilha do Estado diverge das flags da protagonista.");
        }
        if (trilha != null && doHistorico == null) {
            throw new IllegalStateException("O Estado possui uma trilha, mas a protagonista não possui a flag correspondente.");
        }

        Trilha atual = trilha != null ? trilha : doHistorico;

        if (daEscolha != null && atual != null && daEscolha != atual) {
            throw new IllegalStateException("Não é permitido trocar a trilha acadêmica escolhida.");
        }

        if (atual == null && daEscolha != null) {
            definirTrilha(daEscolha);
        } else if (trilha == null && doHistorico != null) {
            // Partida antiga: recupera a trilha a partir das flags.
            definirTrilha(doHistorico);
        }
    }

    private static Trilha identificarTrilhaNasFlags(List<String> flags) {
        boolean ic = flags.contains("trilha_ic");
        boolean dtec = flags.contains("trilha_dtec");
        boolean indep = flags.contains("trilha_indep");

        if ((ic ? 1 : 0) + (dtec ? 1 : 0) + (indep ? 1 : 0) > 1) {
            throw new IllegalStateException(
                    "A protagonista possui mais de uma trilha acadêmica.");
        }
        if (ic) return Trilha.IC;
        if (dtec) return Trilha.DTEC;
        if (indep) return Trilha.INDEP;
        return null;
    }

    public boolean isPartidaConcluida() {
        return partidaConcluida;
    }

    public String getCodigoFinal() {
        return codigoFinal;
    }
}