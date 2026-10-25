package br.uefs.forkeazando.model;

import java.util.Optional;

public enum Colecionavel {
    OLHO_EDITAL("hab_olho_edital", Tipo.HABILIDADE, "Olho de Edital"),
    ESTOMAGO_FERRO("hab_estomago_ferro", Tipo.HABILIDADE, "Estômago de Ferro"),
    FOCO_TOTAL("hab_foco_total", Tipo.HABILIDADE, "Foco Total"),
    REDE_CONTATOS("hab_rede_contatos", Tipo.HABILIDADE, "Rede de Contatos"),
    PALAVRA_HONESTA("hab_palavra_honesta", Tipo.HABILIDADE, "Palavra Honesta"),
    SOBREVIVEU_GREVE("hab_sobreviveu_greve", Tipo.HABILIDADE, "Sobreviveu à Greve"),
    CONTRATO_ASSINADO("hab_contrato_assinado", Tipo.HABILIDADE, "Contrato Assinado"),

    CARTAO_RU("item_cartao_ru", Tipo.ITEM, "Cartão do RU"),
    ESQUEMA_COPIADO("item_esquema_copiado", Tipo.ITEM, "Esquema elétrico copiado"),
    PDFS_IC("item_pdfs_ic", Tipo.ITEM, "5 PDFs de IC"),
    CONTATO_FREELA("item_contato_freela", Tipo.ITEM, "Contato de freela"),
    VAGA_ESTAGIO("item_vaga_estagio", Tipo.ITEM, "QR code de vaga de estágio"),
    PROVA_EMAIL("item_prova_email", Tipo.ITEM, "Foto do e-mail comprometedor"),
    COPO_CAIO("item_copo_caio", Tipo.ITEM, "Copo térmico do Caio"),
    CONTATO_BIA("item_contato_bia", Tipo.ITEM, "Contato da Bia");

    public enum Tipo { HABILIDADE, ITEM }

    private final String flag;
    private final Tipo tipo;
    private final String nome;

    Colecionavel(String flag, Tipo tipo, String nome) {
        this.flag = flag;
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getFlag() { return flag; }
    public Tipo getTipo()   { return tipo; }
    public String getNome() { return nome; }

    public static Optional<Colecionavel> porFlag(String flag){
        for(Colecionavel colecionavel: Colecionavel.values()){
            if (colecionavel.getFlag().equals(flag)) {
                return Optional.of(colecionavel);
            }
        }
        return Optional.empty();
    }


}