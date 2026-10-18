# Forkeazando 

**Entre o que era certo e o que ainda pode ser.**
<p align="center">
  <img
    src="/docs/images/menu-principal.png"
    alt="Menu principal de Forkeazando, jogo narrativo interativo em Java"
    width="750"
  />
</p>
Forkeazando é um jogo narrativo interativo desenvolvido em Java para a disciplina de **Algoritmos II**, da Universidade Estadual de Feira de Santana (UEFS).

A história acompanha uma estudante de Engenharia de Computação ao longo de sua trajetória universitária. Entre disciplinas, projetos, relacionamentos, oportunidades e exigências institucionais, cada escolha pode abrir caminhos, limitar possibilidades e produzir consequências que se refletem nos capítulos seguintes.

A universidade é mais do que o cenário da história: é um ambiente de aprendizado, convivência, competição, adaptação e burocracia. O jogador precisa descobrir como lidar com suas próprias características, administrar recursos limitados e construir uma trajetória em um sistema que nem sempre reconhece o esforço da maneira esperada.

O resultado é uma experiência narrativa em que **não basta escolher o que parece certo: é preciso lidar com as consequências do que foi escolhido.**

---

## Sobre o jogo

Forkeazando transforma a experiência universitária em uma narrativa interativa. A protagonista não chega à universidade como uma personagem sem história: sua formação anterior, sua situação econômica e suas características influenciam as condições iniciais da jornada.

O passado importa, mas não determina sozinho o futuro. As decisões tomadas durante a graduação também afetam os atributos da protagonista, suas relações com outros personagens, as possibilidades disponíveis e os desfechos que poderá alcançar.

Ao longo da história, o jogador precisa equilibrar diferentes dimensões da vida universitária:

- **Desempenho acadêmico:** desenvolver conhecimentos e demonstrar capacidade técnica.
- **Participação:** envolver-se nas discussões e nas atividades acadêmicas.
- **Energia:** administrar a disposição disponível para enfrentar as exigências da rotina.
- **Relacionamentos:** construir vínculos com colegas, veteranos, professores e outras pessoas da universidade.
- **Decisões e habilidades:** desenvolver características que podem abrir ou restringir opções futuras.
- **Adaptação institucional:** lidar com regras, procedimentos, prazos e exigências burocráticas.

Essas dimensões se relacionam. Uma escolha que favorece determinado aspecto da trajetória pode trazer custos em outro. O jogador precisa decidir quais oportunidades aproveitar, quais responsabilidades assumir e quais consequências está disposto a enfrentar.

## Uma trajetória moldada por escolhas

As escolhas não servem apenas para alterar o texto de uma cena. Elas podem modificar o estado da partida e influenciar as possibilidades narrativas posteriores.

O jogo utiliza atributos, relacionamentos e *flags* para representar características e decisões relevantes da jornada. Esses registros permitem que escolhas anteriores sejam consideradas em situações futuras.

Dependendo do estado da protagonista, determinadas alternativas podem estar disponíveis, enquanto outras podem exigir condições específicas ou permanecer inacessíveis.

O objetivo é fazer com que a trajetória construída pelo jogador tenha consequências ao longo da experiência, em vez de tratar cada cena como um episódio completamente isolado.

### A protagonista

A personagem principal é uma estudante de Engenharia de Computação cuja experiência universitária começa antes mesmo de sua primeira aula.

Sua situação econômica, sua experiência no ensino médio e suas características pessoais ajudam a definir seu ponto de partida. A partir daí, suas decisões e experiências constroem uma trajetória própria.

O jogo considera que estudantes diferentes podem chegar à universidade com recursos, expectativas e experiências distintas. Por isso, o contexto inicial faz parte da experiência narrativa, e não é apenas um detalhe de apresentação.

### A universidade como sistema

Em Forkeazando, as dificuldades não vêm somente das disciplinas ou das relações pessoais. Procedimentos, formulários, prazos, regras e comunicações institucionais também fazem parte da experiência.

A burocracia funciona como uma força recorrente na trajetória da protagonista: compreender as regras e aprender a navegar pelo sistema pode ser tão importante quanto desenvolver conhecimento técnico.

Essa dimensão dá ao jogo um conflito particular: a diferença entre aprender de verdade, participar da vida universitária e conseguir demonstrar isso dentro das estruturas que avaliam o estudante.

---

## 🎮 Sistemas de jogo

### Atributos e recursos

A protagonista possui recursos e características que influenciam a experiência de jogo.

