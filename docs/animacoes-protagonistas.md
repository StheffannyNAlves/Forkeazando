# Animações de Tiana e Miles

Revisão de 22/11/2026, sobre a `main` do Forkeazando, commit [`4f06d186291f10a14c1ccbcda91ef73b91fcb6a8`](https://github.com/StheffannyNAlves/Forkeazando/commit/4f06d186291f10a14c1ccbcda91ef73b91fcb6a8).

## Resultado visual

As 44 ilustrações foram revisadas para os dois protagonistas. As 40 que mostram o jogador agora recebem sua identidade visual; quatro composições sem o jogador preservam o desenho comum. Os cenários continuam com 66 × 48 pixels, apresentados pelo JavaFX em 396 × 288, com escala inteira e suavização desativada.

| Característica | Tiana | Miles |
| --- | --- | --- |
| Cabelo | Comprido, castanho escuro, com mechas laterais | Curto, escuro, cacheado, com contorno irregular |
| Roupa cotidiana | Camiseta laranja e jeans azul | Jaqueta verde, camiseta clara e calça escura |
| Acessórios | Argolas douradas e alça clara | Alça de mochila e detalhes da jaqueta |
| Referência existente | `images/personagens/stheffanny/` | `images/personagens/protagonista_masculino/` |
| Identificador persistido | `protagonista_stheffanny` | `protagonista_masculino` |

As paletas e silhuetas seguem os retratos PNG já existentes. Os trajes de contexto, como beca e jaleco, mantêm a identidade pelo cabelo e pela pele. O nome livre digitado na criação continua independente da aparência.

Foram criados desenhos compartilhados de corpo inteiro, busto frontal, costas, perfil aproximado, mãos e cabeça no travesseiro. Incluem sombras da roupa, gola/camiseta, alças, cabelo, argolas, expressões e pequenos movimentos de respiração e piscada. A expressão cansada do RU e a expressão feliz da celebração permanecem ligadas ao contexto da cena.

## Sequências aprimoradas

| Sequência | Resultado |
| --- | --- |
| Entrevista e caderno | Mão e manga correspondem ao jogador; o caderno ganhou o gesto de escrever. |
| Matrícula | Perfil, cabelo, pele, roupa e reação à convocação próprios para cada protagonista; iluminação da tela conservada. |
| RU e Módulo 8 | O participante que representa o jogador usa a aparência escolhida. |
| Oficina e LED | Jogador incluído no primeiro plano da oficina; postura de costas consistente nas duas versões e luz do circuito preservada. |
| PBL e sala do grupo | Cadeira do jogador identificada; removida a antiga repetição da mesma protagonista em duas posições do PBL. |
| Ônibus | Plano subjetivo com manga, braço, dedo que escreve e palma que apaga depois da pausa de leitura. |
| Mural, assembleia, feira e prova | Jogador reconhecível no grupo; enquadramento da feira preserva a leitura da placa do estande. |
| Notas, editais e prazo | Silhueta e roupa variam com a sessão, conservando os resultados e os rótulos do cenário. |
| Greve, freela e quarto | Identidade consistente sob chuva, na mesa e no travesseiro, inclusive com o quarto escurecido. |
| RU do capítulo 6 | Busto frontal e expressão cansada próprios para cada identidade. |
| TCC, defesa e formatura | Presença do jogador nas composições e na turma; beca e arco dos gorros preservados. |
| Portão, três finais legados e cinco futuros | A aparência escolhida acompanha o encerramento e os epílogos. |
| Secretaria, extensão, auditório, ponto, casa e corredor | Jogador identificado, com roupa, cabelo e postura coerentes entre ambientes. |

`cap1_patio`, `cap4_agata`, `cap4_agata_resultado` e `memoria_bancada` apresentam outros personagens ou objetos. Esses ambientes continuam compartilhados entre as sessões. As 44 entradas e os 44 ciclos ambientais existentes permanecem disponíveis para cada perfil.

## Organização do código

| Componente | Responsabilidade |
| --- | --- |
| `view/pixel/PerfilProtagonista.java` | Seleciona Tiana ou Miles pelas flags de origem já persistidas e concentra as características visuais. |
| `view/pixel/ProtagonistaVisual.java` | Desenha as poses e os detalhes comuns; cada instância contém perfil e fase imutáveis de um quadro. |
| `view/pixel/Ilustracoes.java` | Registra as composições e recebe o perfil explicitamente em `animacao`, `ambiente` e `linhas`. |
| `view/pixel/Cenas*.java` | Compõe o ambiente e posiciona o jogador usando as mesmas primitivas de desenho. |
| `view/javafx/ArtesFx.java` | Prepara os quadros no executor de arte e os apresenta com os mecanismos de animação já existentes. |
| `view/javafx/ControllerInterface.java` | Obtém o perfil da partida atual e o encaminha ao renderizador, inclusive nos epílogos. |
| `view/CenaView.java` | Aplica a mesma seleção às ilustrações do terminal. |

A seleção dos recursos e o desenho pertencem à View. As decisões, consequências, roteamento e persistência continuam nas classes existentes. O modelo, os JSONs narrativos, o formato de save e as dependências Maven permanecem compatíveis. Os nomes internos `STHEFFANNY` e `DAVI` são conservados.

O desenho de cada quadro recebe seu próprio perfil. Cenários, NPCs e objetos mantêm suas paletas; a aparência do jogador é aplicada nas posições em que ele é desenhado. A API anterior de `Ilustracoes` continua disponível para consumidores antigos e usa Tiana como perfil padrão. As interfaces do jogo passam o perfil da sessão explicitamente.

### Cache e transições

O cache JavaFX de quadros usa o par **ilustração + perfil**, mantendo o limite de seis entradas. O cache ANSI também possui registros separados por perfil. Isso permite reutilizar o mesmo cenário entre decisões e impede que uma sessão com Miles receba quadros de Tiana.

Cada pedido de ilustração tem uma geração. Outro pedido ou `resetar()` cancela a preparação pendente e invalida os resultados anteriores. O retorno do executor para a thread JavaFX e a anexação do nó à cena conferem essa geração antes de exibir a imagem. Uma preparação antiga que terminou depois da troca de sessão permanece descartada.

Entrada e repouso continuam separados. No repouso, os acontecimentos ficam no último quadro da entrada; o cenário e a pose recebem somente movimento ambiental. A formatura mantém os gorros recuperados, o avião completa a decolagem, o processamento do ÁGATA permanece concluído e a memória da bancada conserva o LED apagado. A suspensão por modal e a interrupção ao retirar nós da tela continuam usando `AnimacoesFx`.

## Validação executada

`mvn clean verify` concluiu com **499 testes, zero falhas, zero erros e zero testes pulados**, usando Java 21, JavaFX 21.0.13 e Maven 3.9.9. Os testes gráficos foram executados em Xvfb com renderização de software.

| Verificação | Cobertura |
| --- | --- |
| Todas as ilustrações | Entradas das duas identidades, dimensões, arte final, repouso com movimento e conservação dos rótulos narrativos. |
| Personagens e cache | Aparência independente do nome, silhuetas e roupas, cache ANSI concorrente e alternância Tiana → Miles → Tiana → Miles no mesmo renderizador JavaFX. |
| Recursos de retrato | Seis expressões PNG de cada protagonista, sem suavização. |
| Trabalho pendente | Resultado da sessão anterior descartado mesmo se o nó só for anexado depois da troca. |
| Saves durante a história | 17 cenas de referência × 2 identidades, com comparação da arte antes/depois da leitura do arquivo. |
| Troca entre partidas | Novas sessões, decisão real, dois slots e carregamento após iniciar a sessão com a outra identidade. |
| Finais | Oito finais atuais/legados × 2 identidades, preservando conclusão, final identificado, epílogos e aparência depois da carga. |
| Percurso completo | O teste de IC foi ampliado para percorrer os oito capítulos com Tiana e Miles, respeitando a escolha específica do prólogo. |
| JavaFX existente | Digitação, pausa, retomada, remoção de nós, expressões, notificações e anexação tardia. |

Além da suíte, o aplicativo real foi aberto pelo FXML e exercitado com seus botões: criação dos dois protagonistas, escolhas iniciais até a matrícula, salvamento em slots distintos e carregamento cruzado. O ensaio conferiu 28 telas narrativas. As outras cenas desse ensaio foram abertas com estados de referência para inspecionar sua apresentação, incluindo os cinco futuros de ambas as identidades; o percurso narrativo completo foi validado pelos testes de integração.

Foram inspecionadas quatro pranchas com as 44 composições para ambos os protagonistas e capturas da interface. O vídeo de 38,4 segundos foi gerado pelos mesmos métodos de desenho do jogo, com entrada e repouso de oito sequências lado a lado. É uma prévia dos quadros, não uma gravação de uma partida completa.

### Desempenho e limites da validação

O preparo continua no executor de arte; a thread JavaFX recebe os pixels prontos e executa as animações. A medição local de 768 operações de desenho e rasterização usadas no vídeo ficou aproximadamente em **0,8 ms na mediana e 1,3 ms no percentil 95**, com os valores exatos no arquivo de evidência. São medidas do ambiente de teste, sem garantia de FPS para outro computador.

A validação gráfica usou Linux, Xvfb e renderização de software, com a interface em 1280 × 760. O comportamento em GPU física e em Windows/macOS não foi medido. O ambiente não ofereceu dispositivo de áudio compatível; a reprodução sonora não pôde ser validada nesse ensaio. Os recursos de áudio existentes foram preservados.

As ilustrações continuam no estilo pixel art procedural do projeto. Os novos recursos visuais são as classes de desenho, compartilhadas entre as cenas; os retratos PNG existentes são reaproveitados.

## Reproduzir os testes

No projeto com Java 21 e Maven:

```bash
mvn clean verify
mvn javafx:run
```

Em Linux sem sessão gráfica, com Xvfb instalado:

```bash
xvfb-run -a mvn clean verify
```

Sem um display, as classes que iniciam o toolkit gráfico abortam na condição inicial. Para reproduzir toda a cobertura informada, execute com display gráfico ou Xvfb.
