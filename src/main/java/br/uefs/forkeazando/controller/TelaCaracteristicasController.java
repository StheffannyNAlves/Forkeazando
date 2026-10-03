package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.TelaCaracteristicas;

public class TelaCaracteristicasController {

    private final TelaCaracteristicas view;

    public TelaCaracteristicasController(TelaCaracteristicas view) {
        this.view = view;
    }

    public Protagonista iniciar() {
        System.out.print("Seu nome: ");
        String nome = Entrada.lerLinha();

        boolean perfeccionista = view.perguntarPersonalidade();
        boolean sociavel       = view.perguntarSociavel();
        boolean estudoTeorico  = view.perguntarTipoDeEstudo();
        var situacao           = view.perguntarSituacaoEconomica();
        var vidaSocial         = view.perguntarVidaSocial();
        var experiencia        = view.perguntarExperienciaEM();

        Protagonista p = new Protagonista(
                nome, perfeccionista, sociavel, estudoTeorico,
                vidaSocial, experiencia, situacao);

        System.out.println();
        System.out.println("─── " + p.getNome() + " ───");
        System.out.println("Energia máxima: " + p.getEnergiaMaxima());
        System.out.println("Confiança:      " + p.getConfianca());
        System.out.println("Vida social:    " + p.getVidaSocial());
        System.out.print("Pressione ENTER para começar o Capítulo 1...");
        Entrada.lerLinha();

        return p;
    }
}