- **Energia:** representa um recurso limitado, consumido por responsabilidades e atividades. Administrá-la é importante para decidir quais compromissos assumir.
- **Score:** representa o desempenho acumulado segundo as regras do jogo e pode participar das condições de progressão e dos desfechos.
- **Participação:** representa o envolvimento da protagonista nas atividades e discussões acadêmicas, podendo influenciar as possibilidades disponíveis.
- **Características e habilidades:** registram aspectos da protagonista que podem habilitar alternativas específicas.
- **Relacionamentos:** representam os vínculos construídos com outros personagens.
- **Flags de decisões:** guardam informações sobre acontecimentos e escolhas relevantes para a continuidade narrativa.

Os recursos não devem ser interpretados isoladamente. Uma decisão pode ser vantajosa para um atributo e, ao mesmo tempo, impor custos ou consequências em outras dimensões da vida universitária.

### Cenas e escolhas

A narrativa é organizada em uma abertura e capítulos compostos por cenas e alternativas de decisão.

Durante a experiência, o jogador lê os acontecimentos, avalia as opções disponíveis e escolhe como a protagonista reage às situações. As consequências podem afetar o estado da partida e as condições de acesso a acontecimentos posteriores.

As escolhas são processadas pelo sistema do jogo, que mantém os atributos, as decisões e os demais dados necessários para dar continuidade à narrativa.

### Finais e progressão

A trajetória pode conduzir a diferentes finais, de acordo com as condições e os acontecimentos registrados durante a partida.

O sistema de progressão permanente permite acompanhar os finais já alcançados, as conquistas desbloqueadas e os colecionáveis registrados. Esses dados ficam separados dos salvamentos individuais, possibilitando acompanhar a evolução do jogador entre diferentes jornadas.

---

## 🏆 Conquistas e colecionáveis

Forkeazando conta com sistemas de progressão que incentivam a exploração da experiência narrativa.

### Conquistas

As conquistas são desbloqueadas quando condições específicas são atendidas durante a jornada. O sistema registra os desbloqueios e permite consultar o progresso, incluindo conquistas ainda não obtidas.

Exemplo de apresentação:

```text
CONQUISTA DESBLOQUEADA 🏆

Primeiro Passo

Você sobreviveu ao primeiro semestre.
```

### Galeria de colecionáveis

O catálogo de colecionáveis permite acompanhar habilidades e itens adquiridos durante as partidas.

A galeria registra os conteúdos obtidos e ajuda o jogador a visualizar o que já descobriu ao longo de suas jornadas.

### Histórico de finais

Os finais alcançados são registrados com data e hora. Assim, a conclusão de uma partida contribui para o histórico de progressão sem depender da permanência daquele estado em um slot específico.

---

## 💾 Salvamento e continuidade

Forkeazando possui um sistema de persistência em JSON que permite continuar partidas, manter configurações e registrar progressos entre execuções.

### Partidas

O jogo oferece três slots independentes de salvamento, com suporte a:

- Criação de uma nova partida.
- Continuação da partida mantida em memória.
- Carregamento de uma partida salva.
- Salvamento manual.
- Salvamento automático durante a progressão.
- Confirmação antes de sobrescrever ou excluir um salvamento.
- Visualização de metadados, como capítulo, data e hora e informações da partida.

Os dados persistidos incluem o estado necessário para continuar a jornada, como cena atual, atributos, relacionamentos, inventário, missões, decisões e desbloqueios, conforme o modelo de estado do jogo.

### Configurações persistentes

As preferências do jogador são armazenadas separadamente dos slots de partida.

Entre as configurações disponíveis estão:

- Velocidade de exibição do texto.
- Volume.
- Confirmação de saída.
- Exibição de dicas de atalhos.

A opção de volume está persistida, mas ainda não controla áudio, pois o sistema de áudio não foi implementado.

### Progresso permanente

O arquivo `saves/progresso.json` mantém registros independentes dos slots de partida, incluindo finais, conquistas e colecionáveis.

Essa separação permite preservar o histórico de progressão mesmo quando uma partida é sobrescrita ou uma nova jornada é iniciada.

### Integridade dos dados

O sistema de persistência utiliza Gson para serialização e desserialização dos dados. Também contempla validação dos arquivos, exceções específicas para falhas de gravação e carregamento e o uso de arquivos temporários antes da substituição do destino.

As exceções personalizadas incluem:

```text
PersistenciaException
├── SaveException
├── CarregamentoException
└── DadosInvalidosException
```

---

## 🖥️ Interface e controles

Forkeazando é executado inteiramente no terminal, com navegação interativa por meio do JLine.

A interface reúne o menu principal, as cenas narrativas, os menus de configuração e salvamento e as telas de consulta de progresso.

