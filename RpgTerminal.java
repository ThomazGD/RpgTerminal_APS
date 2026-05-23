import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Sistema de RPG Terminal - Um jogo de RPG baseado em texto com sistema de combate,
 * habilidades, itens, missões e forja.
 * 
 * @author RPG Terminal Team
 * @version 1.0
 */

// ========== SISTEMA DE DADOS ==========
/**
 * Classe utilitária para rolagem de dados e verificação de críticos.
 * Implementa um sistema de dados D21 (1-21) com mecânicas de crítico natural,
 * sucesso crítico e falha crítica.
 */
class SistemaDeDados {
    private static final Random random = new Random();
    
    /**
     * Rola um dado de 21 lados (D21).
     * @return valor entre 1 e 21
     */
    public static int rolarD21() {
        return random.nextInt(21) + 1; // 1 a 21
    }
    
    /**
     * Rola um dado de 20 lados (D20).
     * @return valor entre 1 e 20
     */
    public static int rolarD20() {
        return random.nextInt(20) + 1;
    }
    
    /**
     * Rola um dado de 12 lados (D12).
     * @return valor entre 1 e 12
     */
    public static int rolarD12() {
        return random.nextInt(12) + 1;
    }
    
    /**
     * Rola um dado de 10 lados (D10).
     * @return valor entre 1 e 10
     */
    public static int rolarD10() {
        return random.nextInt(10) + 1;
    }
    
    /**
     * Rola um dado de 8 lados (D8).
     * @return valor entre 1 e 8
     */
    public static int rolarD8() {
        return random.nextInt(8) + 1;
    }
    
    /**
     * Verifica se o resultado é um crítico natural (21 no D21).
     * Crítico natural causa dano dobrado.
     * @param resultado valor da rolagem do dado
     * @return true se for crítico natural
     */
    public static boolean isCriticoNatural(int resultado) {
        return resultado == 21; // Crítico natural no D21
    }
    
    /**
     * Verifica se o resultado é uma falha crítica (1 no D21).
     * Falha crítica faz o personagem perder o turno.
     * @param resultado valor da rolagem do dado
     * @return true se for falha crítica
     */
    public static boolean isFalhaCritica(int resultado) {
        return resultado == 1;
    }
    
    /**
     * Verifica se o resultado é um sucesso crítico (19-20 no D21).
     * Sucesso crítico causa +25% de dano.
     * @param resultado valor da rolagem do dado
     * @return true se for sucesso crítico
     */
    public static boolean isSucessoCritico(int resultado) {
        return resultado >= 19; // Sucesso crítico no D21 (19-20)
    }
}

// ========== SISTEMA DE HABILIDADES ==========
/**
 * Enumeração dos tipos de habilidades disponíveis no jogo.
 */
enum TipoHabilidade {
    /** Habilidade passiva - efeito constante sem ativação */
    PASSIVA, 
    /** Habilidade ativa - requer ativação manual pelo jogador */
    ATIVA, 
    /** Habilidade de buff - melhora atributos temporariamente */
    BUFF, 
    /** Habilidade de debuff - piora atributos do inimigo */
    DEBUFF
}

/**
 * Enumeração dos ramos de habilidades organizados por classe.
 * Cada classe tem 3 ramos exclusivos de habilidades.
 */
enum RamoHabilidade {
    // Ramo do Guerreiro
    /** Foco em dano físico e força bruta */
    FORCA, 
    /** Foco em defesa e sobrevivência */
    DEFESA, 
    /** Foco em liderança e combate em grupo */
    LIDERANCA,  
    // Ramo do Mago
    /** Habilidades de fogo e dano em área */
    FOGO, 
    /** Habilidades de gelo e controle */
    GELO, 
    /** Habilidades arcanas e mana */
    ARCANO,        
    // Ramo do Arqueiro
    /** Habilidades furtivas e precisão */
    FURTIVO, 
    /** Habilidades de agilidade e esquiva */
    AGILIDADE, 
    /** Habilidades de roubo e recursos */
    ROUBO
}

/**
 * Representa uma habilidade que pode ser aprendida por um personagem.
 * Cada habilidade pertence a um ramo específico e tem requisitos de nível e pontos.
 */
class Habilidade {
    protected String nome;
    protected String descricao;
    protected TipoHabilidade tipo;
    protected RamoHabilidade ramo;
    protected int nivelRequerido;
    protected int pontosNecessarios;
    protected int nivelAtual;
    protected int nivelMaximo;
    
    /**
     * Construtor da classe Habilidade.
     * @param nome Nome da habilidade
     * @param descricao Descrição do efeito da habilidade
     * @param tipo Tipo da habilidade (PASSIVA, ATIVA, BUFF, DEBUFF)
     * @param ramo Ramo de habilidades a que pertence
     * @param nivelRequerido Nível mínimo do personagem para aprender
     * @param pontosNecessarios Pontos de habilidade necessários
     * @param nivelMaximo Nível máximo que a habilidade pode alcançar
     */
    public Habilidade(String nome, String descricao, TipoHabilidade tipo, RamoHabilidade ramo, 
                     int nivelRequerido, int pontosNecessarios, int nivelMaximo) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.ramo = ramo;
        this.nivelRequerido = nivelRequerido;
        this.pontosNecessarios = pontosNecessarios;
        this.nivelAtual = 0;
        this.nivelMaximo = nivelMaximo;
    }
    
    /**
     * Verifica se o personagem pode aprender esta habilidade.
     * @param nivelPersonagem Nível atual do personagem
     * @param pontosDisponiveis Pontos de habilidade disponíveis
     * @return true se o personagem pode aprender a habilidade
     */
    public boolean podeAprender(int nivelPersonagem, int pontosDisponiveis) {
        return nivelPersonagem >= nivelRequerido && 
               pontosDisponiveis >= pontosNecessarios && 
               nivelAtual < nivelMaximo;
    }
    
    /**
     * Aumenta o nível atual da habilidade em 1.
     */
    public void aprender() {
        if (nivelAtual < nivelMaximo) {
            nivelAtual++;
        }
    }
    
    /**
     * Retorna a descrição completa da habilidade incluindo nível e requisitos.
     * @return String formatada com todas as informações da habilidade
     */
    public String getDescricaoCompleta() {
        return String.format("%s (Nível %d/%d)\n%s\nRequer: Nível %d, %d pontos", 
                nome, nivelAtual, nivelMaximo, descricao, nivelRequerido, pontosNecessarios);
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public TipoHabilidade getTipo() { return tipo; }
    public RamoHabilidade getRamo() { return ramo; }
    public int getNivelAtual() { return nivelAtual; }
    public int getNivelMaximo() { return nivelMaximo; }
    public int getPontosNecessarios() { return pontosNecessarios; }
}

/**
 * Gerencia a árvore de habilidades de um personagem.
 * Cada classe tem ramos exclusivos de habilidades que podem ser desbloqueados.
 */
class ArvoreHabilidades {
    private Map<RamoHabilidade, List<Habilidade>> ramos;
    private int pontosDisponiveis;
    private int pontosGastos;
    
    /**
     * Construtor da árvore de habilidades.
     * @param classe Classe do personagem (guerreiro, mago, arqueiro)
     */
    public ArvoreHabilidades(String classe) {
        this.ramos = new HashMap<>();
        this.pontosDisponiveis = 0;
        this.pontosGastos = 0;
        inicializarRamos(classe);
    }
    
    /**
     * Inicializa os ramos de habilidades baseados na classe do personagem.
     * @param classe Classe do personagem
     */
    private void inicializarRamos(String classe) {
        switch (classe.toLowerCase()) {
            case "guerreiro":
                inicializarRamosGuerreiro();
                break;
            case "mago":
                inicializarRamosMago();
                break;
            case "arqueiro":
                inicializarRamosArqueiro();
                break;
        }
    }
    
    /**
     * Inicializa os ramos de habilidades do Guerreiro.
     * Ramos: Força, Defesa, Liderança
     */
    private void inicializarRamosGuerreiro() {
        // Ramo Força
        List<Habilidade> forca = new ArrayList<>();
        forca.add(new Habilidade("Golpe Potente", "Aumenta o dano do ataque básico em 15%", 
            TipoHabilidade.PASSIVA, RamoHabilidade.FORCA, 1, 1, 5));
        forca.add(new Habilidade("Investida Feroz", "Causa dano em área e atordoa inimigos", 
            TipoHabilidade.ATIVA, RamoHabilidade.FORCA, 3, 2, 3));
        forca.add(new Habilidade("Fúria Berserker", "Aumenta ataque em 50% mas diminui defesa", 
            TipoHabilidade.BUFF, RamoHabilidade.FORCA, 5, 3, 1));
        
        // Ramo Defesa
        List<Habilidade> defesa = new ArrayList<>();
        defesa.add(new Habilidade("Escudo Reforçado", "Aumenta a defesa em 20%", 
            TipoHabilidade.PASSIVA, RamoHabilidade.DEFESA, 1, 1, 5));
        defesa.add(new Habilidade("Postura Defensiva", "Reduz dano recebido em 30%", 
            TipoHabilidade.BUFF, RamoHabilidade.DEFESA, 3, 2, 3));
        defesa.add(new Habilidade("Fortaleza", "Imunidade a dano por 2 turnos", 
            TipoHabilidade.ATIVA, RamoHabilidade.DEFESA, 7, 4, 1));
        
        // Ramo Liderança
        List<Habilidade> lideranca = new ArrayList<>();
        lideranca.add(new Habilidade("Grito de Guerra", "Aumenta moral dos aliados", 
            TipoHabilidade.ATIVA, RamoHabilidade.LIDERANCA, 2, 1, 3));
        lideranca.add(new Habilidade("Estrategista", "Bônus em combate contra múltiplos inimigos", 
            TipoHabilidade.PASSIVA, RamoHabilidade.LIDERANCA, 4, 2, 3));
        
        ramos.put(RamoHabilidade.FORCA, forca);
        ramos.put(RamoHabilidade.DEFESA, defesa);
        ramos.put(RamoHabilidade.LIDERANCA, lideranca);
    }
    
    /**
     * Inicializa os ramos de habilidades do Mago.
     * Ramos: Fogo, Gelo, Arcano
     */
    private void inicializarRamosMago() {
        // Ramo Fogo
        List<Habilidade> fogo = new ArrayList<>();
        fogo.add(new Habilidade("Bola de Fogo", "Lança uma bola de fogo causando dano", 
            TipoHabilidade.ATIVA, RamoHabilidade.FOGO, 1, 1, 5));
        fogo.add(new Habilidade("Explosão Flamejante", "Causa dano em área", 
            TipoHabilidade.ATIVA, RamoHabilidade.FOGO, 3, 2, 3));
        fogo.add(new Habilidade("Meteoro", "Chuva de meteoros devastadores", 
            TipoHabilidade.ATIVA, RamoHabilidade.FOGO, 7, 4, 1));
        
        // Ramo Gelo
        List<Habilidade> gelo = new ArrayList<>();
        gelo.add(new Habilidade("Dardo de Gelo", "Lança dardo de gelo", 
            TipoHabilidade.ATIVA, RamoHabilidade.GELO, 1, 1, 5));
        gelo.add(new Habilidade("Congelar", "Impede inimigo de agir", 
            TipoHabilidade.DEBUFF, RamoHabilidade.GELO, 3, 2, 3));
        gelo.add(new Habilidade("Tempestade de Gelo", "Causa dano e congela todos", 
            TipoHabilidade.ATIVA, RamoHabilidade.GELO, 7, 4, 1));
        
        // Ramo Arcano
        List<Habilidade> arcano = new ArrayList<>();
        arcano.add(new Habilidade("Mana Extra", "Aumenta mana máxima", 
            TipoHabilidade.PASSIVA, RamoHabilidade.ARCANO, 1, 1, 5));
        arcano.add(new Habilidade("Barreira Mágica", "Cria escudo mágico", 
            TipoHabilidade.ATIVA, RamoHabilidade.ARCANO, 3, 2, 3));
        arcano.add(new Habilidade("Domínio Arcano", "Poder mágico aumentado", 
            TipoHabilidade.PASSIVA, RamoHabilidade.ARCANO, 5, 3, 1));
        
        ramos.put(RamoHabilidade.FOGO, fogo);
        ramos.put(RamoHabilidade.GELO, gelo);
        ramos.put(RamoHabilidade.ARCANO, arcano);
    }
    
    /**
     * Inicializa os ramos de habilidades do Arqueiro.
     * Ramos: Furtivo, Agilidade, Roubo
     */
    private void inicializarRamosArqueiro() {
        // Ramo Furtivo
        List<Habilidade> furtivo = new ArrayList<>();
        furtivo.add(new Habilidade("Precisão", "Aumenta a chance de acerto crítico", 
            TipoHabilidade.PASSIVA, RamoHabilidade.FURTIVO, 1, 3, 5));
        furtivo.add(new Habilidade("Tiro Certeiro", "Ataque com alta precisão", 
            TipoHabilidade.ATIVA, RamoHabilidade.FURTIVO, 3, 5, 8));
        furtivo.add(new Habilidade("Tiro Mortal", "Ataque devastador à distância", 
            TipoHabilidade.ATIVA, RamoHabilidade.FURTIVO, 5, 8, 12));
        
        // Ramo Agilidade
        List<Habilidade> agilidade = new ArrayList<>();
        agilidade.add(new Habilidade("Reflexos Rápidos", "Aumenta a esquiva e defesa", 
            TipoHabilidade.PASSIVA, RamoHabilidade.AGILIDADE, 1, 3, 5));
        agilidade.add(new Habilidade("Esquiva Perfeita", "Chance de esquivar ataques", 
            TipoHabilidade.PASSIVA, RamoHabilidade.AGILIDADE, 3, 5, 8));
        agilidade.add(new Habilidade("Passo Ágil", "Move-se rapidamente pelo campo", 
            TipoHabilidade.ATIVA, RamoHabilidade.AGILIDADE, 5, 8, 12));
        
        // Ramo Roubo
        List<Habilidade> roubo = new ArrayList<>();
        roubo.add(new Habilidade("Flecha Roubada", "Recupera flechas após o combate", 
            TipoHabilidade.ATIVA, RamoHabilidade.ROUBO, 1, 3, 5));
        roubo.add(new Habilidade("Aljava Infinita", "Aumenta capacidade de munição", 
            TipoHabilidade.PASSIVA, RamoHabilidade.ROUBO, 3, 5, 8));
        roubo.add(new Habilidade("Mestre Arqueiro", "Chance de ataque duplo", 
            TipoHabilidade.PASSIVA, RamoHabilidade.ROUBO, 5, 8, 12));
        
        ramos.put(RamoHabilidade.FURTIVO, furtivo);
        ramos.put(RamoHabilidade.AGILIDADE, agilidade);
        ramos.put(RamoHabilidade.ROUBO, roubo);
    }
    
    /**
     * Adiciona pontos de habilidade disponíveis.
     * @param pontos Quantidade de pontos a adicionar
     */
    public void adicionarPontos(int pontos) {
        pontosDisponiveis += pontos;
    }
    
    /**
     * Tenta aprender uma habilidade específica.
     * @param ramo Ramo da habilidade
     * @param indice Índice da habilidade no ramo
     * @param nivelPersonagem Nível atual do personagem
     * @return true se a habilidade foi aprendida com sucesso
     */
    public boolean aprenderHabilidade(RamoHabilidade ramo, int indice, int nivelPersonagem) {
        List<Habilidade> habilidades = ramos.get(ramo);
        if (habilidades != null && indice >= 0 && indice < habilidades.size()) {
            Habilidade habilidade = habilidades.get(indice);
            if (habilidade.podeAprender(nivelPersonagem, pontosDisponiveis)) {
                habilidade.aprender();
                pontosDisponiveis -= habilidade.getPontosNecessarios();
                pontosGastos += habilidade.getPontosNecessarios();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retorna uma descrição formatada de todos os ramos de habilidades.
     * @return String com a árvore de habilidades formatada
     */
    public String getDescricaoRamos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ÁRVORE DE HABILIDADES ===\n");
        sb.append("Pontos disponíveis: ").append(pontosDisponiveis).append("\n\n");
        
        for (Map.Entry<RamoHabilidade, List<Habilidade>> entry : ramos.entrySet()) {
            sb.append("├── ").append(entry.getKey()).append("\n");
            for (int i = 0; i < entry.getValue().size(); i++) {
                Habilidade hab = entry.getValue().get(i);
                sb.append("│   ├── ").append(i + 1).append(". ").append(hab.getNome())
                  .append(" (").append(hab.getNivelAtual()).append("/").append(hab.getNivelMaximo()).append(")\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Retorna lista de habilidades aprendidas (nível > 0).
     * @return Lista de habilidades aprendidas
     */
    public List<Habilidade> getHabilidadesAprendidas() {
        List<Habilidade> aprendidas = new ArrayList<>();
        for (List<Habilidade> habilidades : ramos.values()) {
            for (Habilidade hab : habilidades) {
                if (hab.getNivelAtual() > 0) {
                    aprendidas.add(hab);
                }
            }
        }
        return aprendidas;
    }
    
    // Getters
    public int getPontosDisponiveis() { return pontosDisponiveis; }
    public int getPontosGastos() { return pontosGastos; }
}

// ========== SISTEMA DE ITENS ==========
/**
 * Enumeração das raridades de itens no jogo.
 * Cada raridade tem um multiplicador de poder.
 */
enum RaridadeItem {
    /** Itens comuns - multiplicador 1x */
    COMUM("Comum", "Branco", 1),
    /** Itens raros - multiplicador 2x */
    RARO("Raro", "Azul", 2),
    /** Itens épicos - multiplicador 3x */
    ÉPICO("Épico", "Roxo", 3),
    /** Itens lendários - multiplicador 4x */
    LENDÁRIO("Lendário", "Laranja", 4);
    
    private String nome;
    private String cor;
    private int multiplicador;
    
    /**
     * Construtor do enum de raridade.
     * @param nome Nome da raridade
     * @param cor Cor associada à raridade
     * @param multiplicador Multiplicador de poder
     */
    RaridadeItem(String nome, String cor, int multiplicador) {
        this.nome = nome;
        this.cor = cor;
        this.multiplicador = multiplicador;
    }
    
    public String getNome() { return nome; }
    public String getCor() { return cor; }
    public int getMultiplicador() { return multiplicador; }
}

/**
 * Enumeração dos tipos de materiais de craft.
 * Cada material tem uma raridade e uma fonte onde pode ser encontrado.
 */
enum TipoMaterial {
    COURO("Couro", "🐄", 1, "Animais"),
    MADEIRA("Madeira", "🪵", 1, "Árvores"),
    FERRO("Ferro", "⚙️", 2, "Minas"),
    OURO("Ouro", "🪙", 3, "Minas raras"),
    ESSÊNCIA_MÁGICA("Essência Mágica", "✨", 4, "Inimigos mágicos"),
    ESCAMA_DRAGÃO("Escama de Dragão", "🐉", 5, "Chefes"),
    CRISTAL_ARCANO("Cristal Arcano", "💎", 6, "Masmorras finais");
    
    private String nome;
    private String icone;
    private int raridade;
    private String fonte;
    
    /**
     * Construtor do enum de material.
     * @param nome Nome do material
     * @param icone Ícone representativo
     * @param raridade Nível de raridade (1-6)
     * @param fonte Fonte onde o material pode ser encontrado
     */
    TipoMaterial(String nome, String icone, int raridade, String fonte) {
        this.nome = nome;
        this.icone = icone;
        this.raridade = raridade;
        this.fonte = fonte;
    }
    
    public String getNome() { return nome; }
    public String getIcone() { return icone; }
    public int getRaridade() { return raridade; }
    public String getFonte() { return fonte; }
}

/**
 * Representa um material de craft com quantidade.
 */
class Material {
    private TipoMaterial tipo;
    private int quantidade;
    
    /**
     * Construtor da classe Material.
     * @param tipo Tipo do material
     * @param quantidade Quantidade inicial
     */
    public Material(TipoMaterial tipo, int quantidade) {
        this.tipo = tipo;
        this.quantidade = quantidade;
    }
    
    /**
     * Adiciona quantidade ao material.
     * @param quantidade Quantidade a adicionar
     */
    public void adicionar(int quantidade) {
        this.quantidade += quantidade;
    }
    
    /**
     * Remove quantidade do material se disponível.
     * @param quantidade Quantidade a remover
     * @return true se foi possível remover
     */
    public boolean remover(int quantidade) {
        if (this.quantidade >= quantidade) {
            this.quantidade -= quantidade;
            return true;
        }
        return false;
    }
    
    // Getters
    public TipoMaterial getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
}

/**
 * Classe base para todos os itens do jogo.
 */
class Item {
    protected String nome;
    protected String descricao;
    protected RaridadeItem raridade;
    protected int nivel;
    protected String icone;
    
    /**
     * Construtor da classe Item.
     * @param nome Nome do item
     * @param descricao Descrição do item
     * @param raridade Raridade do item
     * @param nivel Nível do item
     * @param icone Ícone representativo
     */
    public Item(String nome, String descricao, RaridadeItem raridade, int nivel, String icone) {
        this.nome = nome;
        this.descricao = descricao;
        this.raridade = raridade;
        this.nivel = nivel;
        this.icone = icone;
    }
    
    /**
     * Retorna a descrição completa formatada do item.
     * @return String com informações do item
     */
    public String getDescricaoCompleta() {
        return String.format("%s %s (Nível %d)\n%s", icone, nome, nivel, descricao);
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public RaridadeItem getRaridade() { return raridade; }
    public int getNivel() { return nivel; }
    public String getIcone() { return icone; }
}

/**
 * Representa uma arma equipável que aumenta o ataque do personagem.
 */
class Arma extends Item {
    private int dano;
    private TipoHabilidade tipoHabilidade;
    
    /**
     * Construtor da classe Arma.
     * @param nome Nome da arma
     * @param descricao Descrição da arma
     * @param raridade Raridade da arma
     * @param nivel Nível da arma
     * @param dano Dano base da arma
     * @param tipoHabilidade Tipo de habilidade associada
     * @param icone Ícone representativo
     */
    public Arma(String nome, String descricao, RaridadeItem raridade, int nivel, 
                int dano, TipoHabilidade tipoHabilidade, String icone) {
        super(nome, descricao, raridade, nivel, icone);
        this.dano = dano;
        this.tipoHabilidade = tipoHabilidade;
    }
    
    @Override
    public String getDescricaoCompleta() {
        return String.format("%s %s (Nível %d)\n%s\n⚔️ Dano: %d | Tipo: %s", 
                icone, nome, nivel, descricao, dano, tipoHabilidade);
    }
    
    // Getters
    public int getDano() { return dano; }
    public TipoHabilidade getTipoHabilidade() { return tipoHabilidade; }
}

/**
 * Representa uma armadura equipável que aumenta a defesa do personagem.
 */
class Armadura extends Item {
    private int defesa;
    private String tipo;
    
    /**
     * Construtor da classe Armadura.
     * @param nome Nome da armadura
     * @param descricao Descrição da armadura
     * @param raridade Raridade da armadura
     * @param nivel Nível da armadura
     * @param defesa Defesa fornecida
     * @param tipo Tipo de peça (Peito, etc.)
     * @param icone Ícone representativo
     */
    public Armadura(String nome, String descricao, RaridadeItem raridade, int nivel,
                    int defesa, String tipo, String icone) {
        super(nome, descricao, raridade, nivel, icone);
        this.defesa = defesa;
        this.tipo = tipo;
    }
    
    @Override
    public String getDescricaoCompleta() {
        return String.format("%s %s (Nível %d)\n%s\n🛡️ Defesa: %d | Tipo: %s", 
                icone, nome, nivel, descricao, defesa, tipo);
    }
    
    // Getters
    public int getDefesa() { return defesa; }
    public String getTipo() { return tipo; }
}

/**
 * Representa uma poção consumível que restaura HP ou Mana.
 */
class Pocao extends Item {
    private int quantidade;
    private String tipoEfeito;
    private int valorEfeito;
    
    /**
     * Construtor da classe Poção.
     * @param nome Nome da poção
     * @param descricao Descrição da poção
     * @param quantidade Quantidade de poções
     * @param tipoEfeito Tipo de efeito (Vida, Mana)
     * @param valorEfeito Valor do efeito
     */
    public Pocao(String nome, String descricao, int quantidade, String tipoEfeito, int valorEfeito) {
        super(nome, descricao, RaridadeItem.COMUM, 1, "🧪");
        this.quantidade = quantidade;
        this.tipoEfeito = tipoEfeito;
        this.valorEfeito = valorEfeito;
    }
    
    @Override
    public String getDescricaoCompleta() {
        return String.format("%s %s (x%d)\n%s\n💊 %s: +%d", 
                icone, nome, quantidade, descricao, tipoEfeito, valorEfeito);
    }
    
    // Getters
    public int getQuantidade() { return quantidade; }
    public String getTipoEfeito() { return tipoEfeito; }
    public int getValorEfeito() { return valorEfeito; }
}

// ========== SISTEMA DE PERSONAGEM ==========
/**
 * Representa o personagem do jogador com todos os seus atributos,
 * equipamentos, inventário e habilidades.
 */
class Personagem {
    protected String nome;
    protected String classe;
    protected int nivel;
    protected int experiencia;
    protected int experienciaMaxima;
    protected int vida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defesa;
    protected int mana;
    protected int manaMaxima;
    
    // Equipamentos
    private Arma armaEquipada;
    private Armadura armaduraEquipada;
    
    // Inventário
    private List<Item> inventario;
    private Map<TipoMaterial, Integer> materiais;
    
    // Habilidades
    private ArvoreHabilidades arvoreHabilidades;
    
    /**
     * Construtor da classe Personagem.
     * Inicializa atributos baseados na classe escolhida.
     * @param nome Nome do personagem
     * @param classe Classe do personagem (guerreiro, mago, arqueiro)
     */
    public Personagem(String nome, String classe) {
        this.nome = nome;
        this.classe = classe;
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaMaxima = 100;
        
        // Atributos base por classe
        switch (classe.toLowerCase()) {
            case "guerreiro":
                // Guerreiro: Alto HP e ataque, mana baixa
                this.vidaMaxima = 160;
                this.vida = 160;
                this.ataque = 18;
                this.defesa = 12;
                this.manaMaxima = 60;
                this.mana = 60;
                break;
            case "mago":
                // Mago: Baixo HP e defesa, mana muito alta
                this.vidaMaxima = 85;
                this.vida = 85;
                this.ataque = 10;
                this.defesa = 4;
                this.manaMaxima = 220;
                this.mana = 220;
                break;
            case "arqueiro":
                // Arqueiro: Balanceado, foco em agilidade
                this.vidaMaxima = 105;
                this.vida = 105;
                this.ataque = 14;
                this.defesa = 7;
                this.manaMaxima = 90;
                this.mana = 90;
                break;
            default:
                // Padrão para classes não reconhecidas
                this.vidaMaxima = 100;
                this.vida = 100;
                this.ataque = 10;
                this.defesa = 8;
                this.manaMaxima = 100;
                this.mana = 100;
        }
        
        this.inventario = new ArrayList<>();
        this.materiais = new HashMap<>();
        this.arvoreHabilidades = new ArvoreHabilidades(classe);
        
        // Itens iniciais
        adicionarItemInicial();
    }
    
    /**
     * Adiciona equipamentos iniciais baseados na classe.
     */
    private void adicionarItemInicial() {
        switch (classe.toLowerCase()) {
            case "guerreiro":
                armaEquipada = new Arma("Espada de Ferro", "Espada básica de ferro", RaridadeItem.COMUM, 1, 12, TipoHabilidade.ATIVA, "⚔️");
                armaduraEquipada = new Armadura("Armadura de Couro", "Armadura simples de couro", RaridadeItem.COMUM, 1, 5, "Peito", "🛡️");
                break;
            case "mago":
                armaEquipada = new Arma("Cajado Básico", "Cajado simples de madeira", RaridadeItem.COMUM, 1, 8, TipoHabilidade.ATIVA, "🔮");
                armaduraEquipada = new Armadura("Túnica de Mago", "Túnica simples de tecido", RaridadeItem.COMUM, 1, 2, "Peito", "👘");
                break;
            case "arqueiro":
                armaEquipada = new Arma("Arco Longo", "Arco bem feito de madeira", RaridadeItem.COMUM, 1, 10, TipoHabilidade.ATIVA, "🏹");
                armaduraEquipada = new Armadura("Armadura de Couro Leve", "Armadura leve de couro", RaridadeItem.COMUM, 1, 3, "Peito", "🥋");
                break;
        }
        
        // Poções iniciais
        inventario.add(new Pocao("Poção de Cura", "Restaura 50 pontos de vida", 3, "Vida", 50));
        inventario.add(new Pocao("Poção de Mana", "Restaura 30 pontos de mana", 2, "Mana", 30));
    }
    
    /**
     * Adiciona experiência ao personagem e verifica level up.
     * @param exp Quantidade de experiência a adicionar
     */
    public void ganharExperiencia(int exp) {
        experiencia += exp;
        while (experiencia >= experienciaMaxima) {
            experiencia -= experienciaMaxima;
            subirNivel();
        }
    }
    
    /**
     * Aumenta o nível do personagem e seus atributos.
     * Bônus por nível: +10 HP, +5 Mana, +2 ATK, +1 DEF, +1 ponto de habilidade
     */
    private void subirNivel() {
        nivel++;
        experienciaMaxima = nivel * 100;
        
        // Aumentar atributos
        vidaMaxima += 10;
        vida = vidaMaxima;
        manaMaxima += 5;
        mana = manaMaxima;
        ataque += 2;
        defesa += 1;
        
        // Pontos de habilidade
        arvoreHabilidades.adicionarPontos(1);
        
        System.out.println("\n🎉 PARABÉNS! Você subiu para o nível " + nivel + "!");
        System.out.println("📈 Atributos aumentados!");
        System.out.println("⭐ 1 ponto de habilidade disponível!");
    }
    
    /**
     * Adiciona um item ao inventário.
     * @param item Item a ser adicionado
     */
    public void adicionarItem(Item item) {
        inventario.add(item);
    }
    
    /**
     * Adiciona material ao inventário de materiais.
     * @param tipo Tipo do material
     * @param quantidade Quantidade a adicionar
     */
    public void adicionarMaterial(TipoMaterial tipo, int quantidade) {
        materiais.put(tipo, materiais.getOrDefault(tipo, 0) + quantidade);
    }
    
    /**
     * Usa uma poção do inventário.
     * @param indice Índice da poção no inventário
     * @return true se a poção foi usada com sucesso
     */
    public boolean usarPocao(int indice) {
        if (indice >= 0 && indice < inventario.size()) {
            Item item = inventario.get(indice);
            if (item instanceof Pocao) {
                Pocao pocao = (Pocao) item;
                if (pocao.getQuantidade() > 0) {
                    switch (pocao.getTipoEfeito()) {
                        case "Vida":
                            vida = Math.min(vida + pocao.getValorEfeito(), vidaMaxima);
                            break;
                        case "Mana":
                            mana = Math.min(mana + pocao.getValorEfeito(), manaMaxima);
                            break;
                    }
                    // Criar nova poção com quantidade atualizada
                    Pocao novaPocao = new Pocao(pocao.getNome(), pocao.getDescricao(), 
                                               pocao.getQuantidade() - 1, pocao.getTipoEfeito(), pocao.getValorEfeito());
                    if (novaPocao.getQuantidade() <= 0) {
                        inventario.remove(indice);
                    } else {
                        inventario.set(indice, novaPocao);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Retorna o status completo formatado do personagem.
     * @return String com todos os atributos e equipamentos
     */
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STATUS DO PERSONAGEM ===\n");
        sb.append("👤 ").append(nome).append(" (").append(classe).append(")\n");
        sb.append("📊 Nível: ").append(nivel).append("\n");
        sb.append("✨ EXP: ").append(experiencia).append("/").append(experienciaMaxima).append("\n");
        sb.append("❤️ Vida: ").append(vida).append("/").append(vidaMaxima).append("\n");
        sb.append("💙 Mana: ").append(mana).append("/").append(manaMaxima).append("\n");
        sb.append("⚔️ Ataque: ").append(ataqueTotal()).append("\n");
        sb.append("🛡️ Defesa: ").append(defesaTotal()).append("\n");
        
        if (armaEquipada != null) {
            sb.append("\n🔫 Arma: ").append(armaEquipada.getNome()).append(" (⚔️").append(armaEquipada.getDano()).append(")\n");
        }
        if (armaduraEquipada != null) {
            sb.append("🎽 Armadura: ").append(armaduraEquipada.getNome()).append(" (🛡️").append(armaduraEquipada.getDefesa()).append(")\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Retorna a descrição formatada do inventário.
     * @return String com todos os itens do inventário
     */
    public String getInventarioDescricao() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTÁRIO ===\n");
        
        for (int i = 0; i < inventario.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(inventario.get(i).getDescricaoCompleta()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Retorna a descrição formatada dos materiais.
     * @return String com todos os materiais e quantidades
     */
    public String getMateriaisDescricao() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MATERIAIS ===\n");
        
        for (Map.Entry<TipoMaterial, Integer> entry : materiais.entrySet()) {
            sb.append(entry.getKey().getIcone()).append(" ").append(entry.getKey().getNome())
              .append(": x").append(entry.getValue()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Calcula o ataque total considerando base, arma e habilidades.
     * @return Ataque total
     */
    private int ataqueTotal() {
        int total = ataque;
        if (armaEquipada != null) {
            total += armaEquipada.getDano();
        }
        
        // Bônus de habilidades
        for (Habilidade hab : arvoreHabilidades.getHabilidadesAprendidas()) {
            if (hab.getNome().equals("Golpe Potente")) {
                total += total * 0.15 * hab.getNivelAtual();
            }
        }
        
        return total;
    }
    
    /**
     * Calcula a defesa total considerando base, armadura e habilidades.
     * @return Defesa total
     */
    private int defesaTotal() {
        int total = defesa;
        if (armaduraEquipada != null) {
            total += armaduraEquipada.getDefesa();
        }
        
        // Bônus de habilidades
        for (Habilidade hab : arvoreHabilidades.getHabilidadesAprendidas()) {
            if (hab.getNome().equals("Escudo Reforçado")) {
                total += total * 0.2 * hab.getNivelAtual();
            }
        }
        
        return total;
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getClasse() { return classe; }
    public int getNivel() { return nivel; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaque() { return ataqueTotal(); }
    public int getDefesa() { return defesaTotal(); }
    public int getMana() { return mana; }
    public int getManaMaxima() { return manaMaxima; }
    public ArvoreHabilidades getArvoreHabilidades() { return arvoreHabilidades; }
    public List<Item> getInventario() { return inventario; }
    public Map<TipoMaterial, Integer> getMateriais() { return materiais; }
}

// ========== SISTEMA DE INIMIGOS ==========
/**
 * Enumeração dos tipos de inimigos disponíveis no jogo.
 * Cada tipo tem um nível base e ícone representativo.
 */
enum TipoInimigo {
    ESQUELETO("Esqueleto", "💀", 1),
    GOBLIN("Goblin", "👺", 1),
    VAMPIRO("Vampiro", "🧛", 3),
    DRAGÃO("Dragão", "🐉", 4);
    
    private String nome;
    private String icone;
    private int nivel;
    
    /**
     * Construtor do enum de inimigo.
     * @param nome Nome do tipo de inimigo
     * @param icone Ícone representativo
     * @param nivel Nível base do inimigo
     */
    TipoInimigo(String nome, String icone, int nivel) {
        this.nome = nome;
        this.icone = icone;
        this.nivel = nivel;
    }
    
    public String getNome() { return nome; }
    public String getIcone() { return icone; }
    public int getNivel() { return nivel; }
}

/**
 * Representa um inimigo que pode ser encontrado em exploração.
 * Atributos são escalados baseados no nível do personagem.
 */
class Inimigo {
    protected String nome;
    protected int nivel;
    protected int vida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defesa;
    protected int experienciaRecompensa;
    protected String icone;
    
    /**
     * Construtor da classe Inimigo.
     * Escala os atributos baseados no nível do personagem.
     * @param tipo Tipo do inimigo
     * @param nivelPersonagem Nível do personagem para escalar atributos
     */
    public Inimigo(TipoInimigo tipo, int nivelPersonagem) {
        this.nome = tipo.getNome();
        this.icone = tipo.getIcone();
        this.nivel = Math.max(1, nivelPersonagem + tipo.getNivel() - 2);
        
        // Atributos balanceados por tipo
        switch (tipo) {
            case ESQUELETO:
                // Esqueleto: Balanceado, médio
                this.vidaMaxima = 60 + (nivel * 12);
                this.ataque = 12 + (nivel * 2);
                this.defesa = 5 + (nivel * 1);
                break;
            case GOBLIN:
                // Goblin: Fraco mas rápido
                this.vidaMaxima = 50 + (nivel * 10);
                this.ataque = 10 + (nivel * 2);
                this.defesa = 4 + (nivel * 1);
                break;
            case VAMPIRO:
                // Vampiro: Forte, alto ataque
                this.vidaMaxima = 80 + (nivel * 15);
                this.ataque = 20 + (nivel * 3);
                this.defesa = 10 + (nivel * 2);
                break;
            case DRAGÃO:
                // Dragão: Muito forte, chefe
                this.vidaMaxima = 120 + (nivel * 20);
                this.ataque = 30 + (nivel * 4);
                this.defesa = 18 + (nivel * 3);
                break;
            default:
                this.vidaMaxima = 60 + (nivel * 12);
                this.ataque = 12 + (nivel * 2);
                this.defesa = 5 + (nivel * 1);
        }
        
        this.vida = vidaMaxima;
        this.experienciaRecompensa = 25 + (nivel * 15);
    }
    
    /**
     * Aplica dano ao inimigo considerando sua defesa.
     * @param dano Dano a ser aplicado
     */
    public void receberDano(int dano) {
        int danoReal = Math.max(1, dano - defesa);
        vida -= danoReal;
    }
    
    /**
     * Verifica se o inimigo ainda está vivo.
     * @return true se vida > 0
     */
    public boolean estaVivo() {
        return vida > 0;
    }
    
    /**
     * Retorna o status formatado do inimigo.
     * @return String com atributos do inimigo
     */
    public String getStatus() {
        return String.format("%s %s (Nível %d)\n❤️ Vida: %d/%d\n⚔️ ATK: %d | 🛡️ DEF: %d", 
                icone, nome, nivel, vida, vidaMaxima, ataque, defesa);
    }
    
    // Getters
    public String getNome() { return nome; }
    public int getNivel() { return nivel; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public int getExperienciaRecompensa() { return experienciaRecompensa; }
    public String getIcone() { return icone; }
}

// ========== SISTEMA DE COMBATE ==========
/**
 * Gerencia o combate turn-based entre personagem e inimigo.
 * Implementa sistema de dados D21 com críticos e animações.
 */
class Combate {
    private Personagem personagem;
    private Inimigo inimigo;
    private Random random;
    private Scanner scanner;
    
    /**
     * Construtor da classe Combate.
     * @param personagem Personagem do jogador
     * @param inimigo Inimigo a combater
     */
    public Combate(Personagem personagem, Inimigo inimigo) {
        this.personagem = personagem;
        this.inimigo = inimigo;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Inicia o loop de combate.
     * Alterna turnos entre jogador e inimigo até vitória ou derrota.
     */
    public void iniciar() {
        AsciiArt.imprimirTextoDevagar("\n⚔️ COMBATE INICIADO! ⚔️", 50);
        
        while (personagem.getVida() > 0 && inimigo.estaVivo()) {
            exibirArenaCombate();
            
            System.out.println("\n=== SUA VEZ ===");
            AsciiArt.imprimirTextoDevagar("1. ⚔️  Atacar", 30);
            AsciiArt.imprimirTextoDevagar("2. ✨ Usar Habilidade", 30);
            AsciiArt.imprimirTextoDevagar("3. 🧪 Usar Poção", 30);
            AsciiArt.imprimirTextoDevagar("4. 🏃 Fugir", 30);
            AsciiArt.imprimirTextoDevagar("Escolha sua ação: ", 30);
            
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch (escolha) {
                case 1:
                    atacarAnimado();
                    break;
                case 2:
                    usarHabilidade();
                    break;
                case 3:
                    usarPocao();
                    break;
                case 4:
                    if (tentarFugir()) {
                        AsciiArt.imprimirTextoDevagar("🏃 Você fugiu do combate!", 50);
                        return;
                    }
                    break;
                default:
                    AsciiArt.imprimirTextoDevagar("❌ Opção inválida!", 30);
                    continue;
            }
            
            if (inimigo.estaVivo()) {
                turnoInimigoAnimado();
            }
        }
        
        if (personagem.getVida() > 0) {
            vitoria();
        } else {
            derrota();
        }
    }
    
    private void exibirArenaCombate() {
        AsciiArt.limparTela();

        String[] artePersonagemOriginal = normalizarArte(getArtePersonagem());
        String[] arteInimigoOriginal = normalizarArte(getArteInimigo());

        String statusHpPersonagem = "HP: " + personagem.getVida() + "/" + personagem.getVidaMaxima();
        String statusAtkPersonagem = "ATK: " + personagem.getAtaque();
        String statusDefPersonagem = "DEF: " + personagem.getDefesa();

        String statusHpInimigo = "HP: " + inimigo.getVida() + "/" + inimigo.getVidaMaxima();
        String statusAtkInimigo = "ATK: " + inimigo.getAtaque();
        String statusDefInimigo = "DEF: " + inimigo.getDefesa();

        // Cada lado usa a própria largura da ASCII art.
        // Assim uma arte pequena não força uma caixa gigante e uma arte grande não é espremida.
        int larguraBoxPersonagem = Math.max(
                obterMaiorLargura(artePersonagemOriginal),
                obterMaiorLarguraTextos(personagem.getNome(), statusHpPersonagem, statusAtkPersonagem, statusDefPersonagem)
        );

        int larguraBoxInimigo = Math.max(
                obterMaiorLargura(arteInimigoOriginal),
                obterMaiorLarguraTextos(inimigo.getNome(), statusHpInimigo, statusAtkInimigo, statusDefInimigo)
        );

        // Mínimo visual para não deixar status apertado quando a arte for muito pequena.
        larguraBoxPersonagem = Math.max(larguraBoxPersonagem, 24);
        larguraBoxInimigo = Math.max(larguraBoxInimigo, 24);

        // A altura acompanha a maior ASCII art entre os dois lados.
        int alturaBox = Math.max(artePersonagemOriginal.length, arteInimigoOriginal.length);
        alturaBox = Math.max(alturaBox, 10);

        // Aqui NÃO reduzimos a arte por largura fixa.
        // O box cresce de acordo com a arte.
        String[] artePersonagem = prepararArteParaBoxSemReduzir(artePersonagemOriginal, larguraBoxPersonagem, alturaBox);
        String[] arteInimigo = prepararArteParaBoxSemReduzir(arteInimigoOriginal, larguraBoxInimigo, alturaBox);

        String linhaSeparadora = "╔" + "═".repeat(larguraBoxPersonagem + 2) + "╦" + "═".repeat(larguraBoxInimigo + 2) + "╗";
        String linhaMeio = "╠" + "═".repeat(larguraBoxPersonagem + 2) + "╬" + "═".repeat(larguraBoxInimigo + 2) + "╣";
        String linhaFinal = "╚" + "═".repeat(larguraBoxPersonagem + 2) + "╩" + "═".repeat(larguraBoxInimigo + 2) + "╝";

        System.out.println(linhaSeparadora);

        System.out.printf(
            "║ %-" + larguraBoxPersonagem + "s ║ %-" + larguraBoxInimigo + "s ║%n",
            centralizarTexto(personagem.getNome(), larguraBoxPersonagem),
            centralizarTexto(inimigo.getNome(), larguraBoxInimigo)
        );

        System.out.println(linhaMeio);

        for (int i = 0; i < alturaBox; i++) {
            System.out.printf(
                "║ %-" + larguraBoxPersonagem + "s ║ %-" + larguraBoxInimigo + "s ║%n",
                artePersonagem[i],
                arteInimigo[i]
            );
        }

        System.out.println(linhaMeio);

        System.out.printf("║ %-" + larguraBoxPersonagem + "s ║ %-" + larguraBoxInimigo + "s ║%n",
                statusHpPersonagem,
                statusHpInimigo);

        System.out.printf("║ %-" + larguraBoxPersonagem + "s ║ %-" + larguraBoxInimigo + "s ║%n",
                statusAtkPersonagem,
                statusAtkInimigo);

        System.out.printf("║ %-" + larguraBoxPersonagem + "s ║ %-" + larguraBoxInimigo + "s ║%n",
                statusDefPersonagem,
                statusDefInimigo);

        System.out.println(linhaFinal);
        System.out.println();
    }

    private String[] prepararArteParaBoxSemReduzir(String[] arteOriginal, int larguraBox, int alturaBox) {
        String[] arte = normalizarArte(arteOriginal);
        arte = ajustarLarguraArteSemCortar(arte, larguraBox);
        arte = ajustarAlturaArte(arte, larguraBox, alturaBox);
        return arte;
    }

    private int obterMaiorLarguraTextos(String... textos) {
        int maior = 0;

        if (textos == null) {
            return maior;
        }

        for (String texto : textos) {
            if (texto != null && texto.length() > maior) {
                maior = texto.length();
            }
        }

        return maior;
    }

    private String[] prepararArteParaBox(String[] arteOriginal, int larguraBox, int alturaBox) {
        String[] arte = normalizarArte(arteOriginal);

        if (obterMaiorLargura(arte) > larguraBox || arte.length > alturaBox) {
            arte = reduzirArteProporcionalmente(arte, larguraBox, alturaBox);
        }

        arte = ajustarLarguraArteSemCortar(arte, larguraBox);
        arte = ajustarAlturaArte(arte, larguraBox, alturaBox);

        return arte;
    }

    private String[] normalizarArte(String[] arteOriginal) {
        if (arteOriginal == null || arteOriginal.length == 0) {
            return new String[]{""};
        }

        List<String> linhas = new ArrayList<>();

        for (String linha : arteOriginal) {
            linhas.add(rtrim(linha == null ? "" : linha));
        }

        while (!linhas.isEmpty() && linhas.get(0).trim().isEmpty()) {
            linhas.remove(0);
        }

        while (!linhas.isEmpty() && linhas.get(linhas.size() - 1).trim().isEmpty()) {
            linhas.remove(linhas.size() - 1);
        }

        if (linhas.isEmpty()) {
            linhas.add("");
        }

        return removerColunasVazias(linhas.toArray(new String[0]));
    }

    private String[] removerColunasVazias(String[] arte) {
        if (arte == null || arte.length == 0) {
            return new String[]{""};
        }

        int maiorLargura = obterMaiorLargura(arte);
        int primeiraColuna = maiorLargura;
        int ultimaColuna = -1;

        for (String linha : arte) {
            if (linha == null) linha = "";

            for (int i = 0; i < linha.length(); i++) {
                if (linha.charAt(i) != ' ') {
                    primeiraColuna = Math.min(primeiraColuna, i);
                    ultimaColuna = Math.max(ultimaColuna, i);
                }
            }
        }

        if (ultimaColuna < primeiraColuna) {
            return new String[]{""};
        }

        List<String> resultado = new ArrayList<>();

        for (String linha : arte) {
            if (linha == null) linha = "";

            StringBuilder novaLinha = new StringBuilder();

            for (int i = primeiraColuna; i <= ultimaColuna; i++) {
                if (i < linha.length()) {
                    novaLinha.append(linha.charAt(i));
                } else {
                    novaLinha.append(' ');
                }
            }

            resultado.add(rtrim(novaLinha.toString()));
        }

        return resultado.toArray(new String[0]);
    }

    private int obterMaiorLargura(String[] arte) {
        int maior = 0;

        if (arte == null) {
            return maior;
        }

        for (String linha : arte) {
            if (linha != null && linha.length() > maior) {
                maior = linha.length();
            }
        }

        return maior;
    }

    private String[] reduzirArteProporcionalmente(String[] arte, int larguraMaxima, int alturaMaxima) {
        int larguraAtual = obterMaiorLargura(arte);
        int alturaAtual = arte.length;

        if (larguraAtual <= 0 || alturaAtual <= 0) {
            return new String[]{""};
        }

        double escalaLargura = larguraAtual > larguraMaxima ? (double) larguraMaxima / larguraAtual : 1.0;
        double escalaAltura = alturaAtual > alturaMaxima ? (double) alturaMaxima / alturaAtual : 1.0;
        double escala = Math.min(escalaLargura, escalaAltura);

        if (escala >= 1.0) {
            return arte;
        }

        int novaAltura = Math.max(1, (int) Math.round(alturaAtual * escala));
        int novaLargura = Math.max(1, (int) Math.round(larguraAtual * escala));

        List<String> resultado = new ArrayList<>();

        for (int y = 0; y < novaAltura; y++) {
            int origemY = Math.min(alturaAtual - 1, (int) Math.floor(y / escala));
            String linhaOriginal = arte[origemY] == null ? "" : arte[origemY];

            StringBuilder novaLinha = new StringBuilder();

            for (int x = 0; x < novaLargura; x++) {
                int origemX = Math.min(larguraAtual - 1, (int) Math.floor(x / escala));

                if (origemX >= 0 && origemX < linhaOriginal.length()) {
                    novaLinha.append(linhaOriginal.charAt(origemX));
                } else {
                    novaLinha.append(' ');
                }
            }

            resultado.add(rtrim(novaLinha.toString()));
        }

        return removerColunasVazias(resultado.toArray(new String[0]));
    }

    private String[] ajustarLarguraArteSemCortar(String[] arte, int larguraBox) {
        String[] resultado = new String[arte.length];

        for (int i = 0; i < arte.length; i++) {
            String linha = arte[i] == null ? "" : arte[i];

            if (linha.length() > larguraBox) {
                linha = cortarLarguraCentro(linha, larguraBox);
            }

            resultado[i] = centralizarTexto(linha, larguraBox);
        }

        return resultado;
    }

    private String cortarLarguraCentro(String linha, int larguraBox) {
        if (linha == null) {
            return "";
        }

        if (linha.length() <= larguraBox) {
            return linha;
        }

        int inicio = Math.max(0, (linha.length() - larguraBox) / 2);
        int fim = Math.min(linha.length(), inicio + larguraBox);

        return linha.substring(inicio, fim);
    }

    private String[] ajustarAlturaArte(String[] arte, int larguraBox, int alturaBox) {
        List<String> linhas = new ArrayList<>(Arrays.asList(arte));

        if (linhas.size() > alturaBox) {
            linhas = cortarAlturaCentro(linhas, alturaBox);
        }

        int espacosAntes = (alturaBox - linhas.size()) / 2;
        int espacosDepois = alturaBox - linhas.size() - espacosAntes;

        List<String> resultado = new ArrayList<>();
        String linhaVazia = " ".repeat(larguraBox);

        for (int i = 0; i < espacosAntes; i++) {
            resultado.add(linhaVazia);
        }

        resultado.addAll(linhas);

        for (int i = 0; i < espacosDepois; i++) {
            resultado.add(linhaVazia);
        }

        return resultado.toArray(new String[0]);
    }

    private List<String> cortarAlturaCentro(List<String> linhas, int alturaBox) {
        int inicio = Math.max(0, (linhas.size() - alturaBox) / 2);
        int fim = Math.min(linhas.size(), inicio + alturaBox);

        return new ArrayList<>(linhas.subList(inicio, fim));
    }

    private String centralizarTexto(String texto, int largura) {
        if (texto == null) texto = "";

        if (texto.length() >= largura) {
            return texto.substring(0, largura);
        }

        int espacosTotal = largura - texto.length();
        int esquerda = espacosTotal / 2;
        int direita = espacosTotal - esquerda;

        return " ".repeat(esquerda) + texto + " ".repeat(direita);
    }

    private String rtrim(String texto) {
        if (texto == null) return "";

        int i = texto.length() - 1;

        while (i >= 0 && Character.isWhitespace(texto.charAt(i))) {
            i--;
        }

        return texto.substring(0, i + 1);
    }

    private String[] getArtePersonagem() {
        String classe = personagem.getClasse().toLowerCase();
        String[] arte;
        switch (classe) {
            case "guerreiro":
                arte = AsciiArt.getAsciiArt("cavaleiro");
                break;
            case "mago":
                arte = AsciiArt.getAsciiArt("mago");
                break;
            case "arqueiro":
                arte = AsciiArt.getAsciiArt("arqueiro");
                break;
            default:
                arte = AsciiArt.getAsciiArt("cavaleiro");
        }
        
        return arte;
    }
    
    private String[] getArteInimigo() {
        String nomeInimigo = inimigo.getNome().toLowerCase();
        String[] arte;
        switch (nomeInimigo) {
            case "esqueleto":
                arte = AsciiArt.getAsciiArt("esqueleto");
                break;
            case "goblin":
                arte = AsciiArt.getAsciiArt("goblin");
                break;
            case "dragão":
            case "dragon":
                arte = AsciiArt.getAsciiArt("dragon");
                break;
            case "vampiro":
                arte = AsciiArt.getAsciiArt("vampiro");
                break;
            default:
                arte = AsciiArt.getAsciiArt("esqueleto");
        }
        
        return arte;
    }
    
    /**
     * Executa o ataque do personagem com animação e sistema de dados.
     * Usa D21 com críticos: natural (dano 2x), sucesso (+25%), falha (perde turno).
     */
    private void atacarAnimado() {
        // Animar ataque
        AsciiArt.imprimirTextoDevagar("⚔️ " + personagem.getNome() + " prepara seu ataque!", 40);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Sistema de dados para ataque - D21
        int rolagemDado = SistemaDeDados.rolarD21();
        int bonusAtaque = personagem.getAtaque();
        int resultadoTotal = rolagemDado + bonusAtaque;
        
        // Calcular dano antes de mostrar
        int dano = 0;
        String mensagemCritico = "";
        
        if (SistemaDeDados.isFalhaCritica(rolagemDado)) {
            exibirArenaCombate();
            AsciiArt.imprimirTextoDevagar("❌ FALHA CRÍTICA! Você tropeça e perde o turno!", 40);
            return;
        } else if (SistemaDeDados.isCriticoNatural(rolagemDado)) {
            dano = personagem.getAtaque() * 2; // Dano dobrado
            mensagemCritico = " 💥 CRÍTICO NATURAL! DANO DOBRADO!";
        } else if (SistemaDeDados.isSucessoCritico(rolagemDado)) {
            dano = (int)(personagem.getAtaque() * 1.25); // +25% dano
            mensagemCritico = " ✨ SUCESSO CRÍTICO! +25% DANO!";
        } else {
            dano = personagem.getAtaque();
        }
        
        // Calcular dano final com defesa
        int danoFinal = Math.max(1, dano - inimigo.getDefesa());
        
        // Mostrar animação de ataque
        exibirArenaCombate();
        AsciiArt.imprimirTextoDevagar("🎲 D21: " + rolagemDado, 30);
        AsciiArt.imprimirTextoDevagar("⚔️ " + personagem.getNome() + " ATACA!" + mensagemCritico, 30);
        AsciiArt.imprimirTextoDevagar("⚔️ Causou " + danoFinal + " de dano em " + inimigo.getNome() + "!", 40);
        
        inimigo.receberDano(dano);
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Executa o turno do inimigo com animação e sistema de dados.
     * Usa o mesmo sistema D21 que o jogador.
     */
    private void turnoInimigoAnimado() {
        if (!inimigo.estaVivo()) return;
        
        AsciiArt.imprimirTextoDevagar("\n👹 " + inimigo.getNome() + " prepara seu contra-ataque!", 40);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Sistema de dados para inimigo - D21
        int rolagemDado = SistemaDeDados.rolarD21();
        int bonusAtaque = inimigo.getAtaque();
        int resultadoTotal = rolagemDado + bonusAtaque;
        
        // Calcular dano antes de mostrar
        int dano = 0;
        String mensagemCritico = "";
        
        if (SistemaDeDados.isFalhaCritica(rolagemDado)) {
            exibirArenaCombate();
            AsciiArt.imprimirTextoDevagar("✅ FALHA CRÍTICA DO INIMIGO! Ele tropeça e perde o turno!", 40);
            return;
        } else if (SistemaDeDados.isCriticoNatural(rolagemDado)) {
            dano = inimigo.getAtaque() * 2; // Dano dobrado
            mensagemCritico = " 💥 CRÍTICO NATURAL DO INIMIGO! DANO DOBRADO!";
        } else if (SistemaDeDados.isSucessoCritico(rolagemDado)) {
            dano = (int)(inimigo.getAtaque() * 1.25); // +25% dano
            mensagemCritico = " ✨ SUCESSO CRÍTICO DO INIMIGO! +25% DANO!";
        } else {
            dano = inimigo.getAtaque();
        }
        
        // Calcular dano final com defesa
        int danoFinal = Math.max(1, dano - personagem.getDefesa());
        
        exibirArenaCombate();
        AsciiArt.imprimirTextoDevagar("🎲 D21 inimigo: " + rolagemDado, 30);
        AsciiArt.imprimirTextoDevagar("👹 " + inimigo.getNome() + " CONTRA-ATACA!" + mensagemCritico, 30);
        AsciiArt.imprimirTextoDevagar("👹 Causou " + danoFinal + " de dano em " + personagem.getNome() + "!", 40);
        
        personagem.vida -= danoFinal;
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void atacar() {
        int dano = personagem.getAtaque();
        inimigo.receberDano(dano);
        
        System.out.printf("⚔️ %s ataca %s causando %d de dano!\n", 
                personagem.getNome(), inimigo.getNome(), dano);
    }
    
    private void usarHabilidade() {
        List<Habilidade> habilidades = personagem.getArvoreHabilidades().getHabilidadesAprendidas();
        if (habilidades.isEmpty()) {
            System.out.println("❌ Você não tem habilidades aprendidas!");
            return;
        }
        
        System.out.println("\n=== HABILIDADES ===");
        for (int i = 0; i < habilidades.size(); i++) {
            Habilidade hab = habilidades.get(i);
            System.out.printf("%d. %s (Nível %d)\n", i + 1, hab.getNome(), hab.getNivelAtual());
        }
        
        System.out.print("Escolha uma habilidade: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        if (escolha > 0 && escolha <= habilidades.size()) {
            Habilidade habilidade = habilidades.get(escolha - 1);
            
            if (personagem.getMana() >= 10) {
                personagem.mana -= 10;
                
                int dano = personagem.getAtaque() * 2;
                inimigo.receberDano(dano);
                
                System.out.printf("✨ %s usa %s!\n💥 Causa %d de dano em %s\n", 
                        personagem.getNome(), habilidade.getNome(), dano, inimigo.getNome());
            } else {
                System.out.println("❌ Mana insuficiente!");
            }
        } else {
            System.out.println("❌ Habilidade inválida!");
        }
    }
    
    private void usarPocao() {
        System.out.println("\n=== POÇÕES ===");
        List<Item> pocoes = new ArrayList<>();
        
        for (int i = 0; i < personagem.getInventario().size(); i++) {
            Item item = personagem.getInventario().get(i);
            if (item instanceof Pocao) {
                pocoes.add(item);
                System.out.printf("%d. %s\n", pocoes.size(), item.getDescricaoCompleta());
            }
        }
        
        if (pocoes.isEmpty()) {
            System.out.println("❌ Você não tem poções!");
            return;
        }
        
        System.out.print("Escolha uma poção: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        if (escolha > 0 && escolha <= pocoes.size()) {
            Pocao pocao = (Pocao) pocoes.get(escolha - 1);
            
            // Encontrar índice real da poção no inventário
            int indiceReal = -1;
            for (int i = 0; i < personagem.getInventario().size(); i++) {
                if (personagem.getInventario().get(i) == pocao) {
                    indiceReal = i;
                    break;
                }
            }
            
            if (indiceReal != -1 && personagem.usarPocao(indiceReal)) {
                System.out.printf("🧪 Você usou %s!\n", pocao.getNome());
            }
        } else {
            System.out.println("❌ Poção inválida!");
        }
    }
    
    private boolean tentarFugir() {
        return random.nextInt(100) < 50; // 50% de chance
    }
    
    private void turnoInimigo() {
        int dano = Math.max(1, inimigo.getAtaque() - personagem.getDefesa());
        personagem.vida -= dano;
        
        System.out.printf("👹 %s ataca causando %d de dano!\n", inimigo.getNome(), dano);
    }
    
    /**
     * Processa a vitória do jogador.
     * Concede EXP, ouro e materiais como recompensa.
     */
    private void vitoria() {
        AsciiArt.limparTela();
        
        // Gerar recompensas
        int ouro = random.nextInt(20) + 10;
        TipoMaterial[] materiais = TipoMaterial.values();
        TipoMaterial materialDrop = materiais[random.nextInt(materiais.length)];
        int quantidade = random.nextInt(3) + 1;
        
        personagem.ganharExperiencia(inimigo.getExperienciaRecompensa());
        personagem.adicionarMaterial(materialDrop, quantidade);
        
        // Exibir cena de vitória
        exibirCenaVitoria(ouro, materialDrop, quantidade);
    }
    
    private void exibirCenaVitoria(int ouro, TipoMaterial material, int quantidade) {
        // Obter arte ASCII do herói
        String[] heroiArte = getArtePersonagemVitoria();
        
        // Calcular largura para layout
        int larguraHeroi = Arrays.stream(heroiArte).mapToInt(String::length).max().orElse(30);
        int larguraTotal = larguraHeroi + 25;
        
        // Header
        String linhaSeparadora = "═".repeat(larguraTotal);
        System.out.println(linhaSeparadora);
        System.out.printf("║ %-" + (larguraTotal - 2) + "s ║%n", "🎉 VITÓRIA! 🎉");
        System.out.println(linhaSeparadora);
        
        // Exibir herói e recompensas lado a lado
        for (int i = 0; i < heroiArte.length; i++) {
            String linhaHeroi = heroiArte[i];
            String linhaRecompensas = "";
            
            // Adicionar recompensas nas linhas específicas
            if (i == 2) {
                linhaRecompensas = String.format("✨ +%d EXP", inimigo.getExperienciaRecompensa());
            } else if (i == 5) {
                linhaRecompensas = String.format("💰 +%d OURO", ouro);
            } else if (i == 8) {
                linhaRecompensas = String.format("%s +%d %s", material.getIcone(), quantidade, material.getNome());
            } else if (i == 11) {
                linhaRecompensas = "═════════════════";
            } else if (i == 13) {
                linhaRecompensas = "🏆 HERÓI VITORIOSO!";
            } else if (i == 15) {
                linhaRecompensas = "═════════════════";
            }
            
            System.out.printf("║ %-" + larguraHeroi + "s │ %s ║%n", linhaHeroi, linhaRecompensas);
        }
        
        System.out.println(linhaSeparadora);
        
        // Mensagens animadas
        try {
            Thread.sleep(1000);
            AsciiArt.imprimirTextoDevagar(String.format("⚔️ Você derrotou %s!", inimigo.getNome()), 50);
            Thread.sleep(800);
            AsciiArt.imprimirTextoDevagar(String.format("✨ Ganhou %d de experiência!", inimigo.getExperienciaRecompensa()), 50);
            Thread.sleep(800);
            AsciiArt.imprimirTextoDevagar(String.format("💰 Ganhou %d moedas de ouro!", ouro), 50);
            Thread.sleep(800);
            AsciiArt.imprimirTextoDevagar(String.format("%s Ganhou x%d %s!", material.getIcone(), quantidade, material.getNome()), 50);
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private String[] getArtePersonagemVitoria() {
        String classe = personagem.getClasse().toLowerCase();
        switch (classe) {
            case "guerreiro":
                return AsciiArt.getAsciiArt("cavaleiro");
            case "mago":
                return AsciiArt.getAsciiArt("mago");
            case "arqueiro":
                return AsciiArt.getAsciiArt("arqueiro");
            default:
                return AsciiArt.getAsciiArt("cavaleiro");
        }
    }
    
    /**
     * Processa a derrota do jogador.
     * Perde 10% da EXP e revive com 50% da vida.
     */
    private void derrota() {
        System.out.println("\n💀 DERROTA! 💀");
        System.out.println("Você foi derrotado em combate...");
        System.out.println("Perdeu 10% de experiência!");
        
        personagem.experiencia = Math.max(0, (int)(personagem.experiencia * 0.9));
        personagem.vida = personagem.vidaMaxima / 2; // Revive com 50% da vida
    }
}

// ========== SISTEMA DE MISSÕES ==========
/**
 * Enumeração dos tipos de missões disponíveis no jogo.
 */
enum TipoMissao {
    DERROTAR_INIMIGOS,
    COLETAR_MATERIAIS,
    EXPLORAR_AREA,
    CHEFE_EPICO
}

/**
 * Representa uma missão que o jogador pode completar.
 * Cada missão tem objetivos, recompensas e progresso.
 */
class Missao {
    private String nome;
    private String descricao;
    private TipoMissao tipo;
    private int nivelRequerido;
    private int recompensaXP;
    private Map<TipoMaterial, Integer> recompensasMateriais;
    private boolean concluida;
    private int progresso;
    private int objetivo;
    
    /**
     * Construtor da classe Missão.
     * @param nome Nome da missão
     * @param descricao Descrição do objetivo
     * @param tipo Tipo da missão
     * @param nivelRequerido Nível mínimo para aceitar
     * @param recompensaXP Experiência concedida
     * @param objetivo Quantidade para completar
     */
    public Missao(String nome, String descricao, TipoMissao tipo, int nivelRequerido, 
                 int recompensaXP, int objetivo) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
        this.recompensaXP = recompensaXP;
        this.recompensasMateriais = new HashMap<>();
        this.concluida = false;
        this.progresso = 0;
        this.objetivo = objetivo;
        
        // Adicionar recompensas de materiais baseadas no tipo
        adicionarRecompensasPadrao();
    }
    
    /**
     * Adiciona recompensas de materiais baseadas no tipo da missão.
     */
    private void adicionarRecompensasPadrao() {
        switch (tipo) {
            case DERROTAR_INIMIGOS:
                recompensasMateriais.put(TipoMaterial.ESSÊNCIA_MÁGICA, 2);
                break;
            case COLETAR_MATERIAIS:
                recompensasMateriais.put(TipoMaterial.FERRO, 3);
                recompensasMateriais.put(TipoMaterial.MADEIRA, 2);
                break;
            case EXPLORAR_AREA:
                recompensasMateriais.put(TipoMaterial.OURO, 1);
                break;
            case CHEFE_EPICO:
                recompensasMateriais.put(TipoMaterial.ESCAMA_DRAGÃO, 1);
                recompensasMateriais.put(TipoMaterial.CRISTAL_ARCANO, 1);
                break;
        }
    }
    
    /**
     * Atualiza o progresso da missão.
     * @param quantidade Quantidade a adicionar ao progresso
     */
    public void atualizarProgresso(int quantidade) {
        if (!concluida) {
            progresso += quantidade;
            if (progresso >= objetivo) {
                concluida = true;
                progresso = objetivo;
            }
        }
    }
    
    /**
     * Concede as recompensas da missão ao personagem.
     * @param personagem Personagem que completou a missão
     */
    public void completar(Personagem personagem) {
        if (concluida) {
            personagem.ganharExperiencia(recompensaXP);
            
            for (Map.Entry<TipoMaterial, Integer> entry : recompensasMateriais.entrySet()) {
                personagem.adicionarMaterial(entry.getKey(), entry.getValue());
            }
            
            System.out.println("\n✅ MISSÃO CONCLUÍDA!");
            System.out.println("🎯 " + nome);
            System.out.println("✨ " + recompensaXP + " de experiência!");
            
            for (Map.Entry<TipoMaterial, Integer> entry : recompensasMateriais.entrySet()) {
                System.out.printf("%s x%d %s\n", entry.getKey().getIcone(), entry.getValue(), entry.getKey().getNome());
            }
        }
    }
    
    /**
     * Retorna a descrição completa formatada da missão.
     * @return String com todas as informações da missão
     */
    public String getDescricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 ").append(nome).append(" (Nível ").append(nivelRequerido).append(")\n");
        sb.append("📝 ").append(descricao).append("\n");
        sb.append("📊 Progresso: ").append(progresso).append("/").append(objetivo).append("\n");
        sb.append("🎁 Recompensa: ").append(recompensaXP).append(" XP\n");
        
        for (Map.Entry<TipoMaterial, Integer> entry : recompensasMateriais.entrySet()) {
            sb.append("   ").append(entry.getKey().getIcone()).append(" x").append(entry.getValue()).append(" ").append(entry.getKey().getNome()).append("\n");
        }
        
        if (concluida) {
            sb.append("✅ CONCLUÍDA\n");
        } else {
            sb.append("⏳ EM ANDAMENTO\n");
        }
        
        return sb.toString();
    }
    
    // Getters
    public String getNome() { return nome; }
    public TipoMissao getTipo() { return tipo; }
    public int getNivelRequerido() { return nivelRequerido; }
    public boolean isConcluida() { return concluida; }
    public int getProgresso() { return progresso; }
    public int getObjetivo() { return objetivo; }
}

// ========== SISTEMA DE FORJA ==========
/**
 * Representa uma receita de craft que pode ser criada na forja.
 */
class ReceitaCraft {
    private String nome;
    private Map<TipoMaterial, Integer> materiaisNecessarios;
    private Item resultado;
    
    /**
     * Construtor da classe ReceitaCraft.
     * @param nome Nome da receita
     * @param materiais Mapa de materiais necessários
     * @param resultado Item resultante
     */
    public ReceitaCraft(String nome, Map<TipoMaterial, Integer> materiais, Item resultado) {
        this.nome = nome;
        this.materiaisNecessarios = materiais;
        this.resultado = resultado;
    }
    
    /**
     * Verifica se o personagem tem materiais suficientes.
     * @param materiaisPersonagem Materiais do personagem
     * @return true se pode craftar
     */
    public boolean podeCraft(Map<TipoMaterial, Integer> materiaisPersonagem) {
        for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
            if (materiaisPersonagem.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Consome os materiais e cria o item.
     * @param materiaisPersonagem Materiais do personagem
     */
    public void craft(Map<TipoMaterial, Integer> materiaisPersonagem) {
        if (podeCraft(materiaisPersonagem)) {
            // Remover materiais
            for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
                materiaisPersonagem.put(entry.getKey(), 
                    materiaisPersonagem.get(entry.getKey()) - entry.getValue());
            }
        }
    }
    
    /**
     * Retorna a descrição completa da receita.
     * @return String com materiais e resultado
     */
    public String getDescricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("🔨 ").append(nome).append("\n");
        sb.append("📋 Materiais necessários:\n");
        
        for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
            sb.append("   ").append(entry.getKey().getIcone()).append(" x").append(entry.getValue())
              .append(" ").append(entry.getKey().getNome()).append("\n");
        }
        
        sb.append("🎁 Resultado: ").append(resultado.getDescricaoCompleta()).append("\n");
        
        return sb.toString();
    }
    
    // Getters
    public String getNome() { return nome; }
    public Item getResultado() { return resultado; }
    public Map<TipoMaterial, Integer> getMateriaisNecessarios() { return materiaisNecessarios; }
}

/**
 * Gerencia o sistema de forja e suas receitas disponíveis.
 */
class SistemaForja {
    private List<ReceitaCraft> receitas;
    
    /**
     * Construtor da classe SistemaForja.
     * Inicializa as receitas padrão disponíveis.
     */
    public SistemaForja() {
        this.receitas = new ArrayList<>();
        inicializarReceitas();
    }
    
    /**
     * Inicializa as receitas padrão do sistema de forja.
     */
    private void inicializarReceitas() {
        // Armas
        Map<TipoMaterial, Integer> materiaisEspada = new HashMap<>();
        materiaisEspada.put(TipoMaterial.FERRO, 3);
        materiaisEspada.put(TipoMaterial.MADEIRA, 1);
        receitas.add(new ReceitaCraft("Espada de Ferro", materiaisEspada, 
            new Arma("Espada de Ferro", "Espada forjada em ferro", RaridadeItem.COMUM, 2, 15, TipoHabilidade.ATIVA, "⚔️")));
        
        Map<TipoMaterial, Integer> materiaisCajado = new HashMap<>();
        materiaisCajado.put(TipoMaterial.MADEIRA, 2);
        materiaisCajado.put(TipoMaterial.ESSÊNCIA_MÁGICA, 1);
        receitas.add(new ReceitaCraft("Cajado Arcano", materiaisCajado, 
            new Arma("Cajado Arcano", "Cajado imbuido de magia", RaridadeItem.RARO, 3, 12, TipoHabilidade.ATIVA, "🔮")));
        
        // Armaduras
        Map<TipoMaterial, Integer> materiaisArmadura = new HashMap<>();
        materiaisArmadura.put(TipoMaterial.FERRO, 4);
        materiaisArmadura.put(TipoMaterial.COURO, 2);
        receitas.add(new ReceitaCraft("Armadura de Ferro", materiaisArmadura, 
            new Armadura("Armadura de Ferro", "Armadura completa de ferro", RaridadeItem.RARO, 3, 8, "Completa", "🛡️")));
        
        // Poções
        Map<TipoMaterial, Integer> materiaisPocao = new HashMap<>();
        materiaisPocao.put(TipoMaterial.ESSÊNCIA_MÁGICA, 1);
        materiaisPocao.put(TipoMaterial.FERRO, 1);
        receitas.add(new ReceitaCraft("Poção de Cura Maior", materiaisPocao, 
            new Pocao("Poção de Cura Maior", "Restaura 100 pontos de vida", 1, "Vida", 100)));
    }
    
    /**
     * Retorna lista de receitas com indicador de disponibilidade.
     * @param materiaisPersonagem Materiais do personagem
     * @return String formatada com receitas e status
     */
    public String getReceitasDisponiveis(Map<TipoMaterial, Integer> materiaisPersonagem) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RECEITAS DISPONÍVEIS ===\n");
        
        for (int i = 0; i < receitas.size(); i++) {
            ReceitaCraft receita = receitas.get(i);
            sb.append("[").append(i + 1).append("] ").append(receita.getNome());
            
            if (receita.podeCraft(materiaisPersonagem)) {
                sb.append(" ✅\n");
            } else {
                sb.append(" ❌\n");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Retorna uma receita por índice.
     * @param indice Índice da receita
     * @return ReceitaCraft ou null se índice inválido
     */
    public ReceitaCraft getReceita(int indice) {
        if (indice >= 0 && indice < receitas.size()) {
            return receitas.get(indice);
        }
        return null;
    }
}

// ========== CLASSE PRINCIPAL DO JOGO ==========
/**
 * Classe principal do jogo RPG Terminal.
 * Gerencia o fluxo principal, menu e interações do jogador.
 */
public class RpgTerminal {
    private Personagem personagem;
    private Inimigo inimigoAtual;
    private List<Missao> missoes;
    private SistemaForja forja;
    private Random random;
    private Scanner scanner;
    private boolean rodando;
    
    /**
     * Construtor da classe principal.
     * Inicializa todos os sistemas do jogo.
     */
    public RpgTerminal() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.missoes = new ArrayList<>();
        this.forja = new SistemaForja();
        this.rodando = true;
        
        inicializarMissoes();
    }
    
    /**
     * Inicializa as missões padrão disponíveis no jogo.
     */
    private void inicializarMissoes() {
        missoes.add(new Missao("Primeiros Passos", "Derrote 3 inimigos", TipoMissao.DERROTAR_INIMIGOS, 1, 50, 3));
        missoes.add(new Missao("Ferreiro Aprendiz", "Colete 5 unidades de ferro", TipoMissao.COLETAR_MATERIAIS, 2, 75, 5));
        missoes.add(new Missao("Explorador", "Explore 2 áreas diferentes", TipoMissao.EXPLORAR_AREA, 3, 100, 2));
        missoes.add(new Missao("Caçador de Chefes", "Derrote 1 chefe", TipoMissao.CHEFE_EPICO, 5, 200, 1));
    }
    
    /**
     * Inicia o loop principal do jogo.
     * Exibe título, cria personagem e entra no menu principal.
     */
    public void iniciar() {
        AsciiArt.limparTela();
        
        // Título com arte ASCII
        AsciiArt.imprimirAsciiArt("dragão", 100);
        System.out.println();
        
        AsciiArt.imprimirTextoDevagar("=== BEM-VINDO AO RPG TERMINAL ===", 50);
        AsciiArt.imprimirTextoDevagar("Uma aventura épica no mundo dos textos!", 50);
        System.out.println();
        
        criarPersonagem();
        
        while (rodando) {
            exibirMenuPrincipal();
        }
        
        System.out.println();
        AsciiArt.imprimirTextoDevagar("👋 Obrigado por jogar! Até a próxima aventura!", 50);
    }
    
    private void criarPersonagem() {
        AsciiArt.imprimirTextoDevagar("\n=== CRIAÇÃO DE PERSONAGEM ===", 50);
        AsciiArt.imprimirTextoDevagar("Digite o nome do seu personagem: ", 30);
        String nome = scanner.nextLine();
        
        System.out.println();
        AsciiArt.imprimirTextoDevagar("Escolha sua classe:", 50);
        System.out.println();
        
        // Mostrar arte ASCII das classes
        System.out.println("1. Guerreiro - Alto HP e ataque");
        AsciiArt.imprimirAsciiArt("cavaleiro", 50);
        System.out.println();
        
        System.out.println("2. Mago - Magias poderosas");
        AsciiArt.imprimirAsciiArt("mago", 50);
        System.out.println();
        
        System.out.println("3. Arqueiro - Ágil e furtivo");
        AsciiArt.imprimirAsciiArt("arqueiro", 50);
        System.out.println();
        
        AsciiArt.imprimirTextoDevagar("Escolha (1-3): ", 30);
        
        int escolhaClasse = scanner.nextInt();
        scanner.nextLine();
        
        String classe;
        String arteClasse;
        switch (escolhaClasse) {
            case 1:
                classe = "Guerreiro";
                arteClasse = "cavaleiro";
                break;
            case 2:
                classe = "Mago";
                arteClasse = "mago";
                break;
            case 3:
                classe = "Arqueiro";
                arteClasse = "arqueiro";
                break;
            default:
                classe = "Aventureiro";
                arteClasse = "cavaleiro";
        }
        
        personagem = new Personagem(nome, classe);
        
        AsciiArt.limparTela();
        AsciiArt.imprimirAsciiArt(arteClasse, 100);
        System.out.println();
        AsciiArt.imprimirTextoDevagar("🎭 Personagem criado com sucesso!", 50);
        AsciiArt.imprimirTextoDevagar(personagem.getStatus(), 20);
    }
    
    private void exibirMenuPrincipal() {
        AsciiArt.imprimirTextoDevagar("\n=== MENU PRINCIPAL ===", 50);
        System.out.println();
        AsciiArt.imprimirTextoDevagar("1. 🗺️  Explorar", 30);
        AsciiArt.imprimirTextoDevagar("2. 📊 Status", 30);
        AsciiArt.imprimirTextoDevagar("3. 🎒 Inventário", 30);
        AsciiArt.imprimirTextoDevagar("4. ⭐ Habilidades", 30);
        AsciiArt.imprimirTextoDevagar("5. 📋 Missões", 30);
        AsciiArt.imprimirTextoDevagar("6. 🔨 Forja", 30);
        AsciiArt.imprimirTextoDevagar("7. 😴 Descansar", 30);
        AsciiArt.imprimirTextoDevagar("8. 🚪 Sair", 30);
        AsciiArt.imprimirTextoDevagar("Escolha uma opção: ", 30);
        
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        AsciiArt.imprimirTextoDevagar("\nProcessando sua escolha...", 50);
        
        switch (escolha) {
            case 1:
                explorar();
                break;
            case 2:
                AsciiArt.imprimirTextoDevagar("\n" + personagem.getStatus(), 20);
                break;
            case 3:
                exibirInventario();
                break;
            case 4:
                exibirHabilidades();
                break;
            case 5:
                exibirMissoes();
                break;
            case 6:
                acessarForja();
                break;
            case 7:
                descansar();
                break;
            case 8:
                rodando = false;
                break;
            default:
                AsciiArt.imprimirTextoDevagar("❌ Opção inválida!", 30);
        }
    }
    
    private void explorar() {
        AsciiArt.limparTela();
        AsciiArt.imprimirTextoDevagar("🗺️ EXPLORANDO...", 50);
        
        // Chance de encontrar inimigo
        if (random.nextInt(100) < 70) { // 70% de chance
            TipoInimigo[] tipos = TipoInimigo.values();
            TipoInimigo tipo = tipos[random.nextInt(tipos.length)];
            
            inimigoAtual = new Inimigo(tipo, personagem.getNivel());
            
            // Mostrar arte ASCII do inimigo
            String nomeArquivo = tipo.name().toLowerCase();
            if (nomeArquivo.equals("esqueleto")) {
                AsciiArt.imprimirAsciiArt("esqueleto", 80);
            } else if (nomeArquivo.equals("goblin")) {
                AsciiArt.imprimirAsciiArt("goblin", 80);
            } else if (nomeArquivo.equals("dragão")) {
                AsciiArt.imprimirAsciiArt("dragão", 100);
            } else if (nomeArquivo.equals("vampiro")) {
                AsciiArt.imprimirAsciiArt("vampiro", 80);
            } else {
                AsciiArt.imprimirAsciiArt("esqueleto", 80);
            }
            
            AsciiArt.imprimirTextoDevagar("⚠️ Você encontrou um " + inimigoAtual.getNome() + "!", 50);
            AsciiArt.imprimirTextoDevagar(inimigoAtual.getStatus(), 30);
            
            System.out.println();
            AsciiArt.imprimirTextoDevagar("Prepare-se para a batalha!", 50);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Combate combate = new Combate(personagem, inimigoAtual);
            combate.iniciar();
            
            // Atualizar missões
            atualizarMissoes(TipoMissao.DERROTAR_INIMIGOS, 1);
            
        } else {
            AsciiArt.imprimirTextoDevagar("🌿 Você explorou a área, mas não encontrou nada de especial.", 40);
            
            // Chance de encontrar materiais
            if (random.nextInt(100) < 30) { // 30% de chance
                TipoMaterial[] materiais = TipoMaterial.values();
                TipoMaterial material = materiais[random.nextInt(Math.min(4, materiais.length))];
                int quantidade = random.nextInt(3) + 1;
                
                personagem.adicionarMaterial(material, quantidade);
                AsciiArt.imprimirTextoDevagar(String.format("✨ Você encontrou %s x%d %s!", 
                    material.getIcone(), quantidade, material.getNome()), 40);
                
                atualizarMissoes(TipoMissao.COLETAR_MATERIAIS, quantidade);
            }
        }
        
        atualizarMissoes(TipoMissao.EXPLORAR_AREA, 1);
    }
    
    private void exibirInventario() {
        AsciiArt.imprimirTextoDevagar("\n" + personagem.getInventarioDescricao(), 20);
        AsciiArt.imprimirTextoDevagar("\n" + personagem.getMateriaisDescricao(), 20);
    }
    
    private void exibirHabilidades() {
        System.out.println("\n" + personagem.getArvoreHabilidades().getDescricaoRamos());
        
        System.out.println("Deseja aprender alguma habilidade? (s/n)");
        String resposta = scanner.nextLine();
        
        if (resposta.equalsIgnoreCase("s")) {
            System.out.println("Escolha o ramo:");
            System.out.println("1. Força");
            System.out.println("2. Defesa");
            System.out.println("3. Liderança");
            System.out.println("4. Fogo");
            System.out.println("5. Gelo");
            System.out.println("6. Arcano");
            System.out.println("7. Furtivo");
            System.out.println("8. Agilidade");
            System.out.println("9. Roubo");
            System.out.print("Escolha o ramo: ");
            
            int ramoEscolha = scanner.nextInt();
            scanner.nextLine();
            
            RamoHabilidade[] ramos = RamoHabilidade.values();
            if (ramoEscolha > 0 && ramoEscolha <= ramos.length) {
                RamoHabilidade ramo = ramos[ramoEscolha - 1];
                
                System.out.print("Escolha a habilidade (número): ");
                int habilidadeEscolha = scanner.nextInt();
                scanner.nextLine();
                
                if (personagem.getArvoreHabilidades().aprenderHabilidade(ramo, habilidadeEscolha - 1, personagem.getNivel())) {
                    System.out.println("✅ Habilidade aprendida com sucesso!");
                } else {
                    System.out.println("❌ Não foi possível aprender esta habilidade!");
                }
            }
        }
    }
    
    private void exibirMissoes() {
        System.out.println("\n=== MISSÕES DISPONÍVEIS ===");
        
        for (int i = 0; i < missoes.size(); i++) {
            Missao missao = missoes.get(i);
            if (!missao.isConcluida() && personagem.getNivel() >= missao.getNivelRequerido()) {
                System.out.println("[" + (i + 1) + "] " + missao.getDescricaoCompleta());
            }
        }
        
        System.out.println("\n=== MISSÕES CONCLUÍDAS ===");
        for (Missao missao : missoes) {
            if (missao.isConcluida()) {
                System.out.println("✅ " + missao.getNome());
            }
        }
    }
    
    private void acessarForja() {
        System.out.println("\n=== FORJA ===");
        System.out.println(forja.getReceitasDisponiveis(personagem.getMateriais()));
        
        System.out.print("Escolha uma receita (0 para voltar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        if (escolha > 0) {
            ReceitaCraft receita = forja.getReceita(escolha - 1);
            if (receita != null) {
                System.out.println("\n" + receita.getDescricaoCompleta());
                
                System.out.print("Confirmar criação? (s/n): ");
                String confirmar = scanner.nextLine();
                
                if (confirmar.equalsIgnoreCase("s")) {
                    if (receita.podeCraft(personagem.getMateriais())) {
                        receita.craft(personagem.getMateriais());
                        personagem.adicionarItem(receita.getResultado());
                        System.out.println("✅ Item criado com sucesso!");
                    } else {
                        System.out.println("❌ Materiais insuficientes!");
                    }
                }
            }
        }
    }
    
    private void descansar() {
        System.out.println("\n😴 Descansando...");
        personagem.vida = personagem.getVidaMaxima();
        personagem.mana = personagem.getManaMaxima();
        
        System.out.println("✅ Vida e mana restauradas!");
        System.out.println("💭 Você se sente revigorado!");
    }
    
    private void atualizarMissoes(TipoMissao tipo, int quantidade) {
        for (Missao missao : missoes) {
            if (!missao.isConcluida() && missao.getTipo() == tipo) {
                missao.atualizarProgresso(quantidade);
                
                if (missao.isConcluida()) {
                    missao.completar(personagem);
                }
            }
        }
    }
    
    /**
     * Ponto de entrada do programa.
     * Cria uma instância do jogo e inicia a execução.
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        RpgTerminal jogo = new RpgTerminal();
        jogo.iniciar();
    }
}
