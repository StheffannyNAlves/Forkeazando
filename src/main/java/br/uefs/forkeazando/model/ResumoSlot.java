package br.uefs.forkeazando.model;

import java.time.LocalDateTime;

public record ResumoSlot(int numero, Status status, String nome, int capitulo, LocalDateTime data, int score, int participacao, String rank, int energia, int energiaMaxima) {
    public enum Status {
        VAZIO,
        OCUPADO,
        CORROMPIDO
    }
}