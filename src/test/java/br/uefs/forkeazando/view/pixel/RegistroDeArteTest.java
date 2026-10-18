package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RegistroDeArteTest {

    @Test
    void desenhaUmaUnicaVezEServeDoCache() {
        AtomicInteger desenhos = new AtomicInteger();
        RegistroDeArte r = new RegistroDeArte().registrar("a", () -> {
            desenhos.incrementAndGet();
            return new Tela(4, 2);
        });
        r.linhas("a");
        r.linhas("a");
        r.linhas("a");
        assertEquals(1, desenhos.get());
    }

    @Test
    void arrayDevolvidoECopiaEntaoQuemMudaNaoCorrompeOCache() {
        RegistroDeArte r = new RegistroDeArte().registrar("a", () -> new Tela(4, 2));
        String[] primeira = r.linhas("a");
        String original = primeira[0];
        primeira[0] = "lixo";
        assertEquals(original, r.linhas("a")[0]);
    }

    @Test
    void chaveInexistenteELancadaComMensagemClara() {
        RegistroDeArte r = new RegistroDeArte();
        assertFalse(r.existe("x"));
        assertFalse(r.existe(null));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r.linhas("x"));
        assertTrue(e.getMessage().contains("x"));
    }

    @Test
    void chaveDuplicadaOuVaziaERejeitada() {
        RegistroDeArte r = new RegistroDeArte().registrar("a", () -> new Tela(1, 1));
        assertThrows(IllegalStateException.class, () -> r.registrar("a", () -> new Tela(1, 1)));
        assertThrows(IllegalArgumentException.class, () -> r.registrar(" ", () -> new Tela(1, 1)));
    }
}
