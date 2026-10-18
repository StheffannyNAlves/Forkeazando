package br.uefs.forkeazando.view.pixel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Catálogo chave -> desenho, com cache: cada arte é desenhada e renderizada no
 * máximo uma vez, então redesenhar a cena (ou voltar a ela) não custa nada.
 */
public final class RegistroDeArte {
    private final Map<String, Supplier<Tela>> construtores = new LinkedHashMap<>();
    private final Map<String, String[]> cache = new ConcurrentHashMap<>();

    public RegistroDeArte registrar(String chave, Supplier<Tela> construtor) {
        if (chave == null || chave.isBlank()) {
            throw new IllegalArgumentException("Chave de arte não pode ser vazia");
        }
        if (construtores.putIfAbsent(chave, construtor) != null) {
            throw new IllegalStateException("Arte duplicada: " + chave);
        }
        return this;
    }

    public boolean existe(String chave) {
        return chave != null && construtores.containsKey(chave);
    }

    public Set<String> chaves() {
        return Collections.unmodifiableSet(construtores.keySet());
    }

    /** Linhas ANSI prontas (cacheadas). O array devolvido é uma cópia. */
    public String[] linhas(String chave) {
        Supplier<Tela> construtor = construtores.get(chave);
        if (construtor == null) {
            throw new IllegalArgumentException("Arte não registrada: " + chave);
        }
        return cache.computeIfAbsent(chave, k -> construtor.get().renderizar()).clone();
    }
}