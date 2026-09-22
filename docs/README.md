# Forkeazando

Jogo narrativo interativo em Java, desenvolvido para a disciplina EXA863 (MI - Programação) da Engenharia de Computação da UEFS.

Uma sátira sobre a trajetória de um estudante de Engenharia de Computação, do ingresso como calouro até a entrada na carreira profissional, onde escolhas ao longo do caminho — participar de iniciação científica, monitoria, ou seguir por conta própria — determinam o rumo profissional do protagonista.

## Integrantes

- Stheffanny Nascimento Alves
- Davi

## Status

Fase 1 concluída — jogo jogável de ponta a ponta: menu inicial, criação de personagem, 22 cenas do Capítulo 1 com ramificação real, três finais distintos e sistema de reputação com os personagens secundários.

## Funcionalidades da Fase 1

- Criação de personagem com atributos de origem (situação econômica, experiência emocional, vida social, entre outros)
- Sistema de recursos: Score, Participação e Energia (calculada a partir das características do protagonista)
- Escolhas com condição de acesso (score/participação mínimos, flags de decisões anteriores, energia disponível)
- Ramificação em três caminhos com finais distintos, um deles condicionado a desempenho acumulado
- Sistema de relacionamento numérico com personagens secundários, traduzido em categorias (Aliado, Colega, Rival) com feedback ao jogador
- Validação de entrada em todas as telas do jogo
- 21 testes de unidade cobrindo as classes do Model (`Protagonista`, `Escolha`, `Estado`, `Cena`, `PersonagemSecundario`, `CaracteristicaPersonagemSecundario`)

## Modelagem

Os diagramas de classes estão disponíveis em [`docs/diagrama-classes.png`](docs/Diagramas).

## Como executar

Projeto Maven. Na raiz do repositório:

```bash
mvn compile
java -cp target/classes br.uefs.forkeazando.Main
```

## Como rodar os testes

```bash
mvn test
```