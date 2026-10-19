package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo1 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena0 = new Cena(0, "Capítulo 1 — Matrícula", "Narrador",
                "5h47 da manhã.\n\n" +
                        "A aba do SISU tá aberta há trinta e sete minutos. Você já " +
                        "recarregou dezenove vezes. O F5 do seu teclado tá fazendo um " +
                        "barulho que ele não fazia antes.\n\n" +
                        "A tela pisca.\n\n" +
                        "{{APROVACAO}}\n\n" +
                        "Você olha pra parede. Olha pro teto. Olha de novo pra tela.\n\n" +
                        "Continua lá.\n\n" +
                        "(Nossa.)\n\n" +
                        "Sua mãe tá dormindo no quarto ao lado. Você fecha o notebook " +
                        "bem devagar, como se o barulho pudesse estragar.");

        cena0.adicionarEscolha(new Escolha.Builder(
                "Acordar ela. Você precisa ver a cara dela.", 1).build());
        cena0.adicionarEscolha(new Escolha.Builder(
                "Deixar dormir. Isso é seu por enquanto.", 1)
                .comFlagConcedida("guardou_pra_si").build());
        cenas.add(cena0);

        Cena cena1 = new Cena(1, "Capítulo 1", "Narrador",
                "Segunda-feira, 6h da manhã. Você acorda com um alarme que " +
                        "nem lembra de ter programado. Hoje é matrícula.\n\n" +
                        "6h01. ÁGATA fora do ar.\n\n" +
                        "7h. Fora.\n\n" +
                        "8h. Você abre o grupo dos calouros pra ver se alguém sabe de algo.\n\n" +
                        "O grupo tem 340 mensagens. Todas são 'alguém sabe o que é isso?'.\n\n" +
                        "Você fecha o grupo.\n\n" +
                        "14h. Fora.\n\n" +
                        "17h48. Você já mandou mensagem pro suporte, pra coordenação, " +
                        "pro DCE e pra uma amiga que faz Direito. Ninguém respondeu.\n\n" +
                        "17h52. O sistema volta.\n\n" +
                        "A vaga de 'Introdução à Engenharia' foi preenchida às 17h49.\n\n" +
                        "Por alguém que entrou três minutos antes de você.\n\n" +
                        "(Três minutos.)\n\n" +
                        "(TRÊS.)\n\n" +
                        "Sobrou Cálculo I com o professor que todo mundo chama de 'o pesadelo'.\n\n" +
                        "Você nem sabe quem é.");

        cena1.adicionarEscolha(new Escolha.Builder(
                "Ler o edital inteiro. Nunca mais.", 2)
                .comFlagConcedida("hab_olho_edital")
                .comCustoEnergia(15).build());
        cena1.adicionarEscolha(new Escolha.Builder(
                "Mandar no grupo perguntando se alguém entendeu.", 2)
                .comFlagConcedida("pediu_ajuda_grupo").build());
        cena1.adicionarEscolha(new Escolha.Builder(
                "Foda-se. Vou pegar o que der.", 2)
                .comFlagConcedida("aceitou_caos")
                .comAtributo("sanidade", 3).build());
        cenas.add(cena1);

        Cena cena2 = new Cena(2, "Capítulo 1", "Narrador",
                "Você entrega o Termo de Compromisso.\n\n" +
                        "O funcionário olha o papel. Carimba. Assina. Arquiva.\n\n" +
                        "'Pronto.'\n\n" +
                        "Trinta segundos.\n\n" +
                        "(Cinco anos de estudo.)\n\n" +
                        "(Trinta segundos.)\n\n" +
                        "O RU tá com fila dando a volta no bloco. Cardápio: frango com " +
                        "quiabo. Você não sabe se é frango ou se é quiabo. Talvez os dois.\n\n" +
                        "R$ 2,00 com cartão. R$ 17,18 sem.\n\n" +
                        "Você ainda não tem cartão.\n\n" +
                        "Na fila, uma menina de cabelo cacheado te olha. Hesita. Fala:\n\n" +
                        "'Ei. Você é caloura também? Eu não conheço NINGUÉM aqui. " +
                        "Tipo, ninguém. Ontem eu falei bom dia pra uma planta achando " +
                        "que era uma pessoa.'");

        cena2.adicionarEscolha(new Escolha.Builder(
                "Puxar conversa. Ela parece tão perdida quanto você.", 3)
                .comFlagConcedida("conheceu_malu")
                .comRelacionamento(PersonagemSecundario.MALU, 2f)
                .comAtributo("vidaSocial", 2).build());
        cena2.adicionarEscolha(new Escolha.Builder(
                "Rir e responder de leve. Você também tá perdida.", 3)
                .comRelacionamento(PersonagemSecundario.MALU, 1f).build());
        cena2.adicionarEscolha(new Escolha.Builder(
                "Focar no cartão do RU. A fila tá aumentando.", 3)
                .comFlagConcedida("item_cartao_ru").build());
        cena2.adicionarEscolha(new Escolha.Builder(
                "Você não pode pagar R$ 17. Vai atrás do cartão agora.", 3)
                .comFlagRequerida("origem_apertada")
                .comFlagConcedida("item_cartao_ru")
                .comAtributo("sanidade", -2).build());
        cenas.add(cena2);

        Cena cena3 = new Cena(3, "Capítulo 1", "Narrador",
                "No pátio, um caramelo passa com a calma de quem tem apê ali.\n\n" +
                        "'Esse é o Caramelo', alguém avisa. 'Ele é do DCE.'\n\n" +
                        "Na SIECOMP, um cara com copo térmico amassado te olha. O copo " +
                        "tá tão amassado que parece que já caiu do terceiro andar. Ele " +
                        "segura o copo como se fosse um troféu.\n\n" +
                        "Ele não sorri. Ele não fala. Ele só te olha.\n\n" +
                        "(Ele tá me julgando? Me avaliando? Com sono?)");

        cena3.adicionarEscolha(new Escolha.Builder("Falar com ele.", 4)
                .comFlagConcedida("falou_com_caio").build());
        cena3.adicionarEscolha(new Escolha.Builder(
                "Dar um salgado pro Caramelo. Ele parece menos assustador.", 4)
                .comFlagConcedida("amigo_do_caramelo")
                .comAtributo("sanidade", 3).build());
        cena3.adicionarEscolha(new Escolha.Builder(
                "Passar reto. Você não precisa de veterano desanimando.", 5)
                .comFlagConcedida("evitou_caio")
                .comRelacionamento(PersonagemSecundario.VETERANO, -1f).build());
        cena3.adicionarEscolha(new Escolha.Builder(
                "Ir direto. Você não tem problema em puxar conversa.", 4)
                .comFlagRequerida("perfil_sociavel")
                .comFlagConcedida("falou_com_caio")
                .comAtributo("confianca", 1).build());
        cena3.adicionarEscolha(new Escolha.Builder(
                "Observar de longe. Você precisa medir o terreno.", 4)
                .comFlagRequerida("origem_isolado")
                .comFlagConcedida("observou_caio").build());
        cenas.add(cena3);

        Cena cena4 = new Cena(4, "Capítulo 1", "Caio Andrade",
                "Ele não espera você falar.\n\n" +
                        "'Primeiro semestre?'\n\n" +
                        "Você confirma com a cabeça.\n\n" +
                        "Ele dá um gole no café. Aí fala como se estivesse receitando:\n\n" +
                        "'Não compra livro. Ninguém usa. Todo mundo compra no primeiro " +
                        "semestre e se arrepende em outubro. Se precisar, tem PDF no drive.'\n\n" +
                        "Pausa.\n\n" +
                        "'Pronto. Foi a última coisa que eu falei de graça.'");

        cena4.adicionarEscolha(new Escolha.Builder(
                "Vim pra dominar o código e mudar o mundo.", 71)
                .comFlagConcedida("traco_competitiva")
                .comRelacionamento(PersonagemSecundario.VETERANO, -1f).build());
        cena4.adicionarEscolha(new Escolha.Builder(
                "Só quero o diploma e um emprego remoto.", 72)
                .comFlagConcedida("traco_pragmatica")
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena4.adicionarEscolha(new Escolha.Builder(
                "E você? Tá aqui há quanto tempo?", 73)
                .comFlagConcedida("traco_empatica")
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena4.adicionarEscolha(new Escolha.Builder(
                "O que tem no drive?", 5)
                .comAtributo("confianca", 1)
                .comFlagConcedida("caio_passou_drive").build());
        cenas.add(cena4);

        Cena cena71 = new Cena(71, "Capítulo 1", "Caio Andrade",
                "Ele te olha por cima do copo.\n\n" +
                        "'Mudar o mundo.'\n\n" +
                        "Pausa longa. Ele não tá rindo de você.\n\n" +
                        "'Leva bomba e chiclete pra final de Cálculo. Três horas de prova.'\n\n" +
                        "Outro gole.\n\n" +
                        "'Não é exagero.'");
        cena71.adicionarEscolha(new Escolha.Builder("Onde é a sala?", 5).build());
        cenas.add(cena71);

        Cena cena72 = new Cena(72, "Capítulo 1", "Caio Andrade",
                "'Finalmente alguém com senso.'\n\n" +
                        "Ele sorri torto. É o primeiro sorriso que você vê nele.\n\n" +
                        "'O mercado não quer herói. Quer quem entrega na sexta às 18h " +
                        "sem derrubar o servidor.'");
        cena72.adicionarEscolha(new Escolha.Builder("Onde é a sala?", 5)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cenas.add(cena72);

        Cena cena73 = new Cena(73, "Capítulo 1", "Caio Andrade",
                "Ele para. O copo fica no ar.\n\n" +
                        "'Cinco anos.'\n\n" +
                        "'Entrei aos 18. Reprovei Cálculo duas vezes. Perdi a bolsa. " +
                        "Tô aqui por teimosia.'\n\n" +
                        "Ele bebe. Volta a ser o cara cínico.");
        cena73.adicionarEscolha(new Escolha.Builder("Digo que entendo.", 5)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE)
                .comFlagConcedida("mentoria_caio").build());
        cena73.adicionarEscolha(new Escolha.Builder(
                "Pergunto se ele quer ajuda pra estudar.", 5)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comFlagConcedida("mentoria_caio").build());
        cenas.add(cena73);

        Cena cena5 = new Cena(5, "Capítulo 1", "Prof. Rafael Meira",
                "O professor escreve no quadro sem olhar pra turma:\n\n" +
                        "'Hoje vocês vão montar um circuito simples e descobrir por que ele funciona — ou não.'\n\n" +
                        "Ele deixa sobre a bancada protoboards, resistores, diodos e LEDs. Ao lado, " +
                        "uma fonte, um gerador de funções e um osciloscópio que parece mais caro que a sala inteira.\n\n" +
                        "'Antes de ligar qualquer coisa: leiam o roteiro, confiram os componentes e " +
                        "não tratem o datasheet como decoração. Volto em duas horas.'\n\n" +
                        "A porta fecha. Silêncio por três segundos.\n\n" +
                        "(Eu sei o que é um diodo no papel. Isso não significa que eu saiba reconhecer " +
                        "o que ele está fazendo no circuito.)\n\n" +
                        "Aí alguém do fundo grita: 'PEGA O DATASHEET'. A sala se agita. Uma dupla " +
                        "quer ligar a fonte imediatamente; outra tenta descobrir a polaridade do LED. " +
                        "Você percebe que montar é só o começo: se a ligação estiver errada ou a corrente " +
                        "não for limitada, o componente pode queimar antes de vocês entenderem qualquer coisa.");

        cena5.adicionarEscolha(new Escolha.Builder(
                "Separar LED, resistor e diodo; conferir polaridade e identificação antes de montar.", 16)
                .comScoreGanho(4).comCustoEnergia(10)
                .comFlagConcedida("cap1_identificou_componentes")
                .comAtributo("confianca", 1).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Ler o datasheet e cruzar os valores com o esquema do roteiro.", 16)
                .comScoreGanho(5).comCustoEnergia(15)
                .comFlagConcedida("cap1_interpretou_esquema")
                .comFlagConcedida("hab_foco_total")
                .comAtributo("confianca", 2).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Ajudar o pessoal do lado a conferir polaridade e resistor antes de ligar a fonte.", 16)
                .comScoreGanho(3).comCustoEnergia(20)
                .comFlagConcedida("cap1_identificou_componentes")
                .comRelacionamento(PersonagemSecundario.DANDARA, 2f).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Ficar no celular. Alguém vai resolver.", 16)
                .comCustoEnergia(5).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Copiar o esquema do grupo da frente.", 16)
                .comFlagRequerida("traco_pragmatica")
                .comScoreGanho(3)
                .comFlagConcedida("item_esquema_copiado")
                .comAtributo("confianca", -2).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Anotar cada passo. Se der errado, refaz.", 16)
                .comFlagRequerida("perfil_perfeccionista")
                .comScoreGanho(7).comCustoEnergia(25)
                .comAtributo("confianca", 3).build());
        cena5.adicionarEscolha(new Escolha.Builder(
                "Você lê o datasheet em diagonal e já sabe onde tá o erro.", 16)
                .comLogicaMinima(6)
                .comScoreGanho(7).comCustoEnergia(10).build());
        cenas.add(cena5);

        // Cena intermediária: o jogador tem a oportunidade de coordenar
        // uma pequena atividade antes do PBL principal.
        Cena cena16 = new Cena(16, "Capítulo 1", "Narrador",
                "Antes de começar o Problema 2, o tutor pede que o grupo organize " +
                        "rapidamente o que acabou de acontecer na oficina.\n\n" +
                        "'Em dois minutos: alguém resume o que funcionou, alguém anota " +
                        "as observações e alguém organiza a ordem das ideias.'\n\n" +
                        "As pessoas se entreolham. A tarefa é simples, mas alguém precisa " +
                        "distribuir o trabalho.\n\n" +
                        "(Você pode tomar a frente ou deixar outra pessoa conduzir.)");

        cena16.adicionarEscolha(new Escolha.Builder(
                "Assumir a coordenação e distribuir as tarefas.", 6)
                .comFlagConcedida("ja_foi_coordenador")
                .comScoreGanho(2).comParticipacaoGanha(2)
                .comCustoEnergia(5).build());
        cena16.adicionarEscolha(new Escolha.Builder(
                "Anotar as observações enquanto outra pessoa organiza o grupo.", 6)
                .comFlagConcedida("led_anotou_observacoes")
                .comScoreGanho(2).comCustoEnergia(3).build());
        cena16.adicionarEscolha(new Escolha.Builder(
                "Ajudar com as ideias, sem assumir a liderança.", 6)
                .comFlagConcedida("led_contribuiu_ideias")
                .comScoreGanho(2).comCustoEnergia(3).build());
        cena16.adicionarEscolha(new Escolha.Builder(
                "Esperar que alguém tome a iniciativa.", 6)
                .comCustoEnergia(0)
                .build());
        cenas.add(cena16);

        Cena cena6 = new Cena(6, "Capítulo 1", "Narrador",
                "Sala 203. Mesa redonda. Quadro branco. Seis pessoas.\n\n" +
                        "O tutor entra com uma folha impressa. Cola no quadro sem " +
                        "olhar pra ninguém.\n\n" +
                        "'Problema 2 — Jogo Narrativo. Fase 1: modelagem de classes, " +
                        "esboço do Model, menu inicial e tela de características.'\n\n" +
                        "Ele vira pra turma.\n\n" +
                        "'Hoje a meta é essa. Vocês têm quarenta minutos.'\n\n" +
                        "Pausa.\n\n" +
                        "'Estrutura de sempre: Ideias, Fatos, Questões. " +
                        "Quadro anota. Mesa transcreve. Coordenador conduz.'\n\n" +
                        "Ele senta no canto com um tablet e não fala mais nada.\n\n" +
                        "(Todo mundo olhando um pro outro.)\n\n" +
                        "(Alguém tem que começar.)");

        cena6.adicionarEscolha(new Escolha.Builder(
                "Assumir a Coordenação. Alguém tem que conduzir isso.", 7)
                .comScoreGanho(6).comParticipacaoGanha(5).comCustoEnergia(25)
                .comAtributo("confianca", 2)
                .comFlagConcedida("pbl_coordenador").build());
        cena6.adicionarEscolha(new Escolha.Builder(
                "Ficar no Quadro. Vou anotar as ideias conforme aparecerem.", 7)
                .comScoreGanho(4).comParticipacaoGanha(3).comCustoEnergia(15)
                .comFlagConcedida("pbl_quadro").build());
        cena6.adicionarEscolha(new Escolha.Builder(
                "Ficar na Mesa. Transcrevo pro doc, ninguém me pressiona.", 7)
                .comScoreGanho(2).comCustoEnergia(8)
                .comFlagConcedida("pbl_mesa").build());
        cena6.adicionarEscolha(new Escolha.Builder(
                "Não assumo papel, mas quando alguém propõe algo bom, elaboro.", 7)
                .comScoreGanho(3).comParticipacaoGanha(4).comCustoEnergia(10)
                .comAtributo("confianca", 1)
                .comFlagConcedida("pbl_contribuiu").build());
        cena6.adicionarEscolha(new Escolha.Builder(
                "Ceder a vez. Você já coordenou no problema passado.", 7)
                .comFlagRequerida("ja_foi_coordenador")
                .comScoreGanho(3).comCustoEnergia(10)
                .comRelacionamento(PersonagemSecundario.ALAN, 2f)
                .comFlagConcedida("pbl_cedeu_vez").build());
        cena6.adicionarEscolha(new Escolha.Builder(
                "Não participar. Celular até acabar.", 7)
                .comScoreGanho(0).comCustoEnergia(0)
                .comPermiteEnergiaInsuficiente()
                .comAtributo("confianca", -2)
                .comFlagConcedida("pbl_passivo").build());
        cenas.add(cena6);

        Cena cena7 = new Cena(7, "Capítulo 1", "Narrador",
                "Sexta-feira. Última atividade da semana acabou.\n\n" +
                        "A tarde inteira tá na sua frente. Pela primeira vez em cinco dias, " +
                        "ninguém tá te pedindo nada.\n\n" +
                        "(O que eu faço agora?)");
        cena7.adicionarEscolha(new Escolha.Builder("Voltar pra oficina. Ainda quero tentar resolver o circuito.", 8)
                .comCustoEnergia(10).build());
        cena7.adicionarEscolha(new Escolha.Builder("Ir embora e descansar. A semana foi longa.", 14)
                .comAtributo("energia", 20).build());
        cena7.adicionarEscolha(new Escolha.Builder(
                "Passar no Módulo 8. Não preciso merecer uma pausa.", 13)
                .comCustoEnergia(10)
                .build());
        cena7.adicionarEscolha(new Escolha.Builder(
                "Voltar pra oficina mesmo exausta. Café e teimosia.", 8)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(5)
                .comAtributo("confianca", -2).build());
        cenas.add(cena7);

        Cena cena8 = new Cena(8, "Capítulo 1", "Narrador",
                "A oficina tá vazia.\n\n" +
                        "Só você e o zumbido do osciloscópio. O circuito da manhã continua na protoboard, " +
                        "com um fio amarelo saindo pela beirada.\n\n" +
                        "Você não liga a fonte de imediato. Confere a orientação do diodo no datasheet, " +
                        "segue as conexões linha por linha e percebe que não tem certeza se a montagem " +
                        "corresponde ao esquema. Corrige um jumper e só então energiza o circuito.\n\n" +
                        "O LED continua apagado.\n\n" +
                        "Você pega o multímetro. Depois olha para o gerador de funções e o osciloscópio. " +
                        "O roteiro pede para observar o comportamento do diodo e comparar a curva tensão-corrente. " +
                        "Não basta ver uma luz: você precisa descobrir o que está acontecendo eletricamente.\n\n" +
                        "Ajusta o sinal, verifica as escalas e tenta usar o modo X-Y. A curva aparece deslocada. " +
                        "Você não sabe ainda se o problema está na montagem, nas referências dos canais ou " +
                        "na configuração do instrumento. Pela primeira vez, entende por que o roteiro manda " +
                        "conferir cada etapa antes de interpretar a tela.");
        cena8.adicionarEscolha(new Escolha.Builder(
                "Medir a tensão no resistor e comparar a leitura com o valor esperado.", 9)
                .comCustoEnergia(15).comAtributo("confianca", 2)
                .comFlagConcedida("cap1_mediu_circuito").build());
        cena8.adicionarEscolha(new Escolha.Builder(
                "Ir embora.", 14)
                .comCustoEnergia(0)
                .comAtributo("energia", 20)
                .build());
        cenas.add(cena8);

        Cena cena9 = new Cena(9, "Capítulo 1", "Narrador",
                "O LED acende. Uma luz vermelha minúscula. Tremendo de leve.\n\n" +
                        "Você quase comemora, mas volta para o roteiro. A prática não termina quando o componente acende. " +
                        "Você mede a tensão no resistor, estima a corrente pela Lei de Ohm e compara o resultado com " +
                        "o que esperava. Depois observa a curva do diodo e tenta identificar a região em que a corrente " +
                        "cresce de forma mais acentuada.\n\n" +
                        "O diodo de silício e o LED não se comportam de forma idêntica; quando você considera os dois " +
                        "no circuito, a leitura muda outra vez. Você anota valores, confere unidades e discute com " +
                        "suas próprias anotações antes de concluir qualquer coisa. Ainda há diferença entre a previsão " +
                        "e a medida — e agora você tem dados para investigar, não só um palpite.\n\n" +
                        "(Eu consegui fazer funcionar. Entender por que funcionou é outra etapa.)\n\n" +
                        "Caio aparece na porta. Sem falar nada, olha para a bancada e depois para você. Assente uma vez — " +
                        "bem devagar — e vai embora.\n\n" +
                        "Você fica olhando para a porta por uns cinco segundos.\n\n" +
                        "(Ele assentiu. Eu acho.)");
        cena9.adicionarEscolha(new Escolha.Builder(
                "Encerrar a semana e seguir para o próximo capítulo.",
                Estado.CENA_ENCERRAR)
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap1_depurou_circuito")
                .comFlagConcedida("cap1_oficina_tentada").build());
        cenas.add(cena9);

        Cena cena14 = new Cena(14, "Capítulo 1", "Narrador",
                "Você vai embora.\n\n" +
                        "No ônibus, o vidro tá embaçado e você desenha um 'UEFP' com o " +
                        "dedo sem perceber.\n\n" +
                        "Depois apaga.\n\n" +
                        "(Talvez eu tenha escolhido o curso errado.)\n\n" +
                        "(Talvez não.)");
        cena14.adicionarEscolha(new Escolha.Builder(
                "Encerrar a semana e seguir para o próximo capítulo.",
                Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap1_foi_embora").build());
        cenas.add(cena14);

        Cena cena13 = new Cena(13, "Capítulo 1", "Narrador",
                "Módulo 8.\n\n" +
                        "Dominó batendo. Cerveja gelada. Ninguém falando de prova, de CR, " +
                        "de edital.\n\n" +
                        "Você percebe que não pensa em faculdade há quarenta minutos.\n\n" +
                        "(Quarenta minutos.)");
        cena13.adicionarEscolha(new Escolha.Builder("Puxar conversa e conhecer melhor o pessoal.", 15)
                .comFlagConcedida("cap1_conversou_modulo8")
                .comAtributo("vidaSocial", 1).build());
        cena13.adicionarEscolha(new Escolha.Builder("Só ouvir por enquanto. Você não precisa render socialmente.", 15)
                .comAtributo("energia", -5)
                .comAtributo("vidaSocial", 1).build());
        cena13.adicionarEscolha(new Escolha.Builder(
                "Alguém te chama pelo nome antes de você sentar.", 15)
                .comVidaSocialMinima(5)
                .comAtributo("vidaSocial", 1)
                .comFlagConcedida("cap1_reconhecido_modulo8").build());
        cenas.add(cena13);

        Cena cena15 = new Cena(15, "Capítulo 1", "Zé do Módulo 8",
                "A conversa segue sem grande revelação. Alguém conta uma história absurda " +
                        "da semana, outra pessoa reclama do sistema acadêmico e, por alguns " +
                        "minutos, ninguém tenta transformar a noite em oportunidade.\n\n" +
                        "Você não resolveu a faculdade. Só passou um tempo com outras pessoas. " +
                        "Por hoje, isso basta.");
        cena15.adicionarEscolha(new Escolha.Builder(
                "Encerrar a semana e seguir para o próximo capítulo.",
                Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap1_passou_no_modulo8").build());
        cenas.add(cena15);

        return cenas;
    }
}