# Forkeazando 🎮

Um jogo narrativo desenvolvido em Java como projeto da disciplina de Algoritmos II da Universidade Estadual de Feira de Santana (UEFS).

Em **Forkeazando**, você acompanha a trajetória de uma estudante de Engenharia de Computação durante sua graduação, tomando decisões que influenciam seus atributos, relacionamentos, experiências e caminhos dentro da universidade.

---

## Como jogar

O jogo é executado no **terminal** e utiliza o **JLine** para ler o teclado tecla a tecla (modo raw), permitindo navegar pelos menus com as setas.

### Requisitos

- Java 21 ou superior
- Maven 3.8+ (para compilar a partir do código-fonte)
- Um terminal real (Terminal do Linux ou macOS, Windows Terminal)

> **Não execute pelo console de execução da IDE.** Nele, as setas do teclado podem não funcionar corretamente, porque o JLine precisa de um terminal real.

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

Execute sempre a partir da pasta desejada para os dados: a pasta `saves/` é criada no diretório de execução do jogo.

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

Os menus de confirmação oferecem uma opção **Cancelar**.

### Pausa

Durante a exibição do texto de uma cena:

```text
P → pausar
C → continuar
```

Ao pausar, o jogo exibe `PAUSADO - [C] Continuar`. A pausa vale apenas enquanto o texto da cena é exibido, não nos menus.

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
├── progresso.json
├── slot1.json
├── slot2.json
└── slot3.json
```

Cada slot guarda o estado da partida, incluindo capítulo, cena atual, atributos, relacionamentos e flags que representam decisões, habilidades e itens.

- **Salvar:** permite salvar a partida pelo menu principal.
- **Carregar:** permite escolher um slot na lista, que apresenta informações da partida, como jogador, capítulo e data e hora.
- **Sobrescrita:** há confirmação antes de sobrescrever um slot.
- **Continuar:** retoma a partida em memória depois de voltar ao menu com `0`.

O arquivo `progresso.json` é separado dos slots e mantém os registros permanentes de progressão, como finais, conquistas e colecionáveis.

### Persistência

- Os dados são gravados em **JSON** usando o **Gson**.
- A gravação utiliza um arquivo temporário (`.tmp`) antes de substituir o arquivo de destino, reduzindo o risco de perder dados caso ocorra uma falha durante a escrita.
- Os relacionamentos são gravados pelo **código** do personagem, evitando depender do nome ou da ordem dos personagens.
- Ao carregar, o JSON é validado antes de ser convertido em objeto.
- O sistema utiliza exceções próprias, derivadas de `PersistenciaException`:

```text
PersistenciaException
├── SaveException            falha ao gravar
├── CarregamentoException    falha ao ler ou carregar
└── DadosInvalidosException  conteúdo lido não é um save válido
```

---

## Registro de finais, conquistas e galeria

O jogo possui um sistema de progressão permanente, armazenado em `saves/progresso.json`, separado dos slots de salvamento.

### Finais

Os finais alcançados são registrados com a data e a hora em que foram concluídos. Esse registro permanece independente dos saves de partida.

### Conquistas

As conquistas são desbloqueadas conforme o jogador alcança determinadas condições durante a jornada. O sistema registra a data e a hora do desbloqueio e salva o progresso.

Exemplo de apresentação:

```text
CONQUISTA DESBLOQUEADA 🏆

Primeiro Passo

Você sobreviveu ao primeiro semestre.
```

### Galeria de colecionáveis

A galeria registra habilidades e itens obtidos durante a partida. Os colecionáveis possuem um catálogo próprio, permitindo acompanhar o que já foi adquirido.

### Visualização do progresso

A tela de progresso apresenta os finais registrados e as conquistas, incluindo a porcentagem de conclusão, as descrições, as datas de desbloqueio e as conquistas ainda bloqueadas.

Como esses dados são mantidos separadamente dos slots, a progressão pode ser preservada mesmo quando uma partida é sobrescrita ou uma nova jornada é iniciada.

---

## Tecnologias

- **Java 21** — linguagem de programação
- **JLine** — leitura do teclado e controle do terminal
- **Gson** — serialização e persistência em JSON
- **Maven** — gerenciamento de dependências, testes e geração do JAR executável
- **JUnit 5** — testes automatizados

---

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/
│   │   └── br/uefs/forkeazando/
│   │       ├── controller/    fluxo do jogo e dos menus
│   │       ├── dao/           gerenciamento de saves e progresso
│   │       ├── excecao/       exceções personalizadas
│   │       ├── model/         entidades e estado do jogo
│   │       ├── resources/     recursos do jogo
│   │       ├── roteiro/       capítulos, cenas e escolhas
│   │       ├── view/          telas e entrada do teclado
│   │       └── Main.java      ponto de entrada da aplicação
│   └── resources/             recursos da aplicação
└── test/
    └── java/
        └── br/uefs/forkeazando/
            ├── controller/
            ├── dao/
            └── model/
```

Os testes automatizados ficam em `src/test/java`. Os dados persistentes, como saves, configurações e progresso, são armazenados na pasta `saves/`, criada no diretório de execução do jogo.
---

## Fase 2

Legenda: `[x]` feito, `[*]` parcial, `[ ]` pendente.

- [x] Nova partida e continuação
- [x] Múltiplos slots de salvamento
- [x] Metadados do save 
- [x] Salvamento manual
- [ ] Salvamento automático
- [x] Carregamento do progresso
- [x] Sobrescrita com confirmação
- [ ] Exclusão de save com confirmação
- [x] Configurações persistentes
- [x] Registro de finais
- [x] Conteúdo desbloqueável e conquistas
- [x] Persistência de colecionáveis e conquistas
- [x] Exceções personalizadas
- [x] Testes de persistência e validação do progresso

### Limitações conhecidas

- O volume ainda não tem efeito, pois o áudio não foi implementado.
- O salvamento utiliza o primeiro slot livre: uma partida carregada do slot 1 e salva novamente pode ocupar o slot 2.
- O jogo precisa de um terminal real; em consoles de IDE, as setas podem não funcionar.
- O caminho alternativo de gravação, usado quando o sistema de arquivos não suporta movimentação atômica, não é coberto por testes automatizados.

---

## Versões

- `v1.0.0` — Fase 1
- `v1.1.0` — Fase 2, sessão 1: salvamento em JSON e exceções
- `v1.2.0` — Fase 2, sessão 2: progressão, conquistas e colecionáveis

---

## Autora

**Stheffanny Nascimento Alves**

Projeto desenvolvido para a disciplina de **Algoritmos II — UEFS**.