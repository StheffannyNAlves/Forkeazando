# Forkeazando 🎮

Um jogo narrativo desenvolvido em Java como projeto da disciplina de Algoritmos II da Universidade Estadual de Feira de Santana (UEFS).

Em **Forkeazando**, você acompanha a trajetória de uma estudante de Engenharia de Computação durante sua graduação, tomando decisões que influenciam seus atributos, relacionamentos, experiências e caminhos dentro da universidade.

---

## Como jogar

O jogo é executado no **terminal** e utiliza o **JLine** para ler o teclado tecla a tecla (modo raw), o que permite navegar pelos menus com as setas.

### Requisitos

- Java 21 ou superior
- Maven 3.8+ (para compilar a partir do código-fonte)
- Um terminal de verdade (Terminal do Linux ou do macOS, Windows Terminal)

> **Não execute pelo console de execução da IDE.** Nele as setas do teclado não funcionam, porque o JLine não recebe um terminal real.

### Execução

**A partir do código-fonte:**

```bash
./rodar.sh
```

Caso necessário, dê permissão de execução ao script:

```bash
chmod +x rodar.sh
```

O script usa o Maven para compilar o projeto, resolver as dependências e gerar o classpath necessário para executar o jogo.

**A partir da release (JAR):**

```bash
java -jar forkeazando.jar
```

Execute sempre a partir da mesma pasta: a pasta `saves/` é criada ao lado de onde o jogo foi iniciado.

### Testes

```bash
mvn test
```

---

## Controles

### Menu principal e cenas

As opções são escolhidas digitando o **número** correspondente e pressionando `Enter`.

Durante uma cena, a opção `0` volta ao menu principal **sem encerrar a partida**. Use **Continuar** para retomá-la.

### Menus de seleção

Configurações, salvar, carregar e confirmações usam o teclado direcional:

```text
↑ / ↓   → navegar entre as opções
← / →   → alterar valores
Enter   → confirmar
```

Cada menu de confirmação tem uma opção **Cancelar**.

### Pausa

Durante o texto de uma cena:

```text
P → pausar
C → continuar
```

Ao pausar, o jogo exibe `PAUSADO - [C] Continuar`. A pausa só vale enquanto o texto da cena é exibido, e não nos menus.

---

## Configurações

O jogo possui configurações personalizáveis, persistidas entre execuções em:

```text
saves/configuracao.json
```

- **Velocidade do texto:** altera o ritmo de exibição das cenas.
- **Volume:** reservado para quando o áudio for implementado. A opção é gravada, mas ainda não tem efeito.

---

## Sistema de salvamento

Há **três slots de salvamento**, independentes entre si:

```text
saves/
├── configuracao.json
├── slot1.json
├── slot2.json
└── slot3.json
```

Cada save guarda o estado da partida: capítulo, cena atual, atributos, relacionamentos e flags (decisões, habilidades e itens).

- **Salvar** pelo menu principal ou ao sair com progresso não salvo.
- **Carregar** escolhendo um slot na lista, que mostra nome do jogador, capítulo e data e hora.
- **Confirmação** antes de sobrescrever um slot. Se todos estiverem ocupados e houver progresso não salvo, o jogador escolhe qual sobrescrever.
- **Continuar** retoma a partida em memória depois de voltar ao menu com `0`.

### Persistência

- Os saves são gravados em **JSON** com o **Gson**.
- A gravação é feita em um arquivo temporário (`.tmp`) que só substitui o save quando está completo, para uma falha no meio da escrita não destruir o save anterior.
- Os relacionamentos são gravados pelo **código** do personagem, que não muda quando o nome é alterado ou a ordem do código é reorganizada.
- Ao carregar, o JSON é validado antes de virar objeto.
- Exceções próprias, todas derivadas de `PersistenciaException`:

```text
PersistenciaException
├── SaveException            falha ao gravar
├── CarregamentoException    falha ao ler ou carregar
└── DadosInvalidosException  o arquivo foi lido, mas o conteúdo não é um save válido
```

---

## Registro de finais, conquistas e galeria

**Em desenvolvimento.**

Esses dados serão armazenados em um arquivo próprio, separado dos slots: o registro dos finais já alcançados e o conteúdo desbloqueável precisam sobreviver a sobrescrever um save e a iniciar uma nova partida.

Exemplo da apresentação de uma conquista:

```text
CONQUISTA DESBLOQUEADA 🏆

Primeiro Passo

Você sobreviveu ao primeiro semestre.
```

A galeria permitirá consultar o que já foi desbloqueado e o que ainda está bloqueado.

---

## Tecnologias

- **Java 21**
- **JLine** — leitura do teclado e controle do terminal
- **Gson** — serialização e persistência em JSON
- **Maven** — dependências, testes e geração do JAR executável
- **JUnit 5** — testes automatizados

---

## Estrutura do projeto

```text
src/
├── main/java/br/uefs/forkeazando/
│   ├── controller/    fluxo do jogo e dos menus
│   ├── excecao/       exceções de persistência
│   ├── model/         estado da partida, protagonista, cenas, escolhas
│   ├── persistencia/  slots, salvamento e configurações
│   ├── roteiro/       capítulos e cenas
│   └── view/          telas e entrada do teclado
└── test/java/br/uefs/forkeazando/
```

Os dados persistentes ficam na pasta `saves/`, separada do código.

---

## Fase 2

Legenda: `[x]` feito, `[*]` parcial, `[ ]` pendente.

* [x] Nova partida e continuação
* [x] Múltiplos slots
* [*] Metadados do save (falta o resumo da progressão)
* [x] Salvamento manual
* [ ] Salvamento automático
* [x] Carregamento do progresso
* [x] Sobrescrita com confirmação
* [ ] Exclusão com confirmação
* [x] Configurações persistentes
* [ ] Registro de finais
* [ ] Conteúdo desbloqueável / conquistas
* [x] Exceções personalizadas

### Limitações conhecidas

- O volume ainda não tem efeito (sem áudio).
- O salvar usa sempre o primeiro slot livre: uma partida carregada do slot 1 e salva de novo vai para o slot 2.
- O jogo precisa de um terminal real. Em console de IDE, as setas não funcionam.


## Versões

* `v1.0.0` — Fase 1
* `v1.1.0` — Fase 2, sessão 1 (salvamento em JSON e exceções)
* `v1.2.0` — Fase 2, sessão 2 (em andamento)
---

## Autora

**Stheffanny Nascimento Alves**

Projeto desenvolvido para a disciplina de **Algoritmos II — UEFS**.