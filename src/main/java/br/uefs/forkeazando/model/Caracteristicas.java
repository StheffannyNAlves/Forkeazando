package br.uefs.forkeazando.model;
// Aqui é só para o protagonista, as caracteristicas dos personagens secundarios serão definidas em outra class
public class Caracteristicas {
    private final boolean perfeccionista; // true = Perfeccionista, false = Relaxado(a)
    private final boolean sociavel;       // true = Sim, false = Não
    private final boolean estudoTeorico;  // true = Teórico(a), false = Prático(a)

    public Caracteristicas(boolean perfeccionista, boolean sociavel, boolean estudoTeorico) {
        this.perfeccionista = perfeccionista;
        this.sociavel = sociavel;
        this.estudoTeorico = estudoTeorico;
    }

    public boolean isPerfeccionista() { return perfeccionista; }
    public boolean isSociavel() { return sociavel; }
    public boolean isEstudoTeorico() { return estudoTeorico; }
}