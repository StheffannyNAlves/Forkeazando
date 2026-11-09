
package br.uefs.forkeazando.model;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Progresso {

    private  Map<String, String> finais = new LinkedHashMap<>();
    private  Map<String, String> colecao = new LinkedHashMap<>();
    private  Map<String, String> conquistas = new LinkedHashMap<>();

    private void garantirMapasInicializados() {
        if (finais == null) {
            finais = new LinkedHashMap<>();
        }
        if (colecao == null) {
            colecao = new LinkedHashMap<>();
        }
        if (conquistas == null) {
            conquistas = new LinkedHashMap<>();
        }
    }

    public boolean registrarFinal(Final f, LocalDateTime quando) {
        garantirMapasInicializados();
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
        garantirMapasInicializados();
        return f != null && finais.containsKey(f.getCodigo());
    }

    public Optional<LocalDateTime> quandoAlcancou(Final f) {
        garantirMapasInicializados();
        if (f == null || !finais.containsKey(f.getCodigo())) {
            return Optional.empty();
        }
        return Optional.of(LocalDateTime.parse(finais.get(f.getCodigo())));
    }

    public List<Final> finaisAlcancados() {
        garantirMapasInicializados();
        return finais.keySet().stream()
                .map(Final::porCodigo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public boolean sincronizarFinais(Protagonista p, LocalDateTime agora) {
        garantirMapasInicializados();
        if (p == null || agora == null) {
            throw new IllegalArgumentException("Protagonista e data não podem ser nulos");
        }

        Optional<Final> identificado = Final.identificar(p.getHistoricoFlags());

        return identificado.isPresent()
                && registrarFinal(identificado.get(), agora);
    }



    public boolean sincronizarColecao(Protagonista protagonista, LocalDateTime agora) {
        garantirMapasInicializados();
        if (protagonista == null || agora == null) {
            throw new IllegalArgumentException("Protagonista e data não podem ser nulos");
        }

        boolean alterou = false;
        for (String flag : protagonista.getHistoricoFlags()) {
            if (Colecionavel.porFlag(flag).isPresent()
                    && colecao.putIfAbsent(flag, agora.toString()) == null) {
                alterou = true;
            }
        }
        return alterou;
    }

    public boolean registrarConquista(Conquista conquista, LocalDateTime quando) {
        garantirMapasInicializados();
        if (conquista == null || quando == null) {
            throw new IllegalArgumentException("Conquista e data não podem ser nulas");
        }
        return conquistas.putIfAbsent(conquista.getCodigo(), quando.toString()) == null;
    }

    public boolean coletou(Colecionavel colecionavel) {
        garantirMapasInicializados();
        return colecionavel != null && colecao.containsKey(colecionavel.getFlag());
    }
    public Optional<LocalDateTime> quandoColetou(Colecionavel colecionavel) {
        garantirMapasInicializados();
        if (colecionavel == null || !colecao.containsKey(colecionavel.getFlag())) {
            return Optional.empty();
        }
        return Optional.of(LocalDateTime.parse(colecao.get(colecionavel.getFlag())));
    }

    public List<Conquista> conquistasDesbloqueadas() {
        garantirMapasInicializados();
        return conquistas.keySet().stream()
                .map(Conquista::porCodigo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
    public Optional<LocalDateTime> quandoDesbloqueou(Conquista conquista) {
        garantirMapasInicializados();
        if (conquista == null || !conquistas.containsKey(conquista.getCodigo()))
            return Optional.empty();
        return Optional.of(LocalDateTime.parse(conquistas.get(conquista.getCodigo())));
    }

    public List<Colecionavel> colecionaveisColetados() {
        garantirMapasInicializados();
        return colecao.keySet().stream()
                .map(Colecionavel::porFlag)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public boolean possui(Conquista conquista) {
        garantirMapasInicializados();
        return conquista != null && conquistas.containsKey(conquista.getCodigo());
    }








}
