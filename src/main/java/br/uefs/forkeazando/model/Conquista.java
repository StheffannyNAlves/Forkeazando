package br.uefs.forkeazando.model;
import java.util.Optional;
import java.util.function.Predicate;

public enum Conquista {
    PRIMEIRO_PASSO(
            "primeiro_passo",
            "Primeiro Passo",
            "Toda jornada começa antes de a gente se sentir pronto.",
            p -> p.temFlag("cap3_concluido")
    ),

    AMIGO_DO_CARAMELO(
            "amigo_do_caramelo",
            "Do Meu Lado",
            "Às vezes, tudo que alguém precisa é que outra pessoa não vá embora.",
            p -> p.temFlag("amigo_do_caramelo")
                    && p.temFlag(Final.FLAG_CONCLUSAO)
    ),

    SOBREVIVENTE(
            "sobrevivente",
            "Ainda Aqui",
            "Nem todo dia foi bom. Mesmo assim, tu chegou até aqui.",
            p -> p.temFlag("hab_sobreviveu_greve")
    ),

    MARMITA_COMPARTILHADA(
            "marmita_compartilhada",
            "Cabe Mais Um",
            "Dividir o pouco que se tem também é uma forma de cuidado.",
            p -> p.temFlag("cap5b_dividiu_comida")
    ),

    SEM_MASCARA(
            "sem_mascara",
            "Sem Ensaiar",
            "Dessa vez, tu disse o que realmente pensava.",
            p -> p.temFlag("cap8_resposta_verdadeira")
    ),

    REDE_DE_APOIO(
            "rede_de_apoio",
            "Ninguém Faz Isso Só",
            "Em algum momento, aquelas pessoas deixaram de ser só rostos conhecidos.",
            p -> p.contarRelacionamentos(
                    Protagonista.NivelRelacionamento.ALIADO
            ) >= 3
    );
    
    private final String codigo;
    private final String titulo;
    private final String descricao;
    private final Predicate<Protagonista> condicao;

    Conquista(String codigo, String titulo, String descricao,
              Predicate<Protagonista> condicao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.condicao = condicao;
    }

    public String getCodigo()    { return codigo; }
    public String getTitulo()    { return titulo; }
    public String getDescricao() { return descricao; }


    public boolean atingida(Protagonista p){
       return condicao.test(p);
    }

    public static Optional<Conquista> porCodigo(String codigo){
        if (codigo == null)
            return Optional.empty();

        for(Conquista c: values()){
            if (c.getCodigo().equals(codigo)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

}
