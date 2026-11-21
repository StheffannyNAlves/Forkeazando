# Forkeazando

<p align="center">
  <strong>Entre o que era certo e o que ainda pode ser.</strong>
</p>

<p align="center">
  Um jogo narrativo interativo sobre a vida universitária, as escolhas que fazemos e as consequências que carregamos.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/JavaFX-21-4B9CD3" alt="JavaFX 21">
  <img src="https://img.shields.io/badge/Maven-build-blue?logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white" alt="JUnit 5">
  <img src="https://img.shields.io/badge/Fase%203-JavaFX%20em%20desenvolvimento-7B4D99" alt="Fase 3 em desenvolvimento">
</p>

**Forkeazando** é um jogo narrativo interativo desenvolvido em Java para a disciplina **MI-Programação**, da Universidade Estadual de Feira de Santana (UEFS), seguindo a metodologia PBL (*Problem-Based Learning*).

A história acompanha estudantes de Engenharia de Computação em sua trajetória universitária. Entre disciplinas, projetos, relações pessoais, oportunidades e exigências institucionais, cada decisão pode abrir caminhos, restringir possibilidades e produzir consequências nos capítulos seguintes.

A universidade não é apenas o cenário: é um ambiente de aprendizado, convivência, competição, adaptação e burocracia. O jogador precisa administrar recursos, compreender suas próprias características e construir uma trajetória num sistema que nem sempre reconhece o esforço da maneira esperada.

> **Não basta escolher o que parece certo: é preciso lidar com as consequências do que foi escolhido.**