### Controles

| Tecla | Ação |
|---|---|
| `↑` / `↓` | Navegar pelas opções |
| `←` / `→` | Alterar valores quando disponível |
| `Enter` | Confirmar uma seleção |
| `P` | Pausar a exibição do texto de uma cena |
| `C` | Retomar a exibição do texto pausado |
| `0` | Retornar ao menu principal nas cenas que oferecem essa opção |

A entrada por setas depende de um terminal compatível com JLine. Em cenas ou menus que utilizam entrada numérica, as opções devem ser selecionadas digitando o número correspondente.

### Terminal compatível

Para a navegação por teclado funcionar adequadamente, recomenda-se executar o jogo em um terminal real, como o Terminal do Linux ou macOS ou o Windows Terminal, em vez do console de execução da IDE.

---

## 🛠️ Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Implementação da lógica, dos modelos e da interface |
| JLine | Captura de teclas e interação com o terminal |
| Gson | Persistência e recuperação de dados em JSON |
| Maven | Gerenciamento de dependências e ciclo de compilação |
| JUnit 5 | Testes automatizados |

O projeto utiliza a arquitetura MVC para separar as responsabilidades entre modelos, controladores e visualizações. Essa organização ajuda a manter a lógica da narrativa, o fluxo de interação e a apresentação no terminal em componentes distintos.

---

## 📁 Estrutura do projeto

```text
src/
├── main/
│   ├── java/
│   │   └── br/uefs/forkeazando/
│   │       ├── controller/    # Fluxo do jogo e dos menus
│   │       ├── dao/           # Persistência de partidas e progresso
│   │       ├── excecao/       # Exceções personalizadas
│   │       ├── model/         # Entidades e estado do jogo
│   │       ├── resources/     # Recursos do jogo
│   │       ├── roteiro/       # Abertura, capítulos, cenas e escolhas
│   │       ├── view/          # Telas e entrada do teclado
│   │       └── Main.java      # Ponto de entrada
│   └── resources/             # Recursos da aplicação
└── test/
    └── java/
        └── br/uefs/forkeazando/
            ├── controller/
            ├── dao/
            └── model/
```

Os testes ficam em `src/test/java`. Os dados persistentes são armazenados em `saves/`, criada no diretório de execução do jogo.

---

## 🚀 Como executar

### Requisitos

- Java 21 ou superior.
- Maven 3.8 ou superior para execução a partir do código-fonte.
- Terminal compatível com JLine.

### Executar o jogo

Na raiz do repositório:

```bash
chmod +x rodar.sh
./rodar.sh
```

Se o arquivo JAR executável estiver disponível:

```bash
java -jar forkeazando.jar
```

### Executar os testes

```bash
mvn test
```

Os testes automatizados verificam componentes do projeto, incluindo a persistência e a validação dos dados de progressão.

---

## 📋 Fase 2

A segunda fase amplia o jogo com recursos de continuidade, persistência, progressão permanente e gerenciamento das partidas.

- [x] Criação e continuação de partidas
- [x] Três slots de salvamento
- [x] Metadados das partidas salvas
- [x] Salvamento manual
- [x] Salvamento automático
- [x] Carregamento de partidas
- [x] Confirmação antes de sobrescrever um salvamento
- [x] Confirmação antes de excluir um salvamento
- [x] Configurações persistentes
- [x] Registro permanente de finais
- [x] Conquistas e conteúdos desbloqueáveis
- [x] Persistência de colecionáveis e conquistas
- [x] Exceções personalizadas de persistência
- [x] Testes automatizados de persistência e validação do progresso

### Limitações conhecidas

- A configuração de volume ainda não tem efeito, pois o áudio não foi implementado.
- A escolha automática do slot de destino pode selecionar outro slot livre ao salvar uma partida carregada, em vez de atualizar necessariamente o slot de origem.
- O uso das setas depende de um terminal compatível com JLine.
- O caminho alternativo de gravação, usado quando não há suporte à movimentação atômica de arquivos, não possui cobertura específica nos testes automatizados.

---

## Versões

- `v1.0.0` — Fase 1.
- `v1.1.0` — Fase 2, sessão 1: salvamento em JSON e exceções personalizadas.
- `v1.2.0` — Fase 2, sessão 2: progressão, conquistas e colecionáveis.
- `1.3.0` - Fase 2, sessão 3

---

## Autoria

**Stheffanny Nascimento Alves**

Projeto desenvolvido para a disciplina de **Algoritmos II**, na Universidade Estadual de Feira de Santana (UEFS).
