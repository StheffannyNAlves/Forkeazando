package br.uefs.forkeazando.model;
// só sabe o estado da partida, mas tbm precisa conhecer o protagonista e o relacionamento com os personagens secundarios, além das decisões tomadas ao longo do jogo

public class Estado  {
    private Protagonista protagonista;
    private Trilha trilha;
    public enum Trilha {
        IC,
        DTEC,
        INDEP
    }

    private int capituloAtual = 1;
    private int cenaAtualId = 0;
    private int semestresExtras = 0;
    public static final int CENA_ENCERRAR = 999;
    private transient boolean alterado;

    public void iniciar(Protagonista protagonista) {
        if (protagonista == null) {
            throw new IllegalArgumentException("O protagonista não pode ser null.");
        }
        this.protagonista = protagonista;
        // Migração simples para partidas antigas que guardavam a flag, mas não o contador.
        if (semestresExtras == 0 && protagonista.temFlag("cap6_trancou")) {
            semestresExtras = 1;
        }
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
        this.cenaAtualId = novoid;
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
}