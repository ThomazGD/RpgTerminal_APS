# RPG Terminal

Um jogo de RPG desenvolvido em Java para ser jogado diretamente pelo terminal.  
O projeto utiliza menus interativos, combate por turnos, criação de personagem, sistema de níveis, inventário, árvore de habilidades, missões, forja, materiais e artes em ASCII para deixar a experiência mais visual dentro do console.

---

## Sobre o projeto

O **RPG Terminal** é uma aventura em texto onde o jogador cria um personagem, escolhe uma classe e explora um mundo com inimigos, recompensas e evolução.

Durante o jogo, o jogador pode:

- Criar um personagem
- Escolher uma classe
- Explorar áreas
- Enfrentar inimigos
- Ganhar experiência
- Subir de nível
- Aprender habilidades
- Coletar materiais
- Usar poções
- Acessar o inventário
- Cumprir missões
- Criar itens na forja
- Descansar para recuperar vida e mana

O jogo roda totalmente no terminal e utiliza arte ASCII para representar personagens, inimigos e cenas de combate.

---

## Tecnologias utilizadas

- Java
- Programação Orientada a Objetos
- Terminal/Console
- ASCII Art
- Sistema de menus interativos
- Estruturas de dados como:
  - `ArrayList`
  - `HashMap`
  - `Enum`
  - Classes e objetos

---

## Funcionalidades

### Criação de personagem

O jogador pode criar seu personagem informando um nome e escolhendo uma classe.

Classes disponíveis:

- Guerreiro
- Mago
- Arqueiro

Cada classe possui atributos iniciais diferentes, como vida, ataque, defesa e mana.

---

### Classes do jogo

#### Guerreiro

Classe focada em força física, defesa e resistência.

Características:

- Mais vida
- Mais defesa
- Bom ataque físico
- Menos mana

Habilidades principais:

- Golpe Potente
- Investida Feroz
- Fúria Berserker
- Escudo Reforçado
- Postura Defensiva
- Fortaleza
- Grito de Guerra
- Estrategista

---

#### Mago

Classe focada em magia, mana e ataques elementais.

Características:

- Muita mana
- Menos vida
- Menos defesa
- Alto potencial mágico

Habilidades principais:

- Bola de Fogo
- Explosão Flamejante
- Meteoro
- Dardo de Gelo
- Congelar
- Tempestade de Gelo
- Mana Extra
- Barreira Mágica
- Domínio Arcano

---

#### Arqueiro

Classe focada em agilidade, precisão e ataques à distância.

Características:

- Vida equilibrada
- Ataque médio
- Boa agilidade
- Habilidades de precisão e esquiva

Habilidades principais:

- Precisão
- Tiro Certeiro
- Tiro Mortal
- Reflexos Rápidos
- Esquiva Perfeita
- Passo Ágil
- Flecha Roubada
- Aljava Infinita
- Mestre Arqueiro

---

## Sistema de combate

O combate acontece em turnos.

Durante o combate, o jogador pode escolher entre:

1. Atacar
2. Usar habilidade
3. Usar poção
4. Fugir

O jogo utiliza um sistema de rolagem de dados, incluindo o D21, para calcular falhas críticas, sucessos críticos e ataques especiais.

### Regras principais do dado

- Resultado 1: falha crítica
- Resultado 19 ou 20: sucesso crítico
- Resultado 21: crítico natural
- Crítico natural causa dano dobrado

---

## Inimigos

O jogo possui inimigos com níveis, vida, ataque, defesa e recompensas.

Inimigos disponíveis:

- Esqueleto
- Goblin
- Vampiro
- Dragão

Cada inimigo possui atributos próprios e pode aparecer durante a exploração.

---

## Sistema de experiência e nível

Ao derrotar inimigos ou completar missões, o personagem ganha experiência.

Quando a experiência atinge o limite necessário, o personagem sobe de nível.

Ao subir de nível:

- A vida máxima aumenta
- A mana máxima aumenta
- O ataque aumenta
- A defesa aumenta
- O personagem recebe ponto de habilidade

---

## Sistema de habilidades

Cada classe possui uma árvore de habilidades própria.

As habilidades são divididas por ramos.

Exemplos de ramos:

### Guerreiro

- Força
- Defesa
- Liderança

### Mago

- Fogo
- Gelo
- Arcano

### Arqueiro

- Furtivo
- Agilidade
- Roubo

Cada habilidade possui:

- Nome
- Descrição
- Tipo
- Ramo
- Nível requerido
- Pontos necessários
- Nível atual
- Nível máximo

Tipos de habilidade:

- Passiva
- Ativa
- Buff
- Debuff

---

## Sistema de inventário

O personagem possui um inventário com itens utilizáveis e equipamentos.

Tipos de itens:

- Armas
- Armaduras
- Poções

Exemplos:

- Espada de Ferro
- Cajado Básico
- Arco Longo
- Armadura de Couro
- Túnica de Mago
- Poção de Cura
- Poção de Mana

---

## Sistema de materiais

Durante o jogo, o personagem pode coletar materiais usados para criação de itens.

Materiais disponíveis:

- Couro
- Madeira
- Ferro
- Ouro
- Essência Mágica
- Escama de Dragão
- Cristal Arcano

Cada material possui nome, ícone, raridade e fonte.

---

## Sistema de forja

A forja permite criar novos itens usando materiais coletados.

Receitas disponíveis no projeto:

- Espada de Ferro
- Cajado Arcano
- Armadura de Ferro
- Poção de Cura Maior

Cada receita exige uma quantidade específica de materiais.

Exemplo:

```txt
Espada de Ferro
Materiais necessários:
- Ferro x3
- Madeira x1