**Estado atual:** a **Fase 2** está implementada para terminal. A **Fase 3**, desenvolvida na branch [`fase3`](https://github.com/StheffannyNAlves/Forkeazando/tree/fase3), está migrando a experiência para **JavaFX**, mantendo o núcleo do jogo. Já há apresentação de cenas e escolhas, recursos visuais e animações; a integração completa dos menus, dos salvamentos e a validação de todas as rotas permanecem em andamento.

## Navegação

- [Sobre o jogo](#sobre-o-jogo)
- [Sistemas de jogo](#sistemas-de-jogo)
- [Conquistas, colecionáveis e finais](#conquistas-colecionáveis-e-finais)
- [Salvamento e continuidade](#salvamento-e-continuidade)
- [Interfaces e controles](#interfaces-e-controles)
- [Tecnologias e arquitetura](#tecnologias-e-arquitetura)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar](#como-executar)
- [Testes e qualidade](#testes-e-qualidade)
- [Evolução do projeto](#evolução-do-projeto)
- [Limitações conhecidas](#limitações-conhecidas)
- [Versões](#versões)
- [Autoria](#autoria)

---

## Sobre o jogo

O passado do protagonista importa, mas não determina sozinho seu futuro. Sua experiência escolar, situação econômica e características pessoais influenciam o início da jornada; as escolhas tomadas durante a graduação modificam atributos, vínculos, oportunidades e possíveis desfechos.

### Protagonistas

O projeto contempla **dois protagonistas selecionáveis, Stheffanny e Davi**, com perspectivas distintas sobre a universidade. O jogador pode personalizar o nome exibido. A lógica já possui identidades internas específicas; a padronização dos nomes visíveis na interface e em partes do roteiro ainda está em revisão.

A proposta não é simplesmente trocar a aparência do personagem: decisões, condições iniciais e variações narrativas devem permitir experiências diferentes sem perder a coerência da história compartilhada.

### Escolhas e consequências

As decisões não servem apenas para trocar de texto. Elas modificam o estado do jogo e podem repercutir em capítulos posteriores por meio de **atributos**, **relacionamentos** e **flags**. Algumas alternativas dependem de requisitos específicos; outras deixam de estar disponíveis conforme a trajetória construída.

### A universidade como sistema

Além dos desafios acadêmicos, a narrativa incorpora matrícula, editais, avaliações, formulários, comunicados, prazos e demais estruturas institucionais. Aprender a lidar com esses processos faz parte do conflito do jogo: nem sempre saber fazer algo significa conseguir demonstrar isso dentro das regras da instituição.

---

## Sistemas de jogo

| Sistema | Papel na jornada |
|---|---|
| **Energia** | Recurso limitado, gasto em atividades e responsabilidades; seu esgotamento pode desencadear um episódio específico (cenas 970–972). |
| **Score** | Representa o desempenho acumulado e participa de condições de progressão. |
| **Participação** | Representa o envolvimento em atividades e discussões acadêmicas. |
| **Características e habilidades** | Influenciam atributos e podem habilitar alternativas. |
| **Relacionamentos** | Registram vínculos com colegas, veteranos e professores. |
| **Flags** | Preservam acontecimentos e decisões que afetam cenas e desfechos futuros. |

### Narrativa e percursos

A estrutura inclui um prólogo, oito capítulos e caminhos acadêmicos e profissionais distintos, incluindo trajetórias ligadas à **iniciação científica**, ao **DTEC** e ao **trabalho independente**. O roteiro é carregado de arquivos JSON e processado pelas regras do jogo, independentemente da interface utilizada.

Na Fase 3, a revisão do roteiro procura reduzir cenas de transição redundantes, fortalecer vínculos de causa e consequência e diferenciar a perspectiva dos protagonistas. O formato **JSON v3** já está contemplado pelo carregador; a validação narrativa e a compatibilidade com salvamentos anteriores seguem como tarefas separadas.

---

## Conquistas, colecionáveis e finais

O sistema de progresso permanente mantém informações independentes dos slots de partida:

- **Conquistas:** desbloqueadas ao satisfazer condições específicas.
- **Colecionáveis:** habilidades e itens obtidos durante as jornadas, com catálogo de conteúdos conhecidos e bloqueados.
- **Histórico de finais:** registro dos desfechos alcançados, com informações de progresso.

Os recursos de consulta estão implementados na versão de terminal. A apresentação gráfica definitiva da galeria e do progresso ainda precisa ser integrada ao JavaFX.

---

## Salvamento e continuidade

A persistência utiliza **Gson e JSON**. A versão de terminal dispõe de três slots independentes, salvamento manual, autosave, confirmação de sobrescrita, exclusão de salvamentos, metadados e tratamento de arquivos corrompidos.

Arquivos utilizados:

| Arquivo | Finalidade |
|---|---|
| `saves/slot1.json`, `slot2.json`, `slot3.json` | Partidas salvas manualmente. |
| `saves/autosave.json` | Último salvamento automático. |
| `saves/progresso.json` | Progresso permanente. |
| `saves/configuracao.json` | Preferências do jogador. |

O autosave é realizado periodicamente (a cada cinco cenas concluídas) e não substitui o salvamento manual. A persistência utiliza validação de dados, exceções próprias e gravação com arquivos temporários antes da substituição do arquivo de destino.

**Situação no JavaFX:** a tela de carregamento já lista os três slots e oferece o autosave por meio de `AcessoPartidas` e `CarregamentoAdapter`. O fluxo gráfico de **salvar manualmente, escolher ou sobrescrever slots, excluir saves e manter o slot de origem da partida** ainda precisa ser concluído e testado. A conclusão de uma partida possui gravação no autosave, mas o fluxo completo de progresso e salvamento final requer validação.

---

## Interfaces e controles

### JavaFX — Fase 3

A interface gráfica utiliza **JavaFX, FXML e CSS**, com visual inspirado em jogos narrativos e pixel art. A implementação atual inclui:

- Menu principal, navegação por teclado e mouse e telas secundárias.
- Criação de partida, escolha do protagonista e nome personalizável.
- Exibição de cenas, narrativa e alternativas com processamento por eventos.
- Indicadores básicos de energia, score e participação.
- Tela gráfica de carregamento de slots e autosave.
- Ilustrações de cena convertidas de `Tela` para imagens JavaFX, com **animações de entrada** executadas por `Timeline`.
- Retratos em PNG, expressões e animações discretas de piscada e respiração do pacote gráfico, com fallback para os retratos existentes em pixel art.
- Modais para acontecimentos, mensagens, conquistas e transições.

A implementação visual ainda está sendo refinada. Não se deve interpretar a presença dos recursos como garantia de que todas as cenas, escolhas ou animações foram verificadas manualmente.

### Terminal — Fase 2

A primeira interface utiliza **JLine** e arte desenhada no próprio terminal em cores de 24 bits (*truecolor*). Ela inclui menus, HUD, retratos, ilustrações e animações em pixel art. O último quadro de cada animação também pode ser exibido como imagem estática.

<p align="center">
  <img src="docs/images/fork-versao1.4.0.gif" alt="Registro da interface de terminal do Forkeazando, versão 1.4.0" width="700">
</p>

<p align="center"><em>Registro histórico da interface original — versão 1.4.0 (Fase 2).</em></p>

| Tecla | Ação no terminal |
|---|---|
| `↑` / `↓` | Navegar pelas opções. |
| `←` / `→` | Alterar valores quando disponível. |
| `Enter` | Confirmar. |
| `P` / `C` | Pausar / retomar a exibição do texto. |
| `0` | Voltar ao menu quando disponível. |

Em terminais menores, retratos e ilustrações podem ser ocultados. Para desabilitar as animações da versão de terminal:

```bash
FORKEAZANDO_SEM_ANIMACAO=1 ./rodar.sh
```

Recomenda-se um terminal com suporte a *truecolor* e JLine, fora do console padrão de execução da IDE.

---

## Tecnologias e arquitetura

| Tecnologia | Utilização |
|---|---|
| Java 21 | Modelos, regras e controladores. |
| JavaFX 21 | Interface gráfica. |
| FXML e CSS | Estrutura e estilização das telas. |
| Scene Builder | Apoio à edição de FXML. |
| JLine | Interface interativa de terminal. |
| Gson | Leitura de roteiro e persistência JSON. |
| Maven | Build, dependências e execução. |
| JUnit 5 | Testes automatizados. |

O projeto adota **MVC (*Model–View–Controller*)**. Estado, regras de escolha, persistência e lógica narrativa são reutilizados pelas duas interfaces, sem a necessidade de reimplementar a mecânica dentro da apresentação gráfica.

Na Fase 3, `ControllerInterface` concentra eventos e elementos de apresentação JavaFX, `SessaoGrafica` coordena a sessão e `CenasController` processa as transições e consequências narrativas. `GerenciadorSlots` e as classes de persistência permanecem responsáveis pela gravação e leitura dos saves.

### Padrões de projeto

O projeto utiliza **Adapter** para expor operações existentes de carregamento à interface JavaFX (`AcessoPartidas` e `CarregamentoAdapter`). A disciplina prevê padrões GoF de categorias distintas; a conclusão e demonstração dos padrões criacional e comportamental ainda devem ser documentadas e verificadas, sem atribuir um padrão a uma classe apenas pela semelhança de nome.

---

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/br/uefs/forkeazando/
│   │   ├── adapter/        # Abstrações e adaptadores da persistência
│   │   ├── controller/     # Fluxo e regras de controle
│   │   ├── dao/            # Saves, configurações e progresso
│   │   ├── excecao/        # Exceções de domínio e persistência
│   │   ├── model/          # Estado, protagonistas, cenas e escolhas
│   │   ├── roteiro/        # Carregamento e resolução narrativa
│   │   ├── view/
│   │   │   ├── javafx/     # Interface gráfica e sessão
│   │   │   ├── pixel/      # Artes, quadros e componentes em pixel art
│   │   │   └── widget/     # Componentes de interação
│   │   └── Main.java      # Entrada da versão de terminal
│   └── resources/
│       ├── audio/          # Recursos de áudio
│       ├── fxml/           # Telas JavaFX
│       ├── images/         # Campus, personagens e animações em PNG
│       ├── roteiro/        # JSONs do prólogo, capítulos e variações
│       └── styles/        # Estilos CSS
└── test/java/br/uefs/forkeazando/
    ├── controller/
    ├── dao/
    ├── model/
    ├── roteiro/
    └── view/
```

Os dados persistentes são armazenados em `saves/`, relativo ao diretório de execução.

---

## Como executar

### Requisitos

- **JDK 21** (versão utilizada pelo projeto).
- **Maven 3.8+**.
- Ambiente gráfico para executar JavaFX.

### Interface gráfica JavaFX (em desenvolvimento)

```bash
mvn javafx:run
```

### Versão de terminal

```bash
chmod +x rodar.sh
./rodar.sh
```

### Compilação e testes

```bash
mvn clean test
mvn package
```

O `pom.xml` também configura um pacote JAR usando Maven Shade, cujo ponto de entrada é `br.uefs.forkeazando.Main` (versão de terminal):

```bash
java -jar target/forkeazando.jar
```

**Observação:** o JAR acima não é um instalador JavaFX; para testar a interface gráfica utilize `mvn javafx:run`.

---

## Testes e qualidade

O projeto possui testes JUnit para modelos, controladores, persistência, integridade de salvamentos, fluxo narrativo, roteamento e recursos de pixel art. Entre os testes de integração estão verificações de IDs únicos, escolhas que apontam para cenas existentes, ausência de cenas órfãs e percursos do roteiro.

Há também um workflow em `.github/workflows/tests.yml` que executa `mvn -B test` com **Java 21** no GitHub Actions.

> Em uma execução local relatada durante a revisão do roteiro, `mvn clean test` concluiu **441 testes**. Esse número é um registro histórico, não uma garantia sobre a versão atual. Execute novamente os testes após mudanças e verifique o resultado do CI.

Os testes automatizados de regras e roteiro não substituem os testes manuais da interface JavaFX, especialmente para animações, foco, rolagem, transições e persistência.

---

## Evolução do projeto

### Fase 2 — Terminal

A Fase 2 consolidou o núcleo da experiência: estados e consequências, três slots, autosave, configurações persistentes, conquistas, colecionáveis, finais e apresentação em pixel art no terminal. A demonstração acima permanece como registro dessa etapa.

### Fase 3 — JavaFX e revisão narrativa

A migração vai além da troca de interface. O roteiro está sendo revisto para melhorar **ritmo**, **continuidade**, **consequências** e **perspectivas dos protagonistas**. O campo `textoConsequencia` e o esquema JSON v3 fazem parte dessa evolução. O objetivo não é simplesmente reduzir cenas, mas retirar passagens mecânicas preservando decisões significativas.

#### Funcionalidades presentes no código

- [x] Estrutura JavaFX com Maven, FXML e CSS.
- [x] Menu e navegação básica.
- [x] Criação e continuação de partida em memória.
- [x] Exibição de cenas e processamento de escolhas por eventos.
- [x] Carregamento gráfico de slots e autosave.
- [x] Integração inicial da persistência com Adapter.
- [x] Carregamento dos roteiros JSON com suporte ao esquema v3.
- [x] Exibição de cenários com animações de entrada JavaFX.
- [x] Integração de retratos e animações discretas dos personagens.
- [x] Apresentação gráfica inicial de eventos e epílogos.

#### Pendências de implementação e validação

- [ ] Salvamento manual, sobrescrita e gerenciamento de slots no JavaFX.
- [ ] Integração completa de menus de configurações, galeria e progresso.
- [ ] Padronizar os nomes visíveis dos protagonistas e conferir as variações narrativas.
- [ ] Revisar cobertura visual e correspondência entre expressões e cenas.
- [ ] Aperfeiçoar HUD e organização dos diálogos e escolhas.
- [ ] Verificar percursos completos de todas as rotas e finais na interface gráfica.
- [ ] Validar persistência de progresso permanente e conclusão de partidas.
- [ ] Testar compatibilidade com saves de versões anteriores.
- [ ] Documentar e demonstrar os padrões GoF exigidos na Fase 3.
- [ ] Validar empacotamento e execução da interface fora do ambiente de desenvolvimento.

### Uso de inteligência artificial

A criação de recursos artísticos e animações contou com apoio de inteligência artificial generativa, conforme autorização comunicada pelo professor durante a sessão tutorial. A integração, revisão técnica, testes e validação dos recursos permanecem sob responsabilidade da autoria.

---

## Limitações conhecidas

- Nem todas as opções do menu principal estão integradas ao JavaFX.
- O salvamento manual e o gerenciamento gráfico dos três slots ainda estão pendentes.
- A integração das artes e animações existe, mas falta validar a cobertura visual de todas as cenas e os fluxos de transição.
- A progressão completa de todas as rotas e eventos especiais na interface gráfica ainda exige testes.
- Alterações de IDs e destinos do roteiro podem afetar arquivos de save de versões anteriores.
- Parte da interface gráfica ainda carece de testes automatizados de fluxo.
- O autosave mantém somente o registro mais recente.

---

## Versões

| Versão | Principais mudanças |
|---|---|
| `v1.0.0` | Fase 1. |
| `v1.1.0` | Fase 2, sessão 1: salvamento JSON e exceções. |
| `v1.2.0` | Fase 2, sessão 2: progressão, conquistas e colecionáveis. |
| `v1.3.0` | Fase 2, sessão 3: pixel art, retratos e esgotamento. |
| `v1.3.1` | Dicas de atalhos e documentação. |
| `v1.4.0` | Fase 2, sessão 4: autosave, slots e configurações. |

A **Fase 3** segue em desenvolvimento na branch `fase3`, sem uma nova versão estável declarada neste README.

---

## Autoria

**Stheffanny Nascimento Alves**

Projeto desenvolvido para a disciplina **MI-Programação**, na Universidade Estadual de Feira de Santana (UEFS).