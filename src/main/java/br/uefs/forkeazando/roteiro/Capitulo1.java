package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;
// Os comentários são precisos aqui
public class Capitulo1 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();
        // Cenas Iniciais
        Cena cena0 = new Cena(0, "Capítulo 1", "Sistema",
                "A tela do SISU pisca. A página recarrega pela 15ª vez. O servidor do MEC respira por aparelhos, mas finalmente carrega a tabela: 'Aprovada - Engenharia de Computação'. O seu primeiro erro já foi validado pelo governo federal.");
        cena0.adicionarEscolha(new Escolha.Builder("Atualizar a página mais uma vez só para ter a certeza.", 1).build());
        cena0.adicionarEscolha(new Escolha.Builder("Fechar o browser e questionar as minhas escolhas de vida.", 1).build());
        cenas.add(cena0);

        Cena cena1 = new Cena(1, "Capítulo 1", "Narrador",
                "A matrícula não tem fila nem campus. É você, na frente do computador, lutando contra o portal da UEFS. O desafio real é transformar seu RG e histórico escolar em um PDF com menos de 2MB e rezar para o sistema não cair no meio do upload.");
        cena1.adicionarEscolha(new Escolha.Builder("Finalizar o envio e esperar o e-mail de confirmação.", 2).build());
        cenas.add(cena1);

        Cena cena2 = new Cena(2, "Capítulo 1", "Narrador",
                "O único contato presencial com a burocracia acontece dias depois, de forma assustadoramente rápida. Você vai ao campus apenas para entregar o Termo de Compromisso. O funcionário pega o papel, carimba, arquiva em uma pasta e diz: 'Pronto. Bem-vinda à Engenharia de Computação'. E é isso.");
        cena2.adicionarEscolha(new Escolha.Builder("Sair do colegiado e ver o que está acontecendo no pátio.", 3).build());
        cenas.add(cena2);

        Cena cena3 = new Cena(3, "Capítulo 1", "Narrador",
                "Primeira semana: Recepção dos Calouros. O campus é gigantesco e parece uma cidade própria. Egressos, professores e veteranos organizam a programação. Tem gente te mostrando os laboratórios, apresentando os projetos e tentando te convencer de que o curso é incrível (ou um pesadelo).");
        cena3.adicionarEscolha(new Escolha.Builder("Procurar a programação das oficinas preparatórias.", 4).build());
        cenas.add(cena3);

        Cena cena4 = new Cena(4, "Capítulo 1", "Narrador",
                "Para amenizar o choque de realidade, a semana oferece oficinas. Tem Introdução à Eletrônica, para quem nunca viu um resistor na vida, e a famigerada Preparatória para o PBL, já que a disciplina de programação não tem aula tradicional e você vai precisar aprender a sobreviver a uma discussão em grupo.");
        cena4.adicionarEscolha(new Escolha.Builder("Ir dar uma volta nas mesas da SIECOMP antes das oficinas começarem.", 5).build());
        cenas.add(cena4);

        Cena cena5 = new Cena(5, "Capítulo 1", "Narrador",
                "Atrás da mesa, há um Veterano com olheiras profundas. Ele segura um copo térmico amassado e olha pra você com uma mistura de pena e sarcasmo.");
        cena5.adicionarEscolha(new Escolha.Builder("Dizer olá.", 6).build());
        cenas.add(cena5);

        // Ramificação das flags
        Cena cena6 = new Cena(6, "Capítulo 1", "Veterano",
                "Caloura, né? Dá pra ver pelo brilho de esperança nos olhos. Aproveita. Isso some no segundo semestre, logo depois da primeira prova de Algoritmos II.");
        cena6.adicionarEscolha(new Escolha.Builder("Vim pra dominar o código e mudar o mundo.", 71)
                .comFlagConcedida("traco_competitiva")
                .comRelacionamento(PersonagemSecundario.VETERANO, -1f)
                .build());
        cena6.adicionarEscolha(new Escolha.Builder("Só quero um diploma e um emprego remoto pra pagar as contas.", 72)
                .comFlagConcedida("traco_pragmatica")
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .build());
        cenas.add(cena6);

        // Cena 7A (Offset 71)
        Cena cena71 = new Cena(71, "Capítulo 1", "Veterano",
                "Boa sorte tentando ser o Linus Torvalds. A gente se vê na prova final de Cálculo.");
        cena71.adicionarEscolha(new Escolha.Builder("Onde é que fica a sala de aula?", 8).build());
        cenas.add(cena71);

        // Cena 7B (Offset 72)
        Cena cena72 = new Cena(72, "Capítulo 1", "Veterano",
                "Essa é a atitude. Sobrevivência. O mercado não quer heróis, quer quem entregue a feature na sexta-feira às 18h sem derrubar o servidor.");
        cena72.adicionarEscolha(new Escolha.Builder("Onde é que fica a sala de aula?", 8).build());
        cenas.add(cena72);

        Cena cena8 = new Cena(8, "Capítulo 1", "Veterano",
                "Fica no módulo da frente. Boa sorte. E evita a coxinha da cantina às segundas-feiras.");
        cena8.adicionarEscolha(new Escolha.Builder("Ir para a primeira aula.", 9).build());
        cenas.add(cena8);

        // Início da distribuição de Score e dreno de Energia
        Cena cena9 = new Cena(9, "Capítulo 1", "Professor de Hardware",
                "O roteiro tá na bancada. Façam o LED piscar. Se sair fumaça da protoboard, desliguem a fonte. Volto em duas horas. Um colega ao seu lado já começa a ligar cabos freneticamente e quase causa um curto-circuito.");
        cena9.adicionarEscolha(new Escolha.Builder("Ignorar todo mundo, ler o datasheet e montar o circuito com calma.", 10)
                .comScoreGanho(5).comCustoEnergia(15).build());
        cena9.adicionarEscolha(new Escolha.Builder("Ajudar o colega a perceber que ele inverteu a polaridade da fonte.", 10)
                .comScoreGanho(3).comCustoEnergia(20)
                .comRelacionamento(PersonagemSecundario.COLEGA_RIVAL, 2f)
                .build());
        cena9.adicionarEscolha(new Escolha.Builder("Ficar olhando pro celular e esperar que alguém do grupo termine.", 10)
                .comScoreGanho(0).comCustoEnergia(5).build());
        // Opção Oculta
        cena9.adicionarEscolha(new Escolha.Builder("Esperar o professor sair da sala e copiar o esquema elétrico do grupo da frente.", 10)
                .comFlagRequerida("traco_pragmatica")
                .comScoreGanho(3).comCustoEnergia(0).build());
        cenas.add(cena9);

        // Sessão  PBL
        Cena cena10 = new Cena(10, "Capítulo 1", "Colega / Rival",
                "Primeira sessão de PBL de EXA854. O professor tá no canto, em silêncio. Precisamos definir os papéis pra resolver esse problema de arquitetura na nossa discussão: Coordenador, Quadro ou Mesa. Quem assume o quê?");
        cena10.adicionarEscolha(new Escolha.Builder("Eu assumo a Coordenação. Foco nas metas, pessoal.", 11)
                .comScoreGanho(5).comParticipacaoGanha(5).comCustoEnergia(20).comFlagConcedida("traco_lideranca").build());
        cena10.adicionarEscolha(new Escolha.Builder("Eu fico no Quadro. Vou mapear as ideias.", 11)
                .comScoreGanho(3).comParticipacaoGanha(3).comCustoEnergia(20).comFlagConcedida("traco_exposto").build());
        cena10.adicionarEscolha(new Escolha.Builder("Eu fico na Mesa. Transcrevo tudo pro Word e não me estresso.", 11)
                .comScoreGanho(2).comParticipacaoGanha(0).comCustoEnergia(10).comFlagConcedida("traco_passivo").build());
        // Opção Oculta Competitiva (Alto risco)
        cena10.adicionarEscolha(new Escolha.Builder("A discussão de vocês tá um caos. Eu assumo o quadro, dito a arquitetura final e fecho o problema sozinha.", 11)
                .comFlagRequerida("traco_competitiva")
                .comScoreGanho(8).comParticipacaoGanha(5).comCustoEnergia(40)
                .comFlagConcedida("irritou_rival")
                .comRelacionamento(PersonagemSecundario.COLEGA_RIVAL, -3f)
                .build());
        cenas.add(cena10);

        // Ramificação das Trilhas
        Cena cena11 = new Cena(11, "Capítulo 1", "Narrador",
                "Sua primeira semana acabou. O sistema mastigou a sua paciência. Você tem uma janela de tempo livre antes de ir pra casa. O que decide fazer?");
        cena11.adicionarEscolha(new Escolha.Builder("Voltar para a Oficina de Eletrônica para tentar entender o circuito.", 12).build());
        cena11.adicionarEscolha(new Escolha.Builder("Investigar o Grupo de Pesquisa que o professor mencionou.", 15)
                .comParticipacaoMinima(3).build());
        cena11.adicionarEscolha(new Escolha.Builder("Ir beber no Módulo 8 com a galera.", 18)
                .comScoreMinimo(7).build());
        cenas.add(cena11);


        // Caminho A: DTEC
        Cena cena12 = new Cena(12, "Capítulo 1", "Narrador",
                "Você volta à oficina. Tá vazia, exceto por um zumbido de osciloscópio. Passa horas brigando com um fio solto na protoboard. Ninguém vai te dar a resposta de mão beijada.");
        cena12.adicionarEscolha(new Escolha.Builder("Continuar testando as portas lógicas.", 13).build());
        cenas.add(cena12);

        Cena cena13 = new Cena(13, "Capítulo 1", "Narrador",
                "O LED acende. Uma vitória minúscula, mas completamente sua. O veterano do fundo da sala acena com a cabeça. Bem-vinda ao inferno ou paraiso do hardware, onde a teoria suja as mãos.");
        cena13.adicionarEscolha(new Escolha.Builder("Encerrar Fase 1 e voltar ao Menu Inicial.", Estado.CENA_ENCERRAR).build());
        cenas.add(cena13);

        // Caminho B: IC
        Cena cena15 = new Cena(15, "Capítulo 1", "Narrador",
                "Você entra na sala do grupo de pesquisa. Tem mestrandos debatendo agressivamente sobre complexidade ciclomática como se fosse fofoca de reality show. O Orientador repara em você.");
        cena15.adicionarEscolha(new Escolha.Builder("Perguntar se tem vagas para Iniciação Científica.", 16)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 2f)
                .build());
        cenas.add(cena15);

        Cena cena16 = new Cena(16, "Capítulo 1", "Narrador",
                "O Orientador aponta pra uma cadeira. 'Lê os últimos 5 artigos publicados pelo grupo pra segunda-feira', diz ele. O desafio é absurdo, mas reacende a sua vontade de competir no sistema.");
        cena16.adicionarEscolha(new Escolha.Builder("Encerrar Fase 1 e voltar ao Menu Inicial.", Estado.CENA_ENCERRAR).build());
        cenas.add(cena16);

        // Caminho C: Independente
        Cena cena18 = new Cena(18, "Capítulo 1", "Narrador",
                "Você já mostrou que sabe jogar o jogo (acumulou score suficiente). Merece uma pausa. Pisa no Módulo 8. O barulho das peças de dominó batendo na mesa é terapêutico.");
        cena18.adicionarEscolha(new Escolha.Builder("Sentar na mesa com o pessoal que tá falando de freelances.", 19).build());
        cenas.add(cena18);

        Cena cena19 = new Cena(19, "Capítulo 1", "Narrador",
                "A conversa foge das atas do colegiado para projetos de software paralelos e clientes reais. Você percebe que, talvez, o verdadeiro currículo seja aquele que a universidade não consegue controlar.");
        cena19.adicionarEscolha(new Escolha.Builder("Encerrar Fase 1 e voltar ao Menu Inicial.", Estado.CENA_ENCERRAR).build());
        cenas.add(cena19);

        return cenas;
    }
}