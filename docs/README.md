
# Forkeazando 🎮

Um jogo narrativo desenvolvido em Java como projeto da disciplina de Algoritmos II da Universidade Estadual de Feira de Santana (UEFS).

Em **Forkeazando**, você acompanha a trajetória de uma estudante de Engenharia de Computação durante sua graduação, tomando decisões que influenciam seus atributos, relacionamentos, experiências e caminhos dentro da universidade.

---

## Como jogar

O jogo é executado pelo terminal e utiliza o **JLine** para permitir interação com o teclado em modo raw.

### Requisitos

- Java 21
- Maven 3.8+

### Execução

Para executar o jogo:

```bash
./rodar.sh
````

Caso necessário, dê permissão de execução ao script:

```bash
chmod +x rodar.sh
```

O script utiliza o Maven para compilar o projeto, resolver as dependências e gerar automaticamente o classpath necessário para executar o jogo.

> Recomenda-se executar pelo `rodar.sh` em vez do botão `Run` da IDE, principalmente por causa da captura das teclas direcionais utilizadas nas configurações.

### Testes

Para executar os testes automatizados:

```bash
mvn test
```

---

## Controles

### 🔢 Menu principal e cenas

No menu principal e durante as cenas, as opções são selecionadas utilizando as **teclas numéricas**:

```text
1
2
3
...
```

Digite o número correspondente à opção desejada e pressione `Enter`.

### Configurações

Nas configurações personalizáveis, a navegação é feita pelas teclas direcionais:

```text
↑ / ↓  → navegar entre as opções
← / →  → alterar valores
Enter   → confirmar
```

### Pausa

Durante a partida:

```text
P → Pausar / Continuar
```

O cabeçalho indica o estado atual:

```text
[Ctrl + P] Pausar
```

Quando a partida estiver pausada:

```text
[Ctrl + P] Continuar
```

Pressione `Ctrl +P`/`Ctrl + p` novamente para retomar.

---

## Configurações

O jogo possui configurações personalizáveis que são persistidas entre diferentes execuções.

As preferências são armazenadas em:

```text
config.json
```

Dessa forma, as configurações escolhidas pelo jogador permanecem salvas mesmo após o encerramento do jogo.

---

## Sistema de pausa

A partida pode ser pausada utilizando a tecla `P`, sem a necessidade de pressionar `Enter`.

O sistema utiliza a entrada do teclado em modo raw para detectar a tecla durante a execução da partida.

Enquanto estiver pausado, a progressão da partida permanece interrompida até que `P` seja pressionado novamente.

---

## Sistema de salvamento

O sistema de salvamento faz parte da Fase 2 do projeto.

O objetivo é permitir que o jogador interrompa uma partida e retome seu progresso posteriormente.

Os dados que deverão ser preservados incluem:

* cena atual;
* capítulo atual;
* atributos do jogador;
* relacionamentos;
* inventário;
* missões;
* decisões tomadas;
* flags e desbloqueios.

### Requisitos de salvamento

* [x] Nova partida e continuação
* [x] Múltiplos slots de salvamento
* [*] Metadados do save 
* [x] Salvamento manual
* [ ] Salvamento automático
* [ ] Carregamento do progresso
* [x] Confirmação antes de sobrescrever um save
* [ ] Exclusão de saves mediante confirmação

Os saves deverão apresentar informações como:

* nome do jogador;
* data e hora;
* capítulo ou cena atual;
* resumo da progressão.(Falta ser implementado)

---

## Conquistas e galeria

O jogo contará com um sistema de **conquistas e conteúdo desbloqueável**.

Quando determinadas condições forem atingidas durante uma partida, uma conquista poderá ser desbloqueada e apresentada ao jogador.

As conquistas serão armazenadas separadamente do progresso dos saves, permitindo que o jogador consulte posteriormente seu histórico através de uma galeria.

Exemplo:

```text
CONQUISTA DESBLOQUEADA 🏆 

Primeiro Passo

Você sobreviveu ao primeiro semestre.
```

A galeria permitirá consultar conquistas já desbloqueadas e conteúdos ainda bloqueados.

---

## 🛠️ Tecnologias

* **Java 21**
* **JLine** — interação com o terminal e captura de teclado
* **Gson** — serialização e persistência em JSON
* **Maven** — gerenciamento de dependências e execução do projeto
* **JUnit** — testes automatizados

---

## Estrutura do projeto

```text
src/
└── main/
    └── java/
        └── br/
            └── uefs/
                └── forkeazando/
                    ├── controller/
                    ├── model/
                    ├── roteiro/
                    ├── view/
                    └── ...
```

Os arquivos de dados persistentes são mantidos separadamente do código da aplicação.

Exemplo:

```text
config.json
```

Os arquivos referentes aos saves e à galeria serão adicionados ao sistema de persistência.

---

## Fase 2

A Fase 2 tem como foco a persistência do progresso, gerenciamento de partidas, configurações e conteúdo desbloqueável.

### Requisitos

* [x] Nova partida e continuação
* [x] Múltiplos slots
* [x] Metadados do save
* [x] Salvamento manual
* [ ] Salvamento automático
* [*] Carregamento de cena e progresso
* [x] Sobrescrita com confirmação
* [ ] Exclusão com confirmação
* [x] Configurações persistentes
* [ ] Registro de finais
* [*] Conteúdo desbloqueável / conquistas
* [x] Exceções personalizadas

---

## Autora

**Stheffanny Nascimento Alves**

Projeto desenvolvido para a disciplina de **Algoritmos II — UEFS**.

