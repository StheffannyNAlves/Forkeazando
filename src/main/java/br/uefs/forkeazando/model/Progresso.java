
package br.uefs.forkeazando.model;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Progresso {

    private final Map<String, String> finais = new LinkedHashMap<>();

    public boolean registrarFinal(Final f, LocalDateTime quando) {
        if (f == null || quando == null) {
            throw new IllegalArgumentException("Final e data não podem ser nulos");
        }

        if (finais.containsKey(f.getCodigo())) {
            return false;
        }

        finais.put(f.getCodigo(), quando.toString());
        return true;
    }

    public boolean alcancou(Final f) {
        return f != null && finais.containsKey(f.getCodigo());
    }

    public Optional<LocalDateTime> quandoAlcancou(Final f) {
        if (f == null || !finais.containsKey(f.getCodigo())) {
            return Optional.empty();
        }
        return Optional.of(LocalDateTime.parse(finais.get(f.getCodigo())));
    }

    public List<Final> finaisAlcancados() {
        return finais.keySet().stream()
                .map(Final::porCodigo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public boolean sincronizarFinais(Protagonista p, LocalDateTime agora) {
        if (p == null || agora == null) {
            throw new IllegalArgumentException("Protagonista e data não podem ser nulos");
        }

        Optional<Final> identificado = Final.identificar(p.getHistoricoFlags());

        return identificado.isPresent()
                && registrarFinal(identificado.get(), agora);
    }

}
