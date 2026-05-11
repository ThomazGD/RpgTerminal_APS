import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

// ========== SISTEMA DE HABILIDADES ==========
enum TipoHabilidade {
    PASSIVA, ATIVA, BUFF, DEBUFF
}

enum RamoHabilidade {
    FORCA, DEFESA, LIDERANCA,  // Guerreiro
    FOGO, GELO, ARCANO,        // Mago
    FURTIVO, AGILIDADE, ROUBO  // Ladino
}

class Habilidade {
    protected String nome;
    protected String descricao;
    protected TipoHabilidade tipo;
    protected RamoHabilidade ramo;
    protected int nivelRequerido;
    protected int pontosNecessarios;
    protected int nivelAtual;
    protected int nivelMaximo;
    
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
    
    public boolean podeAprender(int nivelPersonagem, int pontosDisponiveis) {
        return nivelPersonagem >= nivelRequerido && 
               pontosDisponiveis >= pontosNecessarios && 
               nivelAtual < nivelMaximo;
    }
    
    public void aprender() {
        if (nivelAtual < nivelMaximo) {
            nivelAtual++;
        }
    }
    
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

class ArvoreHabilidades {
    private Map<RamoHabilidade, List<Habilidade>> ramos;
    private int pontosDisponiveis;
    private int pontosGastos;
    
    public ArvoreHabilidades(String classe) {
        this.ramos = new HashMap<>();
        this.pontosDisponiveis = 0;
        this.pontosGastos = 0;
        inicializarArvore(classe);
    }
    
    private void inicializarArvore(String classe) {
        switch (classe) {
            case "Guerreiro":
                inicializarArvoreGuerreiro();
                break;
            case "Mago":
                inicializarArvoreMago();
                break;
            case "Ladino":
                inicializarArvoreLadino();
                break;
        }
    }
    
    private void inicializarArvoreGuerreiro() {
        // Ramo de Força
        List<Habilidade> forca = new ArrayList<>();
        forca.add(new Habilidade("Força Bruta", "+5 ATK por nível", TipoHabilidade.PASSIVA, RamoHabilidade.FORCA, 1, 1, 5));
        forca.add(new Habilidade("Golpe Poderoso", "+10% chance de crítico", TipoHabilidade.PASSIVA, RamoHabilidade.FORCA, 3, 2, 3));
        forca.add(new Habilidade("Fúria Berserker", "ATK +50% por 3 turnos", TipoHabilidade.ATIVA, RamoHabilidade.FORCA, 5, 3, 1));
        ramos.put(RamoHabilidade.FORCA, forca);
        
        // Ramo de Defesa
        List<Habilidade> defesa = new ArrayList<>();
        defesa.add(new Habilidade("Armadura Pesada", "+3 DEF por nível", TipoHabilidade.PASSIVA, RamoHabilidade.DEFESA, 1, 1, 5));
        defesa.add(new Habilidade("Escudo Perfeito", "Bloqueia 100% dano 1x por batalha", TipoHabilidade.ATIVA, RamoHabilidade.DEFESA, 3, 2, 3));
        defesa.add(new Habilidade("Indestrutível", "HP +30, DEF +10", TipoHabilidade.PASSIVA, RamoHabilidade.DEFESA, 5, 3, 1));
        ramos.put(RamoHabilidade.DEFESA, defesa);
        
        // Ramo de Liderança
        List<Habilidade> lideranca = new ArrayList<>();
        lideranca.add(new Habilidade("Grito de Guerra", "Buff em todos os stats", TipoHabilidade.BUFF, RamoHabilidade.LIDERANCA, 2, 2, 3));
        lideranca.add(new Habilidade("Proteção de Aliado", "Recebe 50% dano por aliado", TipoHabilidade.PASSIVA, RamoHabilidade.LIDERANCA, 4, 3, 2));
        lideranca.add(new Habilidade("Vontade de Ferro", "Imune a efeitos negativos", TipoHabilidade.PASSIVA, RamoHabilidade.LIDERANCA, 6, 4, 1));
        ramos.put(RamoHabilidade.LIDERANCA, lideranca);
    }
    
    private void inicializarArvoreMago() {
        // Ramo de Fogo
        List<Habilidade> fogo = new ArrayList<>();
        fogo.add(new Habilidade("Mestre das Chamas", "+10% dano fogo", TipoHabilidade.PASSIVA, RamoHabilidade.FOGO, 1, 1, 5));
        fogo.add(new Habilidade("Bola de Fogo Aprimorada", "Dano +50%", TipoHabilidade.ATIVA, RamoHabilidade.FOGO, 3, 2, 3));
        fogo.add(new Habilidade("Tempestade de Fogo", "Ataque em área", TipoHabilidade.ATIVA, RamoHabilidade.FOGO, 5, 3, 1));
        ramos.put(RamoHabilidade.FOGO, fogo);
        
        // Ramo de Gelo
        List<Habilidade> gelo = new ArrayList<>();
        gelo.add(new Habilidade("Controle do Gelo", "+10% dano gelo", TipoHabilidade.PASSIVA, RamoHabilidade.GELO, 1, 1, 5));
        gelo.add(new Habilidade("Congelar", "Inimigo perde 1 turno", TipoHabilidade.DEBUFF, RamoHabilidade.GELO, 3, 2, 3));
        gelo.add(new Habilidade("Avalanche", "Dano em área + slow", TipoHabilidade.ATIVA, RamoHabilidade.GELO, 5, 3, 1));
        ramos.put(RamoHabilidade.GELO, gelo);
        
        // Ramo Arcano
        List<Habilidade> arcano = new ArrayList<>();
        arcano.add(new Habilidade("Mana Infinita", "Regenera 10 MP/turno", TipoHabilidade.PASSIVA, RamoHabilidade.ARCANO, 2, 2, 3));
        arcano.add(new Habilidade("Barreira Mágica", "Escudo que absorve dano", TipoHabilidade.BUFF, RamoHabilidade.ARCANO, 4, 3, 2));
        arcano.add(new Habilidade("Teletransporte Massivo", "Move todos aliados", TipoHabilidade.ATIVA, RamoHabilidade.ARCANO, 6, 4, 1));
        ramos.put(RamoHabilidade.ARCANO, arcano);
    }
    
    private void inicializarArvoreLadino() {
        // Ramo Furtivo
        List<Habilidade> furtivo = new ArrayList<>();
        furtivo.add(new Habilidade("Furtividade Aprimorada", "+20% dano surpresa", TipoHabilidade.PASSIVA, RamoHabilidade.FURTIVO, 1, 1, 5));
        furtivo.add(new Habilidade("Veneno Letal", "Dano ao longo do tempo", TipoHabilidade.DEBUFF, RamoHabilidade.FURTIVO, 3, 2, 3));
        furtivo.add(new Habilidade("Morte Súbita", "Chance de kill instant <25% HP", TipoHabilidade.ATIVA, RamoHabilidade.FURTIVO, 5, 3, 1));
        ramos.put(RamoHabilidade.FURTIVO, furtivo);
        
        // Ramo de Agilidade
        List<Habilidade> agilidade = new ArrayList<>();
        agilidade.add(new Habilidade("Velocidade do Vento", "+2 turnos extras", TipoHabilidade.PASSIVA, RamoHabilidade.AGILIDADE, 1, 1, 5));
        agilidade.add(new Habilidade("Esquiva Perfeita", "50% chance de evitar dano", TipoHabilidade.PASSIVA, RamoHabilidade.AGILIDADE, 3, 2, 3));
        agilidade.add(new Habilidade("Ataque Duplo", "2 ataques por turno", TipoHabilidade.ATIVA, RamoHabilidade.AGILIDADE, 5, 3, 1));
        ramos.put(RamoHabilidade.AGILIDADE, agilidade);
        
        // Ramo de Roubo
        List<Habilidade> roubo = new ArrayList<>();
        roubo.add(new Habilidade("Roubo Rápido", "Pega item do inimigo", TipoHabilidade.ATIVA, RamoHabilidade.ROUBO, 2, 2, 3));
        roubo.add(new Habilidade("Desarmar", "Remove arma do inimigo", TipoHabilidade.DEBUFF, RamoHabilidade.ROUBO, 4, 3, 2));
        roubo.add(new Habilidade("Fuga Garantida", "100% sucesso em fugir", TipoHabilidade.ATIVA, RamoHabilidade.ROUBO, 6, 4, 1));
        ramos.put(RamoHabilidade.ROUBO, roubo);
    }
    
    public void adicionarPontos(int pontos) {
        this.pontosDisponiveis += pontos;
    }
    
    public boolean aprenderHabilidade(RamoHabilidade ramo, int indice, int nivelPersonagem) {
        List<Habilidade> habilidadesRamo = ramos.get(ramo);
        if (habilidadesRamo == null || indice < 0 || indice >= habilidadesRamo.size()) {
            return false;
        }
        
        Habilidade habilidade = habilidadesRamo.get(indice);
        if (habilidade.podeAprender(nivelPersonagem, pontosDisponiveis)) {
            habilidade.aprender();
            pontosDisponiveis -= habilidade.getPontosNecessarios();
            pontosGastos += habilidade.getPontosNecessarios();
            return true;
        }
        return false;
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ÁRVORE DE HABILIDADES ===\n");
        sb.append(String.format("Pontos Disponíveis: %d | Pontos Gastos: %d\n\n", pontosDisponiveis, pontosGastos));
        
        for (Map.Entry<RamoHabilidade, List<Habilidade>> entry : ramos.entrySet()) {
            sb.append("【 ").append(entry.getKey()).append(" 】\n");
            for (int i = 0; i < entry.getValue().size(); i++) {
                Habilidade h = entry.getValue().get(i);
                sb.append(String.format("%d. %s\n", i + 1, h.getDescricaoCompleta()));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    // Getters
    public int getPontosDisponiveis() { return pontosDisponiveis; }
    public int getPontosGastos() { return pontosGastos; }
    public Map<RamoHabilidade, List<Habilidade>> getRamos() { return ramos; }
}

// ========== SISTEMA DE NPCS E MISSÕES ==========
enum TipoNPC {
    MERCADOR, GUARDA, MAGO, LIDER, ALDEAO, CHEFE
}

enum TipoMissao {
    PRINCIPAL, SECUNDARIA, DIARIA, ESPECIAL
}

enum EstadoMissao {
    NAO_INICIADA, EM_PROGRESSO, COMPLETA, FALHOU
}

class NPC {
    protected String nome;
    protected String descricao;
    protected TipoNPC tipo;
    protected List<String> dialogos;
    protected List<Missao> missoes;
    protected int reputacao;
    
    public NPC(String nome, String descricao, TipoNPC tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dialogos = new ArrayList<>();
        this.missoes = new ArrayList<>();
        this.reputacao = 0;
        inicializarDialogos();
    }
    
    private void inicializarDialogos() {
        switch (tipo) {
            case MERCADOR:
                dialogos.add("Olá, aventureiro! Tenho os melhores itens da região!");
                dialogos.add("Precisa de algo? Meus preços são justos.");
                dialogos.add("Volte sempre! Mercadoria nova chega todo dia.");
                break;
            case GUARDA:
                dialogos.add("Pare! Quem vai por aí?");
                dialogos.add("A cidade está perigosa ultimamente. Cuidado!");
                dialogos.add("Se precisar de ajuda, pode me chamar.");
                break;
            case MAGO:
                dialogos.add("Ah, um jovem aventureiro. Sinto poder em você.");
                dialogos.add("As artes arcanas são fascinantes, não acha?");
                dialogos.add("A magia flui por todos nós. Basta saber canalizá-la.");
                break;
            case LIDER:
                dialogos.add("Bem-vindo à nossa humilde aldeia.");
                dialogos.add("Precisamos de heróis como você.");
                dialogos.add("A paz é preciosa. Ajude-nos a mantê-la.");
                break;
            case ALDEAO:
                dialogos.add("Olá! Que bom ver um rosto novo por aqui.");
                dialogos.add("Tenho um probleminha... poderia me ajudar?");
                dialogos.add("Obrigado! Você é muito gentil.");
                break;
            case CHEFE:
                dialogos.add("INTRUSO! ousa desafiar meu poder?");
                dialogos.add("Sua jornada termina aqui!");
                dialogos.add("Impossível! Como você pode ser tão forte?");
                break;
        }
    }
    
    public String getDialogoAleatorio() {
        if (dialogos.isEmpty()) return "...";
        return dialogos.get(new Random().nextInt(dialogos.size()));
    }
    
    public void adicionarMissao(Missao missao) {
        missoes.add(missao);
    }
    
    public void aumentarReputacao(int quantidade) {
        reputacao += quantidade;
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(nome).append(" ===\n");
        sb.append(descricao).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Reputação: ").append(reputacao).append("\n");
        if (!missoes.isEmpty()) {
            sb.append("Missões disponíveis: ").append(missoes.size()).append("\n");
        }
        return sb.toString();
    }
    
    // Getters
    public String getNome() { return nome; }
    public TipoNPC getTipo() { return tipo; }
    public List<Missao> getMissoes() { return missoes; }
    public int getReputacao() { return reputacao; }
}

class Missao {
    protected String nome;
    protected String descricao;
    protected TipoMissao tipo;
    protected EstadoMissao estado;
    protected int nivelRequerido;
    protected int recompensaXP;
    protected int recompensaOuro;
    protected List<Item> recompensasItens;
    protected List<String> objetivos;
    protected List<Boolean> objetivosCompletos;
    protected NPC npcDador;
    
    public Missao(String nome, String descricao, TipoMissao tipo, int nivelRequerido, 
                   int recompensaXP, int recompensaOuro, NPC npcDador) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.estado = EstadoMissao.NAO_INICIADA;
        this.nivelRequerido = nivelRequerido;
        this.recompensaXP = recompensaXP;
        this.recompensaOuro = recompensaOuro;
        this.recompensasItens = new ArrayList<>();
        this.objetivos = new ArrayList<>();
        this.objetivosCompletos = new ArrayList<>();
        this.npcDador = npcDador;
    }
    
    public void adicionarObjetivo(String objetivo) {
        objetivos.add(objetivo);
        objetivosCompletos.add(false);
    }
    
    public void adicionarRecompensaItem(Item item) {
        recompensasItens.add(item);
    }
    
    public void completarObjetivo(int indice) {
        if (indice >= 0 && indice < objetivosCompletos.size()) {
            objetivosCompletos.set(indice, true);
            verificarCompleta();
        }
    }
    
    private void verificarCompleta() {
        boolean todosCompletos = true;
        for (Boolean completo : objetivosCompletos) {
            if (!completo) {
                todosCompletos = false;
                break;
            }
        }
        
        if (todosCompletos && estado == EstadoMissao.EM_PROGRESSO) {
            estado = EstadoMissao.COMPLETA;
        }
    }
    
    public boolean podeIniciar(int nivelPersonagem) {
        return nivelPersonagem >= nivelRequerido && 
               (estado == EstadoMissao.NAO_INICIADA || estado == EstadoMissao.EM_PROGRESSO);
    }
    
    public void iniciar() {
        if (estado == EstadoMissao.NAO_INICIADA) {
            estado = EstadoMissao.EM_PROGRESSO;
        }
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(nome).append(" ===\n");
        sb.append(descricao).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Nível Requerido: ").append(nivelRequerido).append("\n");
        sb.append("Recompensas: ").append(recompensaXP).append(" XP, ").append(recompensaOuro).append(" ouro\n");
        
        if (!objetivos.isEmpty()) {
            sb.append("\nObjetivos:\n");
            for (int i = 0; i < objetivos.size(); i++) {
                String status = objetivosCompletos.get(i) ? "✓" : "○";
                sb.append(String.format("  %s %s\n", status, objetivos.get(i)));
            }
        }
        
        return sb.toString();
    }
    
    // Getters
    public String getNome() { return nome; }
    public TipoMissao getTipo() { return tipo; }
    public EstadoMissao getEstado() { return estado; }
    public int getRecompensaXP() { return recompensaXP; }
    public int getRecompensaOuro() { return recompensaOuro; }
    public List<Item> getRecompensasItens() { return recompensasItens; }
    public NPC getNpcDador() { return npcDador; }
}

class GerenciadorMissoes {
    private List<Missao> todasMissoes;
    private List<Missao> missoesAtivas;
    private List<Missao> missoesCompletas;
    
    public GerenciadorMissoes() {
        this.todasMissoes = new ArrayList<>();
        this.missoesAtivas = new ArrayList<>();
        this.missoesCompletas = new ArrayList<>();
        inicializarMissoes();
    }
    
    private void inicializarMissoes() {
        // NPCs
        NPC mercador = new NPC("João Ferreiro", "Um mercador experiente com olhos afiados.", TipoNPC.MERCADOR);
        NPC guarda = new NPC("Marcos Sentinela", "Um guarda leal que protege a cidade.", TipoNPC.GUARDA);
        NPC mago = new NPC("Elara Arcana", "Uma maga poderosa e sábia.", TipoNPC.MAGO);
        
        // Missões Principais
        Missao missao1 = new Missao("A Ameaça do Dragão", 
            "Um dragão antigo ameaça a região. Derrote-o antes que seja tarde demais.", 
            TipoMissao.PRINCIPAL, 10, 1000, 500, mago);
        missao1.adicionarObjetivo("Encontrar a caverna do dragão");
        missao1.adicionarObjetivo("Derrotar o dragão");
        missao1.adicionarObjetivo("Voltar para Elara");
        
        // Missões Secundárias
        Missao missao2 = new Missao("Resgatar o Gato", 
            "O gato do velho João subiu na árvore e não desce.", 
            TipoMissao.SECUNDARIA, 1, 50, 25, mercador);
        missao2.adicionarObjetivo("Encontrar o gato");
        missao2.adicionarObjetivo("Trazê-lo de volta");
        
        Missao missao3 = new Missao("Bando de Goblins", 
            "Goblins estão atacando as caravanas. Elimine a ameaça.", 
            TipoMissao.SECUNDARIA, 3, 100, 75, guarda);
        missao3.adicionarObjetivo("Derrotar 5 goblins");
        missao3.adicionarObjetivo("Reportar ao guarda");
        
        // Missões Diárias
        Missao missao4 = new Missao("Treinamento Diário", 
            "Mantenha-se em forma treinando contra inimigos.", 
            TipoMissao.DIARIA, 1, 30, 15, guarda);
        missao4.adicionarObjetivo("Derrotar 3 inimigos");
        
        // Adicionar missões aos NPCs
        mercador.adicionarMissao(missao2);
        guarda.adicionarMissao(missao3);
        guarda.adicionarMissao(missao4);
        mago.adicionarMissao(missao1);
        
        todasMissoes.add(missao1);
        todasMissoes.add(missao2);
        todasMissoes.add(missao3);
        todasMissoes.add(missao4);
    }
    
    public void iniciarMissao(Missao missao) {
        if (missao.podeIniciar(1)) { // Nível será verificado dinamicamente
            missao.iniciar();
            if (!missoesAtivas.contains(missao)) {
                missoesAtivas.add(missao);
            }
        }
    }
    
    public void completarMissao(Missao missao) {
        if (missao.getEstado() == EstadoMissao.COMPLETA) {
            missoesAtivas.remove(missao);
            if (!missoesCompletas.contains(missao)) {
                missoesCompletas.add(missao);
            }
        }
    }
    
    public List<Missao> getMissoesDisponiveis(int nivelPersonagem) {
        List<Missao> disponiveis = new ArrayList<>();
        for (Missao missao : todasMissoes) {
            if (missao.podeIniciar(nivelPersonagem) && 
                missao.getEstado() == EstadoMissao.NAO_INICIADA) {
                disponiveis.add(missao);
            }
        }
        return disponiveis;
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== JORNADA DO HERÓI ===\n\n");
        
        sb.append("Missões Ativas (").append(missoesAtivas.size()).append("):\n");
        for (Missao missao : missoesAtivas) {
            sb.append("• ").append(missao.getNome()).append("\n");
        }
        
        sb.append("\nMissões Completas (").append(missoesCompletas.size()).append("):\n");
        for (Missao missao : missoesCompletas) {
            sb.append("✓ ").append(missao.getNome()).append("\n");
        }
        
        return sb.toString();
    }
    
    // Getters
    public List<Missao> getMissoesAtivas() { return missoesAtivas; }
    public List<Missao> getMissoesCompletas() { return missoesCompletas; }
    public List<Missao> getTodasMissoes() { return todasMissoes; }
}

// ========== SISTEMA DE CRAFT E FORJA ==========
enum RaridadeItem {
    COMUM("🟢", 1.0f),
    INCOMUM("🔵", 1.1f),
    RARO("🟣", 1.25f),
    EPICO("🟠", 1.5f),
    LENDARIO("🔴", 2.0f);
    
    private final String icone;
    private final float multiplicadorStats;
    
    RaridadeItem(String icone, float multiplicadorStats) {
        this.icone = icone;
        this.multiplicadorStats = multiplicadorStats;
    }
    
    public String getIcone() { return icone; }
    public float getMultiplicadorStats() { return multiplicadorStats; }
}

enum TipoMaterial {
    FERRO("Ferro", "🔩", 1, "Inimigos metálicos"),
    COURO("Couro", "🦁", 1, "Animais"),
    MADEIRA("Madeira", "🪵", 1, "Árvores"),
    PEDRA("Pedra", "🪨", 1, "Rochas"),
    CRISTAL("Cristal Mágico", "💎", 3, "Magos"),
    ESCAMA("Escama de Dragão", "🐉", 5, "Chefes"),
    OSSO("Osso Demoníaco", "💀", 4, "Inimigos poderosos"),
    ESSENCIA("Essência Divina", "✨", 10, "Missões especiais");
    
    private final String nome;
    private final String icone;
    private final int raridade;
    private final String fonte;
    
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

class Material {
    private TipoMaterial tipo;
    private int quantidade;
    
    public Material(TipoMaterial tipo, int quantidade) {
        this.tipo = tipo;
        this.quantidade = quantidade;
    }
    
    public void adicionar(int quantidade) {
        this.quantidade += quantidade;
    }
    
    public boolean remover(int quantidade) {
        if (this.quantidade >= quantidade) {
            this.quantidade -= quantidade;
            return true;
        }
        return false;
    }
    
    public String getDescricao() {
        return String.format("%s %s x%d", tipo.getIcone(), tipo.getNome(), quantidade);
    }
    
    // Getters
    public TipoMaterial getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
}

class ReceitaCraft {
    private String nome;
    private String descricao;
    private Map<TipoMaterial, Integer> materiaisNecessarios;
    private Item resultado;
    private int nivelForjaRequerido;
    private float chanceSucesso;
    
    public ReceitaCraft(String nome, String descricao, Item resultado, int nivelForjaRequerido) {
        this.nome = nome;
        this.descricao = descricao;
        this.materiaisNecessarios = new HashMap<>();
        this.resultado = resultado;
        this.nivelForjaRequerido = nivelForjaRequerido;
        this.chanceSucesso = 0.8f; // 80% base
    }
    
    public void adicionarMaterial(TipoMaterial material, int quantidade) {
        materiaisNecessarios.put(material, quantidade);
    }
    
    public boolean podeCraft(BolsaMateriais bolsa, int nivelForja) {
        if (nivelForja < nivelForjaRequerido) return false;
        
        for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
            Material material = bolsa.getMaterial(entry.getKey());
            if (material == null || material.getQuantidade() < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
    public Item craft(BolsaMateriais bolsa, int nivelForja) {
        if (!podeCraft(bolsa, nivelForja)) return null;
        
        // Calcular chance de sucesso baseada no nível
        float chanceFinal = chanceSucesso + (nivelForja - nivelForjaRequerido) * 0.05f;
        chanceFinal = Math.min(chanceFinal, 0.95f); // Máximo 95%
        
        if (new Random().nextFloat() > chanceFinal) {
            return null; // Falha no craft
        }
        
        // Consumir materiais
        for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
            bolsa.removerMaterial(entry.getKey(), entry.getValue());
        }
        
        // Criar item com chance de raridade aumentada
        Item itemCraftado = criarItemComRaridade();
        return itemCraftado;
    }
    
    private Item criarItemComRaridade() {
        Random rand = new Random();
        RaridadeItem raridade = RaridadeItem.COMUM;
        
        // Chance baseada nos materiais raros
        int pontosRaridade = 0;
        for (TipoMaterial material : materiaisNecessarios.keySet()) {
            pontosRaridade += material.getRaridade();
        }
        
        float chanceRara = Math.min(pontosRaridade * 0.05f, 0.3f);
        float chanceEpica = Math.min(pontosRaridade * 0.02f, 0.1f);
        float chanceLendaria = Math.min(pontosRaridade * 0.005f, 0.02f);
        
        float rolagem = rand.nextFloat();
        if (rolagem < chanceLendaria) {
            raridade = RaridadeItem.LENDARIO;
        } else if (rolagem < chanceLendaria + chanceEpica) {
            raridade = RaridadeItem.EPICO;
        } else if (rolagem < chanceLendaria + chanceEpica + chanceRara) {
            raridade = RaridadeItem.RARO;
        } else if (rolagem < 0.4f) {
            raridade = RaridadeItem.INCOMUM;
        }
        
        // Aplicar multiplicador de raridade
        int bonusAtaque = Math.round(resultado.getBonusAtaque() * raridade.getMultiplicadorStats());
        int bonusDefesa = Math.round(resultado.getBonusDefesa() * raridade.getMultiplicadorStats());
        int bonusVida = Math.round(resultado.getBonusVida() * raridade.getMultiplicadorStats());
        int valor = Math.round(resultado.getValor() * raridade.getMultiplicadorStats());
        
        String nomeRaro = raridade.getIcone() + " " + resultado.getNome();
        
        return new Item(nomeRaro, resultado.getTipo(), resultado.getDescricao(), 
                       valor, bonusAtaque, bonusDefesa, bonusVida, resultado.getHabilidadeEspecial());
    }
    
    public String getDescricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(nome).append(" ===\n");
        sb.append(descricao).append("\n");
        sb.append("Nível de Forja Requerido: ").append(nivelForjaRequerido).append("\n");
        sb.append("Chance de Sucesso: ").append(Math.round(chanceSucesso * 100)).append("%\n");
        sb.append("\nMateriais Necessários:\n");
        
        for (Map.Entry<TipoMaterial, Integer> entry : materiaisNecessarios.entrySet()) {
            sb.append(String.format("  %s %s x%d\n", 
                entry.getKey().getIcone(), entry.getKey().getNome(), entry.getValue()));
        }
        
        sb.append("\nResultado: ").append(resultado.getNome()).append("\n");
        return sb.toString();
    }
    
    // Getters
    public String getNome() { return nome; }
    public int getNivelForjaRequerido() { return nivelForjaRequerido; }
}

class BolsaMateriais {
    private Map<TipoMaterial, Material> materiais;
    
    public BolsaMateriais() {
        this.materiais = new HashMap<>();
    }
    
    public void adicionarMaterial(TipoMaterial tipo, int quantidade) {
        Material material = materiais.get(tipo);
        if (material == null) {
            material = new Material(tipo, 0);
            materiais.put(tipo, material);
        }
        material.adicionar(quantidade);
    }
    
    public boolean removerMaterial(TipoMaterial tipo, int quantidade) {
        Material material = materiais.get(tipo);
        if (material != null && material.remover(quantidade)) {
            if (material.getQuantidade() == 0) {
                materiais.remove(tipo);
            }
            return true;
        }
        return false;
    }
    
    public Material getMaterial(TipoMaterial tipo) {
        return materiais.get(tipo);
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BOLSA DE MATERIAIS ===\n");
        
        if (materiais.isEmpty()) {
            sb.append("Nenhum material coletado ainda.\n");
        } else {
            for (Material material : materiais.values()) {
                sb.append(material.getDescricao()).append("\n");
            }
        }
        
        return sb.toString();
    }
    
    public List<String> getMateriaisDisponiveis() {
        List<String> lista = new ArrayList<>();
        for (Material material : materiais.values()) {
            lista.add(material.getDescricao());
        }
        return lista;
    }
}

class SistemaForja {
    private int nivelForja;
    private int experienciaForja;
    private List<ReceitaCraft> receitas;
    private BolsaMateriais bolsaMateriais;
    
    public SistemaForja() {
        this.nivelForja = 1;
        this.experienciaForja = 0;
        this.receitas = new ArrayList<>();
        this.bolsaMateriais = new BolsaMateriais();
        inicializarReceitas();
    }
    
    private void inicializarReceitas() {
        // Receitas Básicas
        Item espadaBasica = new Item("Espada Simples", "Arma", "Espada básica de ferro", 50, 5, 0, 0, null);
        ReceitaCraft receitaEspada = new ReceitaCraft("Espada de Ferro", 
            "Uma espada simples mas eficaz", espadaBasica, 1);
        receitaEspada.adicionarMaterial(TipoMaterial.FERRO, 5);
        receitaEspada.adicionarMaterial(TipoMaterial.MADEIRA, 2);
        receitas.add(receitaEspada);
        
        Item armaduraBasica = new Item("Armadura Simples", "Armadura", "Armadura básica de couro", 40, 0, 3, 5, null);
        ReceitaCraft receitaArmadura = new ReceitaCraft("Armadura de Couro", 
            "Proteção básica para iniciantes", armaduraBasica, 1);
        receitaArmadura.adicionarMaterial(TipoMaterial.COURO, 8);
        receitaArmadura.adicionarMaterial(TipoMaterial.FERRO, 2);
        receitas.add(receitaArmadura);
        
        // Receitas Avançadas
        Item cajadoAvancado = new Item("Cajado Arcano", "Arma", "Cajado que canaliza magia poderosa", 150, 8, 0, 0, "+15 Mana");
        ReceitaCraft receitaCajado = new ReceitaCraft("Cajado Mágico", 
            "Ferramenta essencial para magos", cajadoAvancado, 3);
        receitaCajado.adicionarMaterial(TipoMaterial.MADEIRA, 4);
        receitaCajado.adicionarMaterial(TipoMaterial.CRISTAL, 2);
        receitas.add(receitaCajado);
        
        // Receitas Lendárias
        Item espadaLendaria = new Item("Espada de Dragão", "Arma", "Espada forjada com escamas de dragão", 500, 20, 10, 15, "Fogo +20%");
        ReceitaCraft receitaLendaria = new ReceitaCraft("Espada Lendária", 
            "Arma de poder imenso, forjada com materiais raros", espadaLendaria, 5);
        receitaLendaria.adicionarMaterial(TipoMaterial.ESCAMA, 10);
        receitaLendaria.adicionarMaterial(TipoMaterial.FERRO, 5);
        receitaLendaria.adicionarMaterial(TipoMaterial.ESSENCIA, 1);
        receitas.add(receitaLendaria);
    }
    
    public Item craft(ReceitaCraft receita) {
        Item resultado = receita.craft(bolsaMateriais, nivelForja);
        if (resultado != null) {
            // Ganhar experiência de forja
            experienciaForja += receita.getNivelForjaRequerido() * 10;
            verificarSubidaNivelForja();
        }
        return resultado;
    }
    
    private void verificarSubidaNivelForja() {
        int xpParaProximoNivel = nivelForja * 100;
        if (experienciaForja >= xpParaProximoNivel) {
            experienciaForja -= xpParaProximoNivel;
            nivelForja++;
            System.out.println("🔨 LEVEL UP! Seu nível de Forja agora é " + nivelForja + "!");
        }
    }
    
    public void adicionarMaterialAleatorio() {
        Random rand = new Random();
        TipoMaterial[] materiais = TipoMaterial.values();
        TipoMaterial materialSorteado = materiais[rand.nextInt(materiais.length)];
        int quantidade = rand.nextInt(3) + 1; // 1 a 3 unidades
        
        bolsaMateriais.adicionarMaterial(materialSorteado, quantidade);
    }
    
    public List<ReceitaCraft> getReceitasDisponiveis() {
        List<ReceitaCraft> disponiveis = new ArrayList<>();
        for (ReceitaCraft receita : receitas) {
            if (receita.podeCraft(bolsaMateriais, nivelForja)) {
                disponiveis.add(receita);
            }
        }
        return disponiveis;
    }
    
    public String getStatusCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SISTEMA DE FORJA ===\n");
        sb.append("Nível de Forja: ").append(nivelForja).append("\n");
        sb.append("Experiência: ").append(experienciaForja).append("/").append(nivelForja * 100).append("\n");
        sb.append("\n");
        sb.append(bolsaMateriais.getStatusCompleto());
        sb.append("\n");
        sb.append("Receitas Disponíveis: ").append(getReceitasDisponiveis().size()).append("/").append(receitas.size()).append("\n");
        
        return sb.toString();
    }
    
    // Getters
    public int getNivelForja() { return nivelForja; }
    public BolsaMateriais getBolsaMateriais() { return bolsaMateriais; }
    public List<ReceitaCraft> getReceitas() { return receitas; }
}

// ========== SISTEMA DE ITENS ==========
class Item {
    protected String nome;
    protected String tipo;
    protected String descricao;
    protected int valor;
    protected int bonusAtaque;
    protected int bonusDefesa;
    protected int bonusVida;
    protected String habilidadeEspecial;

    public Item(String nome, String tipo, String descricao, int valor,
                int bonusAtaque, int bonusDefesa, int bonusVida, String habilidadeEspecial) {
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefesa = bonusDefesa;
        this.bonusVida = bonusVida;
        this.habilidadeEspecial = habilidadeEspecial;
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public int getValor() { return valor; }
    public int getBonusAtaque() { return bonusAtaque; }
    public int getBonusDefesa() { return bonusDefesa; }
    public int getBonusVida() { return bonusVida; }
    public String getHabilidadeEspecial() { return habilidadeEspecial; }
}

class Inventario {
    private List<Item> itens;
    private int capacidade;
    private int ouro;

    public Inventario(int capacidade) {
        this.capacidade = capacidade;
        this.itens = new ArrayList<>();
        this.ouro = 100;
    }

    public boolean adicionarItem(Item item) {
        if (item == null) return false;
        if (itens.size() < capacidade) {
            itens.add(item);
            return true;
        }
        return false;
    }

    public void removerItem(int index) {
        if (index >= 0 && index < itens.size()) {
            itens.remove(index);
        }
    }

    public void limparItens() {
        itens.clear();
    }

    public List<Item> getItens() {
        return new ArrayList<>(itens);
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = Math.max(0, ouro);
    }

    public void adicionarOuro(int quantidade) {
        ouro += quantidade;
    }

    public boolean gastarOuro(int quantidade) {
        if (ouro >= quantidade) {
            ouro -= quantidade;
            return true;
        }
        return false;
    }

    public String getResumo() {
        return String.format("Inventário: %d/%d | Ouro: %d", itens.size(), capacidade, ouro);
    }
}

// ========== SISTEMA DE CHEFES ÉPICOS ==========
enum TipoChefe {
    DRAGAO_ANTIGO,
    DEMONIO_PRIMEVO,
    LICH_REI,
    FENIX_IMORTAL,
    TITAN_CONSTRUTOR
}

enum FaseChefe {
    FASE1, FASE2, FASE3, FASE4, FASE5, FURIA_FINAL
}

class PadraoAtaque {
    private String nome;
    private String descricao;
    private int danoBase;
    private int chanceUso; // percentagem
    private String efeitoEspecial;
    
    public PadraoAtaque(String nome, String descricao, int danoBase, int chanceUso, String efeitoEspecial) {
        this.nome = nome;
        this.descricao = descricao;
        this.danoBase = danoBase;
        this.chanceUso = chanceUso;
        this.efeitoEspecial = efeitoEspecial;
    }
    
    public boolean deveUsar() {
        return new Random().nextInt(100) < chanceUso;
    }
    
    public int calcularDano(int nivelChefe) {
        return danoBase + (nivelChefe * 10);
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeitoEspecial() { return efeitoEspecial; }
}

class ChefeEpico extends Inimigo {
    private TipoChefe tipoChefe;
    private FaseChefe faseAtual;
    private List<PadraoAtaque> padroesAtaque;
    private int nivelChefe;
    private int[] hpPorFase;
    private int[] ataquePorFase;
    private int[] defesaPorFase;
    private int turnosNaFase;
    private boolean enfurecido;
    private int contadorFuria;
    
    public ChefeEpico(String nome, TipoChefe tipoChefe, int nivelChefe, ImageIcon imagem) {
        super(nome, 1000, 100, 50, 1000, "Chefe Épico", imagem);
        this.tipoChefe = tipoChefe;
        this.faseAtual = FaseChefe.FASE1;
        this.nivelChefe = nivelChefe;
        this.padroesAtaque = new ArrayList<>();
        this.enfurecido = false;
        this.contadorFuria = 0;
        inicializarChefes();
    }
    
    private void inicializarChefes() {
        switch (tipoChefe) {
            case DRAGAO_ANTIGO:
                inicializarDragaoAntigo();
                break;
            case DEMONIO_PRIMEVO:
                inicializarDemonioPrimevo();
                break;
            case LICH_REI:
                inicializarLichRei();
                break;
            case FENIX_IMORTAL:
                inicializarFenixImortal();
                break;
            case TITAN_CONSTRUTOR:
                inicializarTitanConstrutor();
                break;
        }
    }
    
    private void inicializarDragaoAntigo() {
        // HP por fase: 1000, 2000, 3000, 4000, 5000
        hpPorFase = new int[]{1000, 2000, 3000, 4000, 5000};
        ataquePorFase = new int[]{50, 75, 100, 150, 200};
        defesaPorFase = new int[]{30, 40, 60, 80, 100};
        
        // Padrões de ataque
        padroesAtaque.add(new PadraoAtaque("Arranhão Mortal", "Ataque básico com garras afiadas", 40, 30, "Sangramento"));
        padroesAtaque.add(new PadraoAtaque("Sopro de Fogo", "Jato de fogo que atinge todos", 60, 25, "Dano em área"));
        padroesAtaque.add(new PadraoAtaque("Investida Dragônica", "Carga poderosa que knockback", 80, 20, "Derrubar"));
        padroesAtaque.add(new PadraoAtaque("Chamado da Tempestade", "Invoca dragões menores", 50, 15, "Invocação"));
        padroesAtaque.add(new PadraoAtaque("Fúria do Dragão", "Ataque devastador em fúria", 120, 10, "Crítico garantido"));
        
        this.vida = hpPorFase[0];
        this.vidaMaxima = hpPorFase[4]; // HP máximo considerando todas as fases
        this.ataque = ataquePorFase[0];
        this.defesa = defesaPorFase[0];
    }
    
    private void inicializarDemonioPrimevo() {
        hpPorFase = new int[]{800, 1600, 2400, 3200, 4000};
        ataquePorFase = new int[]{60, 90, 120, 180, 250};
        defesaPorFase = new int[]{25, 35, 50, 70, 90};
        
        padroesAtaque.add(new PadraoAtaque("Golpe Demoníaco", "Ataque sombrio corrosivo", 50, 30, "Corrosão"));
        padroesAtaque.add(new PadraoAtaque("Raio do Abismo", "Feixe de energia escura", 70, 25, "Dano contínuo"));
        padroesAtaque.add(new PadraoAtaque("Maldição Ancestral", "Amaldiçoa o inimigo", 40, 20, "Maldição"));
        padroesAtaque.add(new PadraoAtaque("Portal Demoníaco", "Invoca demônios", 60, 15, "Invocação"));
        padroesAtaque.add(new PadraoAtaque("Apocalipse", "Poder destrutivo máximo", 150, 10, "Dano massivo"));
        
        this.vida = hpPorFase[0];
        this.vidaMaxima = hpPorFase[4];
        this.ataque = ataquePorFase[0];
        this.defesa = defesaPorFase[0];
    }
    
    private void inicializarLichRei() {
        hpPorFase = new int[]{600, 1200, 1800, 2400, 3000};
        ataquePorFase = new int[]{40, 60, 80, 120, 180};
        defesaPorFase = new int[]{20, 30, 40, 60, 80};
        
        padroesAtaque.add(new PadraoAtaque("Toque Mortífero", "Drena vida do alvo", 45, 30, "Drenagem"));
        padroesAtaque.add(new PadraoAtaque("Bola de Energia", "Projétil mágico rápido", 55, 25, "Dano mágico"));
        padroesAtaque.add(new PadraoAtaque("Paralisia Temporal", "Congela o inimigo", 35, 20, "Paralisia"));
        padroesAtaque.add(new PadraoAtaque("Ressurreição", "Reviva aliados caídos", 40, 15, "Cura"));
        padroesAtaque.add(new PadraoAtaque("Morte Instantânea", "Feitiço de morte poderoso", 100, 10, "Chance de kill"));
        
        this.vida = hpPorFase[0];
        this.vidaMaxima = hpPorFase[4];
        this.ataque = ataquePorFase[0];
        this.defesa = defesaPorFase[0];
    }
    
    private void inicializarFenixImortal() {
        hpPorFase = new int[]{700, 1400, 2100, 2800, 3500};
        ataquePorFase = new int[]{55, 80, 110, 160, 220};
        defesaPorFase = new int[]{35, 45, 60, 80, 100};
        
        padroesAtaque.add(new PadraoAtaque("Bico Ardente", "Ataque rápido e preciso", 50, 30, "Queimadura"));
        padroesAtaque.add(new PadraoAtaque("Asas de Fogo", "Ataque em área com asas", 65, 25, "Dano em área"));
        padroesAtaque.add(new PadraoAtaque("Tempestade de Cinzas", "Nuvem de cinzas tóxicas", 45, 20, "Veneno"));
        padroesAtaque.add(new PadraoAtaque("Ressurreição", "Renasce das cinzas", 80, 15, "Cura completa"));
        padroesAtaque.add(new PadraoAtaque("Explosão Solar", "Explosão de energia solar", 140, 10, "Dano massivo"));
        
        this.vida = hpPorFase[0];
        this.vidaMaxima = hpPorFase[4];
        this.ataque = ataquePorFase[0];
        this.defesa = defesaPorFase[0];
    }
    
    private void inicializarTitanConstrutor() {
        hpPorFase = new int[]{1200, 2400, 3600, 4800, 6000};
        ataquePorFase = new int[]{45, 70, 95, 140, 200};
        defesaPorFase = new int[]{50, 70, 90, 120, 150};
        
        padroesAtaque.add(new PadraoAtaque("Martelo Titânico", "Golpe pesado e lento", 60, 30, "Stun"));
        padroesAtaque.add(new PadraoAtaque("Terremoto", "Abalo sísmico na área", 70, 25, "Dano em área"));
        padroesAtaque.add(new PadraoAtaque("Construção de Barreira", "Aumenta defesa temporariamente", 30, 20, "Buff"));
        padroesAtaque.add(new PadraoAtaque("Invocação Golem", "Cria golem de pedra", 50, 15, "Invocação"));
        padroesAtaque.add(new PadraoAtaque("Colapso Dimensional", "Poder destrutivo máximo", 160, 10, "Dano massivo"));
        
        this.vida = hpPorFase[0];
        this.vidaMaxima = hpPorFase[4];
        this.ataque = ataquePorFase[0];
        this.defesa = defesaPorFase[0];
    }
    
    public void proximaFase() {
        int indiceFase = faseAtual.ordinal();
        if (indiceFase < hpPorFase.length - 1) {
            faseAtual = FaseChefe.values()[indiceFase + 1];
            this.vida = hpPorFase[indiceFase + 1];
            this.ataque = ataquePorFase[indiceFase + 1];
            this.defesa = defesaPorFase[indiceFase + 1];
            turnosNaFase = 0;
            
            // Mensagem de mudança de fase
            System.out.println("🔥 " + nome + " entra na " + getNomeFase() + "!");
            System.out.println("HP: " + vida + " | ATK: " + ataque + " | DEF: " + defesa);
        } else {
            // Fúria final
            faseAtual = FaseChefe.FURIA_FINAL;
            enfurecido = true;
            this.ataque *= 2;
            this.defesa *= 0.8; // Reduz defesa mas aumenta ataque
            
            System.out.println("💀 " + nome + " entra em FÚRIA FINAL!");
            System.out.println("ATK dobrado! DEF reduzida!");
        }
    }
    
    public PadraoAtaque selecionarAtaque() {
        // Em fúria final, usa ataques mais poderosos
        List<PadraoAtaque> ataquesDisponiveis = new ArrayList<>();
        
        for (PadraoAtaque ataque : padroesAtaque) {
            if (enfurecido && ataque.getEfeitoEspecial().contains("massivo")) {
                return ataque; // Sempre usa o ataque mais poderoso em fúria
            } else if (ataque.deveUsar()) {
                ataquesDisponiveis.add(ataque);
            }
        }
        
        if (ataquesDisponiveis.isEmpty()) {
            return padroesAtaque.get(0); // Ataque básico se nenhum estiver disponível
        }
        
        return ataquesDisponiveis.get(new Random().nextInt(ataquesDisponiveis.size()));
    }
    
    public String executarAtaque(Personagem alvo) {
        turnosNaFase++;
        contadorFuria++;
        
        // A cada 3 turnos, chance de entrar em fúria
        if (!enfurecido && contadorFuria >= 3 && new Random().nextInt(100) < 30) {
            enfurecido = true;
            this.ataque *= 1.5;
            System.out.println("🔴 " + nome + " fica ENFURECIDO!");
        }
        
        PadraoAtaque ataqueSelecionado = selecionarAtaque();
        int dano = ataqueSelecionado.calcularDano(nivelChefe);
        
        // Aplicar modificadores de fase
        if (enfurecido) {
            dano = Math.round(dano * 1.5f);
        }
        
        alvo.receberDano(dano);
        
        String mensagem = String.format("⚔️ %s usa %s!\n💥 Causa %d de dano em %s%s", 
            nome, ataqueSelecionado.getNome(), dano, alvo.getNome(),
            ataqueSelecionado.getEfeitoEspecial() != null ? 
            "\n🌟 Efeito: " + ataqueSelecionado.getEfeitoEspecial() : "");
        
        return mensagem;
    }
    
    public void receberDano(int dano) {
        super.receberDano(dano);
        
        // Verificar se precisa mudar de fase
        if (!enfurecido && vida <= 0) {
            int indiceFase = faseAtual.ordinal();
            if (indiceFase < hpPorFase.length - 1) {
                proximaFase();
            }
        }
    }
    
    public String getNomeFase() {
        switch (faseAtual) {
            case FASE1: return "Fase Jovem";
            case FASE2: return "Fase Adulta";
            case FASE3: return "Fase Ancião";
            case FASE4: return "Fase Enfurecida";
            case FASE5: return "Fase Divina";
            case FURIA_FINAL: return "Fúria Final";
            default: return "Desconhecida";
        }
    }
    
    public String getStatusChefe() {
        StringBuilder sb = new StringBuilder();
        sb.append("👑 ").append(nome).append(" 👑\n");
        sb.append("Tipo: ").append(tipoChefe).append("\n");
        sb.append("Fase: ").append(getNomeFase()).append("\n");
        sb.append("HP: ").append(vida).append("/").append(hpPorFase[Math.min(faseAtual.ordinal(), hpPorFase.length - 1)]).append("\n");
        sb.append("ATK: ").append(ataque).append(" | DEF: ").append(defesa).append("\n");
        sb.append("Turnos na Fase: ").append(turnosNaFase).append("\n");
        if (enfurecido) {
            sb.append("🔴 ENFURECIDO! 🔴\n");
        }
        sb.append("Recompensa: ").append(getRecompensaExp()).append(" XP, ").append(getRecompensaOuro()).append(" ouro");
        
        return sb.toString();
    }
    
    public List<Item> getRecompensasLendarias() {
        List<Item> recompensas = new ArrayList<>();
        
        switch (tipoChefe) {
            case DRAGAO_ANTIGO:
                recompensas.add(new Item("🔴 Espada de Dragão", "Arma", "Forjada com escamas dracônicas", 1000, 30, 15, 20, "Fogo +25%"));
                recompensas.add(new Item("🔴 Armadura de Dragão", "Armadura", "Proteção impenetrável", 800, 10, 25, 30, "Resistência ao fogo"));
                recompensas.add(new Item("✨ Essência de Dragão", "Material", "Poder concentrado de dragão", 500, 0, 0, 0, "Craft lendário"));
                break;
            case DEMONIO_PRIMEVO:
                recompensas.add(new Item("🔴 Lâmina Abissal", "Arma", "Corrompida por energia escura", 900, 35, 10, 15, "Sombra +20%"));
                recompensas.add(new Item("🔴 Armadura Demoníaca", "Armadura", "Forjada em metal infernal", 750, 15, 20, 25, "Resistência escura"));
                break;
            case LICH_REI:
                recompensas.add(new Item("🔴 Cajado do Lich", "Arma", "Canaliza poder necromântico", 850, 20, 5, 10, "Mana +50"));
                recompensas.add(new Item("🔴 Coroa do Rei Morto", "Acessório", "Símbolo de poder sobre a morte", 600, 15, 10, 20, "Controle sobre mortos"));
                break;
            case FENIX_IMORTAL:
                recompensas.add(new Item("🔴 Lâmina da Fênix", "Arma", "Em chamas eternas", 950, 28, 12, 18, "Fogo +30%"));
                recompensas.add(new Item("🔴 Plumas Imortais", "Acessório", "Penas da ave renascida", 700, 10, 15, 25, "Ressurreição 1x"));
                break;
            case TITAN_CONSTRUTOR:
                recompensas.add(new Item("🔴 Martelo Titânico", "Arma", "Destruição em massa", 1100, 40, 20, 10, "Stun +25%"));
                recompensas.add(new Item("🔴 Armadura Titânica", "Armadura", "Proteção lendária", 900, 5, 35, 40, "Imunidade a knockback"));
                break;
        }
        
        return recompensas;
    }
    
    // Getters
    public TipoChefe getTipoChefe() { return tipoChefe; }
    public FaseChefe getFaseAtual() { return faseAtual; }
    public boolean estaEnfurecido() { return enfurecido; }
    public int getTurnosNaFase() { return turnosNaFase; }
}

class FabricaChefes {
    public static ChefeEpico criarChefe(TipoChefe tipo, int nivel, ImageIcon imagem) {
        String nome = getNomeChefe(tipo);
        return new ChefeEpico(nome, tipo, nivel, imagem);
    }
    
    private static String getNomeChefe(TipoChefe tipo) {
        switch (tipo) {
            case DRAGAO_ANTIGO: return "Ignis, o Dragão Ancião";
            case DEMONIO_PRIMEVO: return "Malgorath, o Demônio Primevo";
            case LICH_REI: return "Kael'thas, o Lich Rei";
            case FENIX_IMORTAL: return "Aurora, a Fênix Imortal";
            case TITAN_CONSTRUTOR: return "Atlas, o Titã Construtor";
            default: return "Chefe Desconhecido";
        }
    }
    
    public static List<TipoChefe> getChefesDisponiveis(int nivelJogador) {
        List<TipoChefe> disponiveis = new ArrayList<>();
        
        if (nivelJogador >= 5) disponiveis.add(TipoChefe.DRAGAO_ANTIGO);
        if (nivelJogador >= 8) disponiveis.add(TipoChefe.DEMONIO_PRIMEVO);
        if (nivelJogador >= 10) disponiveis.add(TipoChefe.LICH_REI);
        if (nivelJogador >= 12) disponiveis.add(TipoChefe.FENIX_IMORTAL);
        if (nivelJogador >= 15) disponiveis.add(TipoChefe.TITAN_CONSTRUTOR);
        
        return disponiveis;
    }
}

// ========== SISTEMA DE MAGIAS ==========
enum Magia {
    FIREBALL("Fireball", 15, 30, "Bola de fogo que causa dano massivo"),
    SHIELD("Shield", 10, 0, "Barreira mágica protetora"),
    TELEPORTE("Teleporte", 8, 0, "Teletransporte rápido"),
    BERSERKER("Berserker", 12, 20, "Fúria guerreira aumentando ataque"),
    BLOCK("Block", 5, 0, "Bloqueio perfeito do próximo ataque"),
    COUNTER("Counter", 10, 15, "Contra-ataque devastador"),
    STEALTH("Stealth", 7, 0, "Furtividade para ataque sorrateiro"),
    POISON("Poison", 12, 10, "Veneno corrosivo"),
    CRITICAL_STRIKE("Critical Strike", 15, 25, "Golpe crítico preciso");

    private final String nome;
    private final int custoMana;
    private final int dano;
    private final String descricao;

    Magia(String nome, int custoMana, int dano, String descricao) {
        this.nome = nome;
        this.custoMana = custoMana;
        this.dano = dano;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public int getCustoMana() { return custoMana; }
    public int getDano() { return dano; }
    public String getDescricao() { return descricao; }
}

// ========== CLASSES PRINCIPAIS ==========
class Personagem {
    protected int id;
    protected String nome;
    protected int vida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected int experiencia;
    protected String classe;
    protected ImageIcon imagem;
    protected int mana;
    protected int manaMaxima;
    protected Inventario inventario;
    protected List<Magia> magiasConhecidas;
    protected Item armaEquipada;
    protected Item armaduraEquipada;
    protected Item acessorioEquipado;
    protected ArvoreHabilidades arvoreHabilidades;
    protected GerenciadorMissoes gerenciadorMissoes;
    protected SistemaForja sistemaForja;

    public Personagem(String nome, int vida, int ataque, int defesa, String classe, ImageIcon imagem) {
        this.id = -1;
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.experiencia = 0;
        this.classe = classe;
        this.imagem = imagem;
        this.mana = 50;
        this.manaMaxima = 50;
        this.inventario = new Inventario(20);
        this.magiasConhecidas = new ArrayList<>();
        this.armaEquipada = null;
        this.armaduraEquipada = null;
        this.acessorioEquipado = null;
        
        // Inicializar sistemas avançados
        this.arvoreHabilidades = new ArvoreHabilidades(classe);
        this.gerenciadorMissoes = new GerenciadorMissoes();
        this.sistemaForja = new SistemaForja();
        
        adicionarMagiasIniciais();
    }

    private void adicionarMagiasIniciais() {
        switch (classe) {
            case "Mago":
                magiasConhecidas.add(Magia.FIREBALL);
                magiasConhecidas.add(Magia.SHIELD);
                magiasConhecidas.add(Magia.TELEPORTE);
                break;
            case "Guerreiro":
                magiasConhecidas.add(Magia.BERSERKER);
                magiasConhecidas.add(Magia.BLOCK);
                magiasConhecidas.add(Magia.COUNTER);
                break;
            case "Ladino":
                magiasConhecidas.add(Magia.STEALTH);
                magiasConhecidas.add(Magia.POISON);
                magiasConhecidas.add(Magia.CRITICAL_STRIKE);
                break;
        }
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getClasse() { return classe; }
    public ImageIcon getImagem() { return imagem; }
    public int getNivel() { return nivel; }
    public int getMana() { return mana; }
    public int getManaMaxima() { return manaMaxima; }
    public Inventario getInventario() { return inventario; }

    public List<Magia> getMagiasConhecidas() {
        return new ArrayList<>(magiasConhecidas);
    }

    public void atacar(Personagem alvo) {
        int dano = this.ataque
                + (armaEquipada != null ? armaEquipada.getBonusAtaque() : 0)
                + (acessorioEquipado != null ? acessorioEquipado.getBonusAtaque() : 0);

        alvo.receberDano(dano);
    }

    public void receberDano(int dano) {
        int defesaTotal = this.defesa
                + (armaduraEquipada != null ? armaduraEquipada.getBonusDefesa() : 0)
                + (acessorioEquipado != null ? acessorioEquipado.getBonusDefesa() : 0);

        int danoReal = Math.max(1, dano - defesaTotal);
        this.vida -= danoReal;

        if (this.vida < 0) this.vida = 0;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void ganharExperiencia(int exp) {
        this.experiencia += exp;

        while (this.experiencia >= nivel * 100) {
            this.experiencia -= nivel * 100;
            subirNivel();
        }
    }

    public void subirNivel() {
        nivel++;
        vidaMaxima += 20;
        vida = vidaMaxima;
        ataque += 5;
        defesa += 3;
        manaMaxima += 10;
        mana = manaMaxima;

        System.out.println("\nLEVEL UP!");
        System.out.println(nome + " alcançou o nível " + nivel + "!");
        System.out.println("HP +20 | ATK +5 | DEF +3 | MP +10");
        System.out.println("XP necessário para próximo nível: " + (nivel * 100));
        
        // FORÇAR REESCRITA DO SAVE A CADA LEVEL UP
        // NOTA: Esta chamada será feita pela classe JogoGrafico
        System.out.println("[SAVE] Progresso salvo automaticamente após LEVEL UP!");
    }

    public String getXPText() {
        int xpAtual = this.experiencia;
        int xpParaProximoNivel = nivel * 100;
        int xpFaltando = xpParaProximoNivel - xpAtual;
        return String.format("XP: %d/%d (Falta: %d)", xpAtual, xpParaProximoNivel, xpFaltando);
    }

    public boolean usarMagia(Magia magia, Personagem alvo) {
        if (mana < magia.getCustoMana()) return false;

        mana -= magia.getCustoMana();

        if (magia == Magia.SHIELD || magia == Magia.BLOCK || magia == Magia.TELEPORTE || magia == Magia.STEALTH) {
            return true;
        }

        int danoMagico = magia.getDano() + (classe.equals("Mago") ? 5 : 0);
        alvo.receberDano(danoMagico);
        return true;
    }

    public void regenerarMana(int quantidade) {
        mana = Math.min(manaMaxima, mana + quantidade);
    }

    public String getStatusText() {
        int atkTotal = ataque
                + (armaEquipada != null ? armaEquipada.getBonusAtaque() : 0)
                + (acessorioEquipado != null ? acessorioEquipado.getBonusAtaque() : 0);

        int defTotal = defesa
                + (armaduraEquipada != null ? armaduraEquipada.getBonusDefesa() : 0)
                + (acessorioEquipado != null ? acessorioEquipado.getBonusDefesa() : 0);

        return String.format("%s (Lv.%d %s)\nHP: %d/%d | MP: %d/%d | ATK: %d | DEF: %d\n%s",
                nome, nivel, classe, vida, vidaMaxima, mana, manaMaxima, atkTotal, defTotal, getXPText());
    }

    public String getStatusCompleto() {
        String equipamentos = "Equipamentos: ";
        equipamentos += armaEquipada != null ? armaEquipada.getNome() : "Nenhuma arma";
        equipamentos += " | " + (armaduraEquipada != null ? armaduraEquipada.getNome() : "Sem armadura");
        equipamentos += " | " + (acessorioEquipado != null ? acessorioEquipado.getNome() : "Sem acessório");

        return getStatusText() + "\n" + equipamentos + "\n" + inventario.getResumo();
    }
}

class Inimigo extends Personagem {
    private int recompensaExp;
    private int recompensaOuro;
    
    public Inimigo(String nome, int vida, int ataque, int defesa, int recompensaExp, String classe, ImageIcon imagem) {
        super(nome, vida, ataque, defesa, classe, imagem);
        this.recompensaExp = recompensaExp;
        this.recompensaOuro = recompensaExp / 2;
    }

    public int getRecompensaExp() { return recompensaExp; }
    public int getRecompensaOuro() { return recompensaOuro; }
}

// ========== FABRICA DE INIMIGOS ==========
class InimigoFactory {
    public static Inimigo criarInimigo(String tipo, int nivel) {
        ImageIcon imagem;
        String nome;
        int vida;
        int ataque;
        int defesa;
        int exp;

        switch (tipo.toLowerCase()) {
            case "goblin":
                imagem = new ImageIcon("img/goblin.png");
                nome = "Goblin";
                vida = 50 + nivel * 10;
                ataque = 12 + nivel * 2;
                defesa = 3 + nivel;
                exp = 30 + nivel * 10;
                break;
            case "esqueleto":
                imagem = new ImageIcon("img/esqueleto.png");
                nome = "Esqueleto";
                vida = 60 + nivel * 15;
                ataque = 15 + nivel * 3;
                defesa = 8 + nivel * 2;
                exp = 40 + nivel * 15;
                break;
            case "vampiro":
                imagem = new ImageIcon("img/vampiro.png");
                nome = "Vampiro";
                vida = 80 + nivel * 20;
                ataque = 18 + nivel * 4;
                defesa = 12 + nivel * 3;
                exp = 60 + nivel * 20;
                break;
            case "dragao":
                imagem = new ImageIcon("img/dragon.png");
                nome = "Dragão";
                vida = 150 + nivel * 30;
                ataque = 25 + nivel * 5;
                defesa = 15 + nivel * 4;
                exp = 100 + nivel * 30;
                break;
            case "fogo_elemental":
                imagem = new ImageIcon("img/fireelement.png");
                nome = "Elemental de Fogo";
                vida = 70 + nivel * 18;
                ataque = 20 + nivel * 4;
                defesa = 5 + nivel;
                exp = 50 + nivel * 18;
                break;
            case "gelo_elemental":
            default:
                imagem = new ImageIcon("img/iceelement.png");
                nome = "Elemental de Gelo";
                vida = 75 + nivel * 20;
                ataque = 18 + nivel * 3;
                defesa = 10 + nivel * 2;
                exp = 55 + nivel * 20;
                break;
        }

        Image imgOriginal = imagem.getImage();
        Image imgRedimensionada = imgOriginal.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        imagem = new ImageIcon(imgRedimensionada);

        return new Inimigo(nome, vida, ataque, defesa, exp, "Monstro", imagem);
    }
}

// ========== FABRICA DE ITENS ==========
class ItemFactory {
    public static Item criarArma(String tipo) {
        switch (tipo.toLowerCase()) {
            case "espada":
                return new Item("Espada Longa", "Arma", "Espada afiada de aço", 100, 8, 0, 0, null);
            case "arco":
                return new Item("Arco Longo", "Arma", "Arco preciso à distância", 80, 6, 0, 0, "Distância");
            case "cajado":
                return new Item("Cajado Mágico", "Arma", "Cajado que canaliza magia", 120, 4, 0, 0, "+10 Mana");
            default:
                return new Item("Adaga", "Arma", "Adaga rápida", 50, 3, 0, 0, "Crítico +5%");
        }
    }

    public static Item criarArmadura(String tipo) {
        switch (tipo.toLowerCase()) {
            case "couro":
                return new Item("Armadura de Couro", "Armadura", "Proteção leve de couro", 60, 0, 4, 10, null);
            case "metal":
                return new Item("Armadura de Metal", "Armadura", "Armadura pesada de metal", 150, 0, 8, 20, null);
            case "magica":
                return new Item("Armadura Mágica", "Armadura", "Armadura encantada", 200, 2, 6, 15, "+5 Mana regen");
            default:
                return new Item("Túnica Simples", "Armadura", "Túnica básica", 30, 0, 2, 5, null);
        }
    }

    public static Item criarAcessorio(String tipo) {
        switch (tipo.toLowerCase()) {
            case "anel":
                return new Item("Anel de Poder", "Acessório", "Anel mágico antigo", 80, 2, 2, 5, "+5 todos os stats");
            case "amuleto":
                return new Item("Amuleto da Sorte", "Acessório", "Amuleto da fortuna", 100, 3, 3, 10, "Crítico +10%");
            default:
                return new Item("Bracelete Simples", "Acessório", "Bracelete de couro", 40, 1, 1, 0, null);
        }
    }

    public static Item criarPocao(String tipo) {
        switch (tipo.toLowerCase()) {
            case "cura":
                return new Item("Poção de Cura", "Consumível", "Restaura 50 HP", 20, 0, 0, 0, "Cura 50 HP");
            case "mana":
                return new Item("Poção de Mana", "Consumível", "Restaura 30 MP", 15, 0, 0, 0, "Cura 30 MP");
            default:
                return new Item("Poção Fraca", "Consumível", "Recuperação básica", 10, 0, 0, 0, "Cura 20 HP");
        }
    }

    public static Item recriarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty() || nome.equals("null")) return null;

        switch (nome) {
            case "Espada Longa": return criarArma("espada");
            case "Arco Longo": return criarArma("arco");
            case "Cajado Mágico": return criarArma("cajado");
            case "Adaga": return criarArma("adaga");

            case "Armadura de Couro": return criarArmadura("couro");
            case "Armadura de Metal": return criarArmadura("metal");
            case "Armadura Mágica": return criarArmadura("magica");
            case "Túnica Simples": return criarArmadura("tunica");

            case "Anel de Poder": return criarAcessorio("anel");
            case "Amuleto da Sorte": return criarAcessorio("amuleto");
            case "Bracelete Simples": return criarAcessorio("bracelete");

            case "Poção de Cura": return criarPocao("cura");
            case "Poção de Mana": return criarPocao("mana");
            case "Poção Fraca": return criarPocao("fraca");

            default: return null;
        }
    }
}

// ========== SAVE MANAGER ==========
class SaveManager {
    private static final String SAVE_FILE = "rpg_saves.txt";
    private static final List<SaveData> saves = new ArrayList<>();
    private static int proximoId = 1;

    public SaveManager() {
        carregarSaves();
    }

    public static class SaveData {
        private final Personagem personagem;
        private final int runAtual;
        private final int monstrosDerrotados;

        public SaveData(Personagem personagem, int runAtual, int monstrosDerrotados) {
            this.personagem = personagem;
            this.runAtual = runAtual;
            this.monstrosDerrotados = monstrosDerrotados;
        }

        public Personagem getPersonagem() { return personagem; }
        public int getRunAtual() { return runAtual; }
        public int getMonstrosDerrotados() { return monstrosDerrotados; }
    }

    private void carregarSaves() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        saves.clear();
        proximoId = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";", -1);
                if (dados.length < 18) continue;

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                int vida = Integer.parseInt(dados[2]);
                int vidaMaxima = Integer.parseInt(dados[3]);
                int ataque = Integer.parseInt(dados[4]);
                int defesa = Integer.parseInt(dados[5]);
                int nivel = Integer.parseInt(dados[6]);
                int experiencia = Integer.parseInt(dados[7]);
                String classe = dados[8];
                int mana = Integer.parseInt(dados[9]);
                int manaMaxima = Integer.parseInt(dados[10]);
                int ouro = Integer.parseInt(dados[11]);
                String nomeArma = dados[12];
                String nomeArmadura = dados[13];
                String nomeAcessorio = dados[14];
                int runAtual = Integer.parseInt(dados[15]);
                int monstrosDerrotados = Integer.parseInt(dados[16]);
                String itensStr = dados[17];

                ImageIcon imagem = getImagemPorClasse(classe);

                Personagem p = new Personagem(nome, vidaMaxima, ataque, defesa, classe, imagem);
                p.id = id;
                p.vida = vida;
                p.vidaMaxima = vidaMaxima;
                p.ataque = ataque;
                p.defesa = defesa;
                p.nivel = nivel;
                p.experiencia = experiencia;
                p.mana = mana;
                p.manaMaxima = manaMaxima;

                p.inventario = new Inventario(20);
                p.inventario.setOuro(ouro);
                p.inventario.limparItens();

                if (!nomeArma.equals("null") && !nomeArma.isEmpty()) {
                    p.armaEquipada = ItemFactory.recriarPorNome(nomeArma);
                }
                if (!nomeArmadura.equals("null") && !nomeArmadura.isEmpty()) {
                    p.armaduraEquipada = ItemFactory.recriarPorNome(nomeArmadura);
                }
                if (!nomeAcessorio.equals("null") && !nomeAcessorio.isEmpty()) {
                    p.acessorioEquipado = ItemFactory.recriarPorNome(nomeAcessorio);
                }

                if (!itensStr.isEmpty()) {
                    String[] itens = itensStr.split("\\|");
                    for (String nomeItem : itens) {
                        Item item = ItemFactory.recriarPorNome(nomeItem);
                        if (item != null) {
                            p.inventario.adicionarItem(item);
                        }
                    }
                }

                saves.add(new SaveData(p, runAtual, monstrosDerrotados));

                if (id >= proximoId) {
                    proximoId = id + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar saves: " + e.getMessage());
        }
    }

    private ImageIcon getImagemPorClasse(String classe) {
        switch (classe) {
            case "Guerreiro": return new ImageIcon("img/cavaleiroimg.png");
            case "Mago": return new ImageIcon("img/mago.png");
            case "Ladino": return new ImageIcon("img/ladino.png");
            default: return new ImageIcon("img/cavaleiroimg.png");
        }
    }

    private String serializarItens(Inventario inventario) {
        List<Item> itens = inventario.getItens();
        if (itens.isEmpty()) return "null";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(itens.get(i).getNome());
        }
        return sb.toString();
    }

    private void salvarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE, false))) {
            for (SaveData save : saves) {
                Personagem p = save.getPersonagem();

                String linha =
                        p.id + ";" +
                        p.nome + ";" +
                        p.vida + ";" +
                        p.vidaMaxima + ";" +
                        p.ataque + ";" +
                        p.defesa + ";" +
                        p.nivel + ";" +
                        p.experiencia + ";" +
                        p.classe + ";" +
                        p.mana + ";" +
                        p.manaMaxima + ";" +
                        p.inventario.getOuro() + ";" +
                        (p.armaEquipada != null ? p.armaEquipada.getNome() : "null") + ";" +
                        (p.armaduraEquipada != null ? p.armaduraEquipada.getNome() : "null") + ";" +
                        (p.acessorioEquipado != null ? p.acessorioEquipado.getNome() : "null") + ";" +
                        save.getRunAtual() + ";" +
                        save.getMonstrosDerrotados() + ";" +
                        serializarItens(p.inventario);

                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private Personagem clonarPersonagem(Personagem original) {
        Personagem clone = new Personagem(
                original.nome,
                original.vidaMaxima,
                original.ataque,
                original.defesa,
                original.classe,
                original.getImagem()
        );

        clone.id = original.id;
        clone.vida = original.vida;
        clone.vidaMaxima = original.vidaMaxima;
        clone.ataque = original.ataque;
        clone.defesa = original.defesa;
        clone.nivel = original.nivel;
        clone.experiencia = original.experiencia;
        clone.mana = original.mana;
        clone.manaMaxima = original.manaMaxima;

        clone.inventario = new Inventario(20);
        clone.inventario.setOuro(original.inventario.getOuro());
        clone.inventario.limparItens();

        for (Item item : original.inventario.getItens()) {
            Item novo = ItemFactory.recriarPorNome(item.getNome());
            if (novo != null) clone.inventario.adicionarItem(novo);
        }

        clone.armaEquipada = original.armaEquipada != null ? ItemFactory.recriarPorNome(original.armaEquipada.getNome()) : null;
        clone.armaduraEquipada = original.armaduraEquipada != null ? ItemFactory.recriarPorNome(original.armaduraEquipada.getNome()) : null;
        clone.acessorioEquipado = original.acessorioEquipado != null ? ItemFactory.recriarPorNome(original.acessorioEquipado.getNome()) : null;

        return clone;
    }

    public int salvarPersonagem(Personagem p, int runAtual, int monstrosDerrotados) {
        if (p.id == -1) {
            p.id = proximoId++;
        }

        int index = -1;
        for (int i = 0; i < saves.size(); i++) {
            if (saves.get(i).getPersonagem().id == p.id) {
                index = i;
                break;
            }
        }

        SaveData novoSave = new SaveData(clonarPersonagem(p), runAtual, monstrosDerrotados);

        if (index >= 0) {
            saves.set(index, novoSave);
        } else {
            saves.add(novoSave);
        }

        salvarArquivo();
        return p.id;
    }

    public SaveData carregarPersonagem(int id) {
        for (SaveData save : saves) {
            if (save.getPersonagem().id == id) {
                return new SaveData(
                        clonarPersonagem(save.getPersonagem()),
                        save.getRunAtual(),
                        save.getMonstrosDerrotados()
                );
            }
        }
        return null;
    }

    public void listarPersonagens(JTextArea textArea) {
        textArea.setText("");
        textArea.append("=== PERSONAGENS SALVOS ===\n");

        if (saves.isEmpty()) {
            textArea.append("Nenhum personagem salvo ainda.\n");
        } else {
            for (SaveData save : saves) {
                Personagem p = save.getPersonagem();
                textArea.append(
                        "ID: " + p.id +
                        " | Nome: " + p.nome +
                        " | Classe: " + p.classe +
                        " | Nível: " + p.nivel +
                        " | Ouro: " + p.inventario.getOuro() +
                        " | Run: " + save.getRunAtual() +
                        " | Derrotados: " + save.getMonstrosDerrotados() +
                        "\n"
                );
            }
        }
    }
}
// ========== INTERFACE GRAFICA ==========
class JogoGrafico extends JFrame {
    private JTextArea textoArea;
    private JLabel personagemImage;
    private JLabel statusLabel;
    private JLabel inimigoImage;
    private JLabel inimigoStatus;

    private Personagem jogador;
    private Inimigo inimigoAtual;
    private final Random random = new Random();
    private SaveManager saveManager;

    private int runAtual = 1;
    private int monstrosDerrotados = 0;

    private static final int LARGURA_IMG = 400;
    private static final int ALTURA_IMG = 400;

    // Sistema de navegação
    private JPanel buttonPanel;
    private boolean emSubmenu = false;
    private List<JButton> botoesPrincipais = new ArrayList<>();

    public JogoGrafico() {
        super("RPG Gráfico");
        this.saveManager = new SaveManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        personagemImage = new JLabel();
        statusLabel = new JLabel("<html><div style='text-align:center;'>Crie ou carregue um personagem</div></html>", SwingConstants.CENTER);
        inimigoImage = new JLabel();
        inimigoStatus = new JLabel("<html><div style='text-align:center;'>Nenhum inimigo no momento</div></html>", SwingConstants.CENTER);
        textoArea = new JTextArea();

        configurarLabelsVisuais();

        textoArea.setEditable(false);
        textoArea.setBackground(Color.BLACK);
        textoArea.setForeground(Color.GREEN);
        textoArea.setCaretColor(Color.GREEN);
        textoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textoArea.setLineWrap(true);
        textoArea.setWrapStyleWord(true);

        JPanel painelPersonagem = new JPanel(new BorderLayout(0, 10));
        painelPersonagem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelPersonagem.add(personagemImage, BorderLayout.CENTER);
        painelPersonagem.add(statusLabel, BorderLayout.SOUTH);

        JPanel painelInimigo = new JPanel(new BorderLayout(0, 10));
        painelInimigo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelInimigo.add(inimigoImage, BorderLayout.CENTER);
        painelInimigo.add(inimigoStatus, BorderLayout.SOUTH);

        JPanel painelCombate = new JPanel(new GridLayout(1, 2, 40, 10));
        painelCombate.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        painelCombate.add(painelPersonagem);
        painelCombate.add(painelInimigo);

        JScrollPane scrollTexto = new JScrollPane(textoArea);
        scrollTexto.setPreferredSize(new Dimension(0, 150));

        buttonPanel = new JPanel(new GridLayout(1, 9, 6, 6));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 10, 10));

        JButton ataqueBtn = criarBotao("Ataque", Color.RED, Color.WHITE, e -> abrirMenuAcao("ataque"));
        JButton defesaBtn = criarBotao("Defesa", Color.BLUE, Color.WHITE, e -> abrirMenuAcao("defesa"));
        JButton magiaBtn = criarBotao("Magia", Color.MAGENTA, Color.WHITE, e -> abrirMenuAcao("magia"));
        JButton inventarioBtn = criarBotao("Inventário", Color.DARK_GRAY, Color.WHITE, e -> abrirInventario());
        JButton habilidadesBtn = criarBotao("Habilidades", new Color(102, 51, 153), Color.WHITE, e -> abrirHabilidades());
        JButton missoesBtn = criarBotao("Missões", new Color(34, 139, 34), Color.WHITE, e -> abrirMissões());
        JButton forjaBtn = criarBotao("Forja", new Color(156, 93, 12), Color.WHITE, e -> abrirForja());
        JButton npcsBtn = criarBotao("NPCs", new Color(0, 102, 153), Color.WHITE, e -> abrirNPC());
        JButton chefeBtn = criarBotao("Chefe", new Color(139, 0, 0), Color.WHITE, e -> abrirChefe());

        botoesPrincipais.add(ataqueBtn);
        botoesPrincipais.add(defesaBtn);
        botoesPrincipais.add(magiaBtn);
        botoesPrincipais.add(inventarioBtn);
        botoesPrincipais.add(habilidadesBtn);
        botoesPrincipais.add(missoesBtn);
        botoesPrincipais.add(forjaBtn);
        botoesPrincipais.add(npcsBtn);
        botoesPrincipais.add(chefeBtn);

        for (JButton btn : botoesPrincipais) {
            buttonPanel.add(btn);
        }

        JPanel painelInferior = new JPanel(new BorderLayout(0, 6));
        painelInferior.add(scrollTexto, BorderLayout.CENTER);
        painelInferior.add(buttonPanel, BorderLayout.SOUTH);

        add(painelCombate, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        setVisible(true);
        menuPrincipal();
    }

    private ImageIcon redimensionarIcone(ImageIcon icon, int largura, int altura) {
        if (icon == null) {
            return null;
        }

        Image img = icon.getImage();
        Image imgRedimensionada = img.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imgRedimensionada);
    }

    private void configurarLabelsVisuais() {
        personagemImage.setHorizontalAlignment(SwingConstants.CENTER);
        personagemImage.setVerticalAlignment(SwingConstants.CENTER);
        personagemImage.setPreferredSize(new Dimension(LARGURA_IMG, ALTURA_IMG));

        inimigoImage.setHorizontalAlignment(SwingConstants.CENTER);
        inimigoImage.setVerticalAlignment(SwingConstants.CENTER);
        inimigoImage.setPreferredSize(new Dimension(LARGURA_IMG, ALTURA_IMG));

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setVerticalAlignment(SwingConstants.TOP);
        statusLabel.setPreferredSize(new Dimension(400, 140));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        inimigoStatus.setHorizontalAlignment(SwingConstants.CENTER);
        inimigoStatus.setVerticalAlignment(SwingConstants.TOP);
        inimigoStatus.setPreferredSize(new Dimension(400, 140));
        inimigoStatus.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private JButton criarBotao(String texto, Color bg, Color fg, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.addActionListener(action);
        return btn;
    }

    private void abrirMenuAcao(String tipo) {
        // Limpar painel de botões atual
        buttonPanel.removeAll();
        
        // Criar botões de acordo com o tipo
        if (tipo.equals("ataque")) {
            JButton ataqueNormalBtn = criarBotao("Ataque Normal", Color.RED, Color.WHITE, e -> executarAcao(1));
            JButton ataqueForteBtn = criarBotao("Ataque Forte", Color.ORANGE, Color.WHITE, e -> executarAcao(4));
            JButton voltarBtn = criarBotao("Voltar", Color.GRAY, Color.WHITE, e -> voltarMenuPrincipal());
            
            buttonPanel.add(ataqueNormalBtn);
            buttonPanel.add(ataqueForteBtn);
            buttonPanel.add(voltarBtn);
        } else if (tipo.equals("defesa")) {
            JButton defenderBtn = criarBotao("Defender", Color.BLUE, Color.WHITE, e -> executarAcao(2));
            JButton esquivaBtn = criarBotao("Esquivar", Color.CYAN, Color.WHITE, e -> executarAcao(5));
            JButton voltarBtn = criarBotao("Voltar", Color.GRAY, Color.WHITE, e -> voltarMenuPrincipal());
            
            buttonPanel.add(defenderBtn);
            buttonPanel.add(esquivaBtn);
            buttonPanel.add(voltarBtn);
        } else if (tipo.equals("magia")) {
            // Adicionar magias conhecidas
            if (jogador != null) {
                for (Magia magia : jogador.getMagiasConhecidas()) {
                    JButton magiaBtn = criarBotao(magia.getNome(), Color.MAGENTA, Color.WHITE, 
                        e -> usarMagiaBattle(magia));
                    buttonPanel.add(magiaBtn);
                }
            }
            JButton voltarBtn = criarBotao("Voltar", Color.GRAY, Color.WHITE, e -> voltarMenuPrincipal());
            buttonPanel.add(voltarBtn);
        }
        
        emSubmenu = true;
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    private void voltarMenuPrincipal() {
        // Limpar painel atual
        buttonPanel.removeAll();
        
        // Restaurar botões principais
        for (JButton btn : botoesPrincipais) {
            buttonPanel.add(btn);
        }
        
        emSubmenu = false;
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    private void usarMagiaBattle(Magia magia) {
        if (jogador == null || inimigoAtual == null) {
            adicionarTexto("Nenhuma batalha em andamento!");
            voltarMenuPrincipal();
            return;
        }
        
        if (jogador.usarMagia(magia, inimigoAtual)) {
            adicionarTexto("[MAGIA] " + jogador.nome + " usou " + magia.getNome() + "!");
            adicionarTexto("[CUSTO] Mana consumida: " + magia.getCustoMana());
            
            if (magia.getDano() > 0) {
                adicionarTexto("[DANO] Dano mágico causado: " + magia.getDano());
            }
            
            // Contra-ataque do inimigo se ainda estiver vivo
            if (inimigoAtual.estaVivo()) {
                adicionarTexto("[CONTRA-ATAQUE] " + inimigoAtual.nome + " contra-ataca!");
                inimigoAtual.atacar(jogador);
            }
            
            atualizarPersonagemPanel();
            atualizarInimigoPanel();
            
            if (!jogador.estaVivo()) {
                tratarGameOver();
            } else if (!inimigoAtual.estaVivo()) {
                tratarVitoria(true);
            } else {
                salvarProgressoRun();
            }
        } else {
            adicionarTexto("[FALHA] Mana insuficiente para usar " + magia.getNome() + "!");
        }
        
        voltarMenuPrincipal();
    }

    private void executarAcao(int acao) {
        if (jogador == null || inimigoAtual == null) {
            adicionarTexto("Nenhuma batalha em andamento!");
            return;
        }

        if (!jogador.estaVivo() || !inimigoAtual.estaVivo()) {
            adicionarTexto("A batalha já terminou!");
            return;
        }

        switch (acao) {
            case 1:
                adicionarTexto("[ATAQUE] " + jogador.nome + " ataca!");
                jogador.atacar(inimigoAtual);
                break;
            case 2:
                adicionarTexto("[DEFESA] " + jogador.nome + " assume posição defensiva!");
                jogador.defesa += 5;
                break;
            case 3:
                if (random.nextInt(100) < 50) {
                    adicionarTexto("[FUGA] " + jogador.nome + " conseguiu fugir da batalha!");
                    salvarProgressoRun();
                    criarProximoInimigo();
                    return;
                } else {
                    adicionarTexto("[FALHA] " + jogador.nome + " não conseguiu fugir!");
                }
                break;
            case 4:
                adicionarTexto("[ATAQUE FORTE] " + jogador.nome + " usa ataque forte!");
                int danoForte = jogador.ataque * 2 + (jogador.armaEquipada != null ? jogador.armaEquipada.getBonusAtaque() : 0);
                inimigoAtual.receberDano(danoForte);
                break;
            case 5:
                adicionarTexto("[ESQUIVA] " + jogador.nome + " se esquiva do próximo ataque!");
                // Implementar lógica de esquiva aqui
                break;
            default:
                adicionarTexto("[ERRO] Ação inválida!");
                return;
        }

        if (inimigoAtual.estaVivo()) {
            adicionarTexto("[CONTRA-ATAQUE] " + inimigoAtual.nome + " contra-ataca!");
            inimigoAtual.atacar(jogador);
        }

        if (acao == 2) {
            jogador.defesa -= 5;
        }

        atualizarPersonagemPanel();
        atualizarInimigoPanel();

        if (!jogador.estaVivo()) {
            tratarGameOver();
        } else if (!inimigoAtual.estaVivo()) {
            tratarVitoria(false);
        } else {
            salvarProgressoRun();
        }
        
        // Voltar ao menu principal se estiver em submenu
        if (emSubmenu) {
            voltarMenuPrincipal();
        }
    }

    private void tratarVitoria(boolean porMagia) {
        String msg = porMagia
                ? "[VITORIA] " + inimigoAtual.nome + " foi derrotado pela magia!"
                : "[VITORIA] " + inimigoAtual.nome + " foi derrotado!";

        adicionarTexto(msg);

        int nivelAntes = jogador.nivel;
        int xpGanho = inimigoAtual.getRecompensaExp();
        int ouroGanho = inimigoAtual.getRecompensaOuro();

        jogador.ganharExperiencia(xpGanho);
        jogador.inventario.adicionarOuro(ouroGanho);

        monstrosDerrotados++;
        runAtual++;

        adicionarTexto("[XP] " + jogador.nome + " ganhou " + xpGanho + " pontos de experiência!");
        adicionarTexto("[OURO] " + jogador.nome + " ganhou " + ouroGanho + " moedas de ouro!");
        adicionarTexto("[RUN] Você avançou para a run " + runAtual + "!");
        adicionarTexto("[STATUS] " + jogador.getXPText());

        if (jogador.nivel > nivelAntes) {
            adicionarTexto("=========================================");
            adicionarTexto("LEVEL UP!");
            adicionarTexto(jogador.nome + " alcançou o nível " + jogador.nivel + "!");
            adicionarTexto("HP +20 | ATK +5 | DEF +3 | MP +10");
            adicionarTexto("=========================================");
        }

        sortearDrop();
        atualizarPersonagemPanel();

        // SAVE FORÇADO LOGO APÓS DERROTAR MOB
        salvarProgressoRun();

        criarProximoInimigo();
    }

    private void sortearDrop() {
        int chance = random.nextInt(100);
        Item drop = null;

        if (chance < 25) {
            drop = ItemFactory.criarPocao("cura");
        } else if (chance < 40) {
            drop = ItemFactory.criarPocao("mana");
        } else if (chance < 50) {
            drop = ItemFactory.criarAcessorio("anel");
        }

        if (drop != null) {
            if (jogador.inventario.adicionarItem(drop)) {
                adicionarTexto("[DROP] Você recebeu: " + drop.getNome());
            } else {
                adicionarTexto("[DROP] Caiu " + drop.getNome() + ", mas o inventário está cheio.");
            }
        }
    }

    private void tratarGameOver() {
        adicionarTexto("[MORTE] " + jogador.nome + " foi derrotado...");
        adicionarTexto("=== GAME OVER ===");

        resetarJogadorAposGameOver();

        String[] opcoes = {"Continuar", "Novo Jogo", "Menu Principal"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "GAME OVER! Seu personagem foi resetado para nível 1.\nDeseja continuar?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 0:
                adicionarTexto("[REVIVE] " + jogador.nome + " foi revivido no nível 1!");
                jogador.vida = jogador.vidaMaxima;
                jogador.mana = jogador.manaMaxima;
                atualizarPersonagemPanel();
                primeiraMissao();
                break;
            case 1:
                novoJogo();
                break;
            default:
                menuPrincipal();
                break;
        }
    }

    private void salvarProgressoRun() {
        if (jogador != null) {
            int idSalvo = saveManager.salvarPersonagem(jogador, runAtual, monstrosDerrotados);
            jogador.id = idSalvo;

            adicionarTexto("[SAVE] Progresso salvo automaticamente!");
            adicionarTexto("[SAVE] ID: " + idSalvo +
                    " | Run: " + runAtual +
                    " | Monstros derrotados: " + monstrosDerrotados +
                    " | Nível: " + jogador.nivel +
                    " | Ouro: " + jogador.inventario.getOuro());
        }
    }

    private void resetarJogadorAposGameOver() {
        runAtual = 1;
        monstrosDerrotados = 0;
        jogador.nivel = 1;
        jogador.experiencia = 0;

        if (jogador.classe.equals("Guerreiro")) {
            jogador.vida = 120;
            jogador.vidaMaxima = 120;
            jogador.ataque = 15;
            jogador.defesa = 10;
        } else if (jogador.classe.equals("Mago")) {
            jogador.vida = 80;
            jogador.vidaMaxima = 80;
            jogador.ataque = 25;
            jogador.defesa = 5;
        } else {
            jogador.vida = 100;
            jogador.vidaMaxima = 100;
            jogador.ataque = 18;
            jogador.defesa = 7;
        }

        jogador.mana = 50;
        jogador.manaMaxima = 50;
        jogador.inventario = new Inventario(20);
        jogador.armaEquipada = null;
        jogador.armaduraEquipada = null;
        jogador.acessorioEquipado = null;

        salvarProgressoRun();
    }

    private void criarProximoInimigo() {
        String[] tiposInimigos = {"goblin", "esqueleto", "vampiro", "dragao", "fogo_elemental", "gelo_elemental"};
        String tipoAleatorio = tiposInimigos[random.nextInt(tiposInimigos.length)];
        int nivelInimigo = Math.max(1, jogador.nivel + random.nextInt(3) - 1);

        inimigoAtual = InimigoFactory.criarInimigo(tipoAleatorio, nivelInimigo);
        atualizarInimigoPanel();
        adicionarTexto("\n=== NOVO DESAFIO ===");
        adicionarTexto("Um " + inimigoAtual.nome + " nível " + nivelInimigo + " aparece!");
    }

    private void abrirInventario() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog inventarioDialog = new JDialog(this, "Inventário", true);
        inventarioDialog.setSize(650, 500);
        inventarioDialog.setLayout(new BorderLayout());

        JTextArea statusArea = new JTextArea(jogador.getStatusCompleto());
        statusArea.setEditable(false);
        statusArea.setBackground(Color.BLACK);
        statusArea.setForeground(Color.GREEN);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        inventarioDialog.add(statusArea, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Item item : jogador.inventario.getItens()) {
            listModel.addElement(item.getNome() + " - " + item.getDescricao() + " (Valor: " + item.getValor() + ")");
        }

        JList<String> itemList = new JList<>(listModel);
        JScrollPane itemScrollPane = new JScrollPane(itemList);
        inventarioDialog.add(itemScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton usarBtn = new JButton("Usar Item");
        JButton equiparBtn = new JButton("Equipar");
        JButton lojaBtn = new JButton("Loja");
        JButton fecharBtn = new JButton("Fechar");

        usarBtn.addActionListener(e -> {
            usarItem(itemList.getSelectedIndex());
            inventarioDialog.dispose();
        });

        equiparBtn.addActionListener(e -> {
            equiparItem(itemList.getSelectedIndex());
            inventarioDialog.dispose();
        });

        lojaBtn.addActionListener(e -> abrirLoja());
        fecharBtn.addActionListener(e -> inventarioDialog.dispose());

        buttonPanel.add(usarBtn);
        buttonPanel.add(equiparBtn);
        buttonPanel.add(lojaBtn);
        buttonPanel.add(fecharBtn);

        inventarioDialog.add(buttonPanel, BorderLayout.SOUTH);
        inventarioDialog.setLocationRelativeTo(this);
        inventarioDialog.setVisible(true);
    }

    private void abrirMagias() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog magiasDialog = new JDialog(this, "Magias", true);
        magiasDialog.setSize(500, 400);
        magiasDialog.setLayout(new BorderLayout());

        JLabel manaLabel = new JLabel("Mana: " + jogador.getMana() + "/" + jogador.getManaMaxima(), SwingConstants.CENTER);
        manaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        magiasDialog.add(manaLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Magia magia : jogador.getMagiasConhecidas()) {
            listModel.addElement(magia.getNome() + " - " + magia.getDescricao() +
                    " (Custo: " + magia.getCustoMana() + " MP, Dano: " + magia.getDano() + ")");
        }

        JList<String> magiaList = new JList<>(listModel);
        JScrollPane magiaScrollPane = new JScrollPane(magiaList);
        magiasDialog.add(magiaScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton usarMagiaBtn = new JButton("Usar Magia");
        JButton fecharBtn = new JButton("Fechar");

        usarMagiaBtn.addActionListener(e -> {
            usarMagia(magiaList.getSelectedIndex());
            magiasDialog.dispose();
        });

        fecharBtn.addActionListener(e -> magiasDialog.dispose());

        buttonPanel.add(usarMagiaBtn);
        buttonPanel.add(fecharBtn);

        magiasDialog.add(buttonPanel, BorderLayout.SOUTH);
        magiasDialog.setLocationRelativeTo(this);
        magiasDialog.setVisible(true);
    }

    private void usarItem(int index) {
        List<Item> itens = jogador.inventario.getItens();

        if (index < 0 || index >= itens.size()) {
            adicionarTexto("Selecione um item válido!");
            return;
        }

        Item item = itens.get(index);

        if (item.getTipo().equals("Consumível")) {
            if (item.getNome().contains("Cura")) {
                jogador.vida = Math.min(jogador.vidaMaxima, jogador.vida + 50);
                adicionarTexto("[ITEM] " + jogador.nome + " usou " + item.getNome() + " e recuperou 50 HP!");
            } else if (item.getNome().contains("Mana")) {
                jogador.regenerarMana(30);
                adicionarTexto("[ITEM] " + jogador.nome + " usou " + item.getNome() + " e recuperou 30 MP!");
            } else {
                jogador.vida = Math.min(jogador.vidaMaxima, jogador.vida + 20);
                adicionarTexto("[ITEM] " + jogador.nome + " usou " + item.getNome() + " e recuperou 20 HP!");
            }

            jogador.inventario.removerItem(index);
            atualizarPersonagemPanel();
            salvarProgressoRun();
        } else {
            adicionarTexto("[ITEM] Este item não pode ser usado diretamente. Tente equipá-lo!");
        }
    }

    private void equiparItem(int index) {
        List<Item> itens = jogador.inventario.getItens();

        if (index < 0 || index >= itens.size()) {
            adicionarTexto("Selecione um item válido!");
            return;
        }

        Item item = itens.get(index);

        if (item.getTipo().equals("Arma")) {
            jogador.armaEquipada = item;
            adicionarTexto("[EQUIPAMENTO] " + jogador.nome + " equipou " + item.getNome() + "!");
        } else if (item.getTipo().equals("Armadura")) {
            jogador.armaduraEquipada = item;
            adicionarTexto("[EQUIPAMENTO] " + jogador.nome + " equipou " + item.getNome() + "!");
        } else if (item.getTipo().equals("Acessório")) {
            jogador.acessorioEquipado = item;
            adicionarTexto("[EQUIPAMENTO] " + jogador.nome + " equipou " + item.getNome() + "!");
        } else {
            adicionarTexto("[EQUIPAMENTO] Este item não pode ser equipado!");
            return;
        }

        atualizarPersonagemPanel();
        salvarProgressoRun();
    }

    private void usarMagia(int index) {
        List<Magia> magias = jogador.getMagiasConhecidas();

        if (index < 0 || index >= magias.size()) {
            adicionarTexto("Selecione uma magia válida!");
            return;
        }

        if (inimigoAtual == null) {
            adicionarTexto("Nenhum inimigo para atacar!");
            return;
        }

        Magia magia = magias.get(index);

        if (!jogador.usarMagia(magia, inimigoAtual)) {
            adicionarTexto("[MAGIA] Mana insuficiente para usar " + magia.getNome() + "!");
            return;
        }

        if (magia == Magia.SHIELD || magia == Magia.BLOCK) {
            jogador.defesa += 5;
            adicionarTexto("[MAGIA] " + jogador.nome + " usou " + magia.getNome() + " e aumentou a defesa temporariamente!");
        } else if (magia == Magia.TELEPORTE || magia == Magia.STEALTH) {
            adicionarTexto("[MAGIA] " + jogador.nome + " usou " + magia.getNome() + " e evitou o próximo golpe!");
            atualizarPersonagemPanel();
            salvarProgressoRun();
            return;
        } else {
            adicionarTexto("[MAGIA] " + jogador.nome + " usou " + magia.getNome() + "!");
            adicionarTexto("[DANO] Causou " + magia.getDano() + " de dano mágico!");
        }

        atualizarInimigoPanel();

        if (!inimigoAtual.estaVivo()) {
            atualizarPersonagemPanel();
            tratarVitoria(true);
            return;
        }

        adicionarTexto("[CONTRA-ATAQUE] " + inimigoAtual.nome + " contra-ataca!");
        inimigoAtual.atacar(jogador);

        if (magia == Magia.SHIELD || magia == Magia.BLOCK) {
            jogador.defesa -= 5;
        }

        atualizarPersonagemPanel();
        atualizarInimigoPanel();

        if (!jogador.estaVivo()) {
            tratarGameOver();
        } else {
            salvarProgressoRun();
        }
    }

    private void comprarItem(Item item) {
        if (jogador.inventario.gastarOuro(item.getValor())) {
            Item comprado = ItemFactory.recriarPorNome(item.getNome());
            if (jogador.inventario.adicionarItem(comprado)) {
                adicionarTexto("[LOJA] " + jogador.nome + " comprou " + item.getNome() +
                        " por " + item.getValor() + " ouro!");
                salvarProgressoRun();
            } else {
                adicionarTexto("[LOJA] Inventário cheio! Não foi possível comprar " + item.getNome());
                jogador.inventario.adicionarOuro(item.getValor());
            }
        } else {
            adicionarTexto("[LOJA] Ouro insuficiente para comprar " + item.getNome() + "!");
        }
    }

    private void adicionarTexto(String texto) {
        textoArea.append(texto + "\n");
        textoArea.setCaretPosition(textoArea.getDocument().getLength());
    }

    private void abrirLoja() {
        if (jogador == null) return;

        JDialog lojaDialog = new JDialog(this, "Loja de Itens", true);
        lojaDialog.setSize(600, 500);
        lojaDialog.setLayout(new BorderLayout());

        JLabel ouroLabel = new JLabel("Ouro: " + jogador.inventario.getOuro(), SwingConstants.CENTER);
        ouroLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lojaDialog.add(ouroLabel, BorderLayout.NORTH);

        JPanel itensPanel = new JPanel(new GridLayout(0, 1));

        Item[] itensLoja = {
                ItemFactory.criarPocao("cura"),
                ItemFactory.criarPocao("mana"),
                ItemFactory.criarArma("espada"),
                ItemFactory.criarArma("arco"),
                ItemFactory.criarArma("cajado"),
                ItemFactory.criarArmadura("couro"),
                ItemFactory.criarArmadura("metal"),
                ItemFactory.criarArmadura("magica"),
                ItemFactory.criarAcessorio("anel"),
                ItemFactory.criarAcessorio("amuleto")
        };

        for (Item item : itensLoja) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.add(new JLabel(item.getNome() + " - " + item.getDescricao() +
                    " (Valor: " + item.getValor() + " ouro)"), BorderLayout.CENTER);

            JButton comprarBtn = new JButton("Comprar");
            comprarBtn.addActionListener(e -> {
                comprarItem(item);
                ouroLabel.setText("Ouro: " + jogador.inventario.getOuro());
            });

            itemPanel.add(comprarBtn, BorderLayout.EAST);
            itensPanel.add(itemPanel);
        }

        JScrollPane itensScrollPane = new JScrollPane(itensPanel);
        lojaDialog.add(itensScrollPane, BorderLayout.CENTER);

        JButton fecharBtn = new JButton("Fechar Loja");
        fecharBtn.addActionListener(e -> lojaDialog.dispose());
        lojaDialog.add(fecharBtn, BorderLayout.SOUTH);

        lojaDialog.setLocationRelativeTo(this);
        lojaDialog.setVisible(true);
    }

    private void abrirHabilidades() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog habilidadesDialog = new JDialog(this, "Árvore de Habilidades", true);
        habilidadesDialog.setSize(700, 600);
        habilidadesDialog.setLayout(new BorderLayout());

        JTextArea habilidadesArea = new JTextArea(jogador.arvoreHabilidades.getStatusCompleto());
        habilidadesArea.setEditable(false);
        habilidadesArea.setBackground(Color.BLACK);
        habilidadesArea.setForeground(Color.GREEN);
        habilidadesArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        habilidadesDialog.add(habilidadesArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton aprenderBtn = new JButton("Aprender Habilidade");
        JButton adicionarPontosBtn = new JButton("Adicionar Pontos");
        JButton fecharBtn = new JButton("Fechar");

        aprenderBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o ramo e índice da habilidade (ex: FORCA,0):");
            if (input != null) {
                String[] partes = input.split(",");
                if (partes.length == 2) {
                    try {
                        RamoHabilidade ramo = RamoHabilidade.valueOf(partes[0].trim().toUpperCase());
                        int indice = Integer.parseInt(partes[1].trim());
                        
                        if (jogador.arvoreHabilidades.aprenderHabilidade(ramo, indice, jogador.nivel)) {
                            adicionarTexto("[HABILIDADE] Habilidade aprendida com sucesso!");
                            salvarProgressoRun();
                        } else {
                            adicionarTexto("[HABILIDADE] Não foi possível aprender esta habilidade!");
                        }
                        habilidadesArea.setText(jogador.arvoreHabilidades.getStatusCompleto());
                    } catch (Exception ex) {
                        adicionarTexto("[HABILIDADE] Formato inválido! Use RAMO,INDICE");
                    }
                }
            }
        });

        adicionarPontosBtn.addActionListener(e -> {
            jogador.arvoreHabilidades.adicionarPontos(1);
            adicionarTexto("[HABILIDADE] 1 ponto de habilidade adicionado!");
            habilidadesArea.setText(jogador.arvoreHabilidades.getStatusCompleto());
            salvarProgressoRun();
        });

        fecharBtn.addActionListener(e -> habilidadesDialog.dispose());

        buttonPanel.add(aprenderBtn);
        buttonPanel.add(adicionarPontosBtn);
        buttonPanel.add(fecharBtn);

        habilidadesDialog.add(buttonPanel, BorderLayout.SOUTH);
        habilidadesDialog.setLocationRelativeTo(this);
        habilidadesDialog.setVisible(true);
    }

    private void abrirMissões() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog missoesDialog = new JDialog(this, "Sistema de Missões", true);
        missoesDialog.setSize(800, 600);
        missoesDialog.setLayout(new BorderLayout());

        JTextArea missoesArea = new JTextArea();
        missoesArea.setEditable(false);
        missoesArea.setBackground(Color.BLACK);
        missoesArea.setForeground(Color.GREEN);
        missoesArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        atualizarMissoesArea(missoesArea);
        missoesDialog.add(missoesArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton iniciarBtn = new JButton("Iniciar Missão");
        JButton completarBtn = new JButton("Completar Missão");
        JButton fecharBtn = new JButton("Fechar");

        iniciarBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o número da missão para iniciar:");
            if (input != null) {
                try {
                    int indice = Integer.parseInt(input);
                    List<Missao> disponiveisAtuais = jogador.gerenciadorMissoes.getMissoesDisponiveis(jogador.nivel);
                    if (indice >= 0 && indice < disponiveisAtuais.size()) {
                        Missao missao = disponiveisAtuais.get(indice);
                        jogador.gerenciadorMissoes.iniciarMissao(missao);
                        adicionarTexto("[MISSÃO] Missão iniciada: " + missao.getNome());
                        salvarProgressoRun();
                        atualizarMissoesArea(missoesArea);
                    } else {
                        adicionarTexto("[MISSÃO] Índice inválido!");
                    }
                } catch (Exception ex) {
                    adicionarTexto("[MISSÃO] Número inválido!");
                }
            }
        });

        completarBtn.addActionListener(e -> {
            List<Missao> ativas = jogador.gerenciadorMissoes.getMissoesAtivas();
            if (ativas.isEmpty()) {
                adicionarTexto("[MISSÃO] Nenhuma missão ativa para completar!");
                return;
            }
            
            String input = JOptionPane.showInputDialog(this, "Digite o número da missão ativa para completar:");
            if (input != null) {
                try {
                    int indice = Integer.parseInt(input);
                    if (indice >= 0 && indice < ativas.size()) {
                        Missao missao = ativas.get(indice);
                        missao.completarObjetivo(0); // Simula completar primeiro objetivo
                        jogador.gerenciadorMissoes.completarMissao(missao);
                        
                        jogador.ganharExperiencia(missao.getRecompensaXP());
                        jogador.inventario.adicionarOuro(missao.getRecompensaOuro());
                        
                        adicionarTexto("[MISSÃO] Missão completada: " + missao.getNome());
                        adicionarTexto("[MISSÃO] Recompensas: " + missao.getRecompensaXP() + " XP, " + missao.getRecompensaOuro() + " ouro");
                        atualizarPersonagemPanel();
                        salvarProgressoRun();
                        atualizarMissoesArea(missoesArea);
                    } else {
                        adicionarTexto("[MISSÃO] Índice inválido!");
                    }
                } catch (Exception ex) {
                    adicionarTexto("[MISSÃO] Número inválido!");
                }
            }
        });

        fecharBtn.addActionListener(e -> missoesDialog.dispose());

        buttonPanel.add(iniciarBtn);
        buttonPanel.add(completarBtn);
        buttonPanel.add(fecharBtn);

        missoesDialog.add(buttonPanel, BorderLayout.SOUTH);
        missoesDialog.setLocationRelativeTo(this);
        missoesDialog.setVisible(true);
    }

    private void atualizarMissoesArea(JTextArea area) {
        StringBuilder texto = new StringBuilder();
        texto.append("=== SISTEMA DE MISSÕES ===\n\n");
        texto.append(jogador.gerenciadorMissoes.getStatusCompleto());
        texto.append("\n=== MISSÕES DISPONÍVEIS ===\n");
        
        List<Missao> disponiveis = jogador.gerenciadorMissoes.getMissoesDisponiveis(jogador.nivel);
        for (int i = 0; i < disponiveis.size(); i++) {
            texto.append(i).append(". ").append(disponiveis.get(i).getNome()).append("\n");
        }

        area.setText(texto.toString());
    }

    private void abrirForja() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog forjaDialog = new JDialog(this, "Sistema de Forja", true);
        forjaDialog.setSize(800, 600);
        forjaDialog.setLayout(new BorderLayout());

        JTextArea forjaArea = new JTextArea(jogador.sistemaForja.getStatusCompleto());
        forjaArea.setEditable(false);
        forjaArea.setBackground(Color.BLACK);
        forjaArea.setForeground(Color.GREEN);
        forjaArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        forjaDialog.add(forjaArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton craftBtn = new JButton("Craft Item");
        JButton coletarBtn = new JButton("Coletar Material");
        JButton fecharBtn = new JButton("Fechar");

        craftBtn.addActionListener(e -> {
            List<ReceitaCraft> disponiveis = jogador.sistemaForja.getReceitasDisponiveis();
            if (disponiveis.isEmpty()) {
                adicionarTexto("[FORJA] Nenhuma receita disponível para craft!");
                return;
            }
            
            StringBuilder texto = new StringBuilder("=== RECEITAS DISPONÍVEIS ===\n");
            for (int i = 0; i < disponiveis.size(); i++) {
                texto.append(i).append(". ").append(disponiveis.get(i).getNome()).append("\n");
            }
            
            String input = JOptionPane.showInputDialog(this, texto.toString() + "\nDigite o número da receita:");
            if (input != null) {
                try {
                    int indice = Integer.parseInt(input);
                    if (indice >= 0 && indice < disponiveis.size()) {
                        ReceitaCraft receita = disponiveis.get(indice);
                        Item item = jogador.sistemaForja.craft(receita);
                        
                        if (item != null) {
                            if (jogador.inventario.adicionarItem(item)) {
                                adicionarTexto("[FORJA] Craft bem-sucedido: " + item.getNome());
                            } else {
                                adicionarTexto("[FORJA] Inventário cheio!");
                            }
                        } else {
                            adicionarTexto("[FORJA] Craft falhou! Tente novamente.");
                        }
                        
                        atualizarPersonagemPanel();
                        salvarProgressoRun();
                        forjaArea.setText(jogador.sistemaForja.getStatusCompleto());
                    } else {
                        adicionarTexto("[FORJA] Índice inválido!");
                    }
                } catch (Exception ex) {
                    adicionarTexto("[FORJA] Número inválido!");
                }
            }
        });

        coletarBtn.addActionListener(e -> {
            jogador.sistemaForja.adicionarMaterialAleatorio();
            adicionarTexto("[FORJA] Material coletado aleatoriamente!");
            forjaArea.setText(jogador.sistemaForja.getStatusCompleto());
            salvarProgressoRun();
        });

        fecharBtn.addActionListener(e -> forjaDialog.dispose());

        buttonPanel.add(craftBtn);
        buttonPanel.add(coletarBtn);
        buttonPanel.add(fecharBtn);

        forjaDialog.add(buttonPanel, BorderLayout.SOUTH);
        forjaDialog.setLocationRelativeTo(this);
        forjaDialog.setVisible(true);
    }

    private void abrirNPC() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        JDialog npcDialog = new JDialog(this, "Sistema de NPCs", true);
        npcDialog.setSize(600, 500);
        npcDialog.setLayout(new BorderLayout());

        JTextArea npcArea = new JTextArea();
        npcArea.setEditable(false);
        npcArea.setBackground(Color.BLACK);
        npcArea.setForeground(Color.GREEN);
        npcArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        // Criar NPCs de exemplo
        NPC mercador = new NPC("João Ferreiro", "Um mercador experiente com olhos afiados.", TipoNPC.MERCADOR);
        NPC guarda = new NPC("Marcos Sentinela", "Um guarda leal que protege a cidade.", TipoNPC.GUARDA);
        NPC mago = new NPC("Elara Arcana", "Uma maga poderosa e sábia.", TipoNPC.MAGO);

        StringBuilder texto = new StringBuilder();
        texto.append("=== SISTEMA DE NPCs ===\n\n");
        texto.append("0. ").append(mercador.getNome()).append(" (").append(mercador.getTipo()).append(")\n");
        texto.append("1. ").append(guarda.getNome()).append(" (").append(guarda.getTipo()).append(")\n");
        texto.append("2. ").append(mago.getNome()).append(" (").append(mago.getTipo()).append(")\n");

        npcArea.setText(texto.toString());
        npcDialog.add(npcArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton falarBtn = new JButton("Falar com NPC");
        JButton missaoBtn = new JButton("Ver Missões");
        JButton fecharBtn = new JButton("Fechar");

        falarBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o número do NPC para falar:");
            if (input != null) {
                try {
                    int indice = Integer.parseInt(input);
                    String dialogo = "";
                    switch (indice) {
                        case 0: dialogo = mercador.getDialogoAleatorio(); break;
                        case 1: dialogo = guarda.getDialogoAleatorio(); break;
                        case 2: dialogo = mago.getDialogoAleatorio(); break;
                        default: dialogo = "NPC não encontrado!"; break;
                    }
                    adicionarTexto("[NPC] " + dialogo);
                } catch (Exception ex) {
                    adicionarTexto("[NPC] Número inválido!");
                }
            }
        });

        missaoBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o número do NPC para ver missões:");
            if (input != null) {
                try {
                    int indice = Integer.parseInt(input);
                    List<Missao> missoes = new ArrayList<>();
                    switch (indice) {
                        case 0: missoes = mercador.getMissoes(); break;
                        case 1: missoes = guarda.getMissoes(); break;
                        case 2: missoes = mago.getMissoes(); break;
                        default: 
                            adicionarTexto("[NPC] NPC não encontrado!");
                            return;
                    }
                    
                    if (missoes.isEmpty()) {
                        adicionarTexto("[NPC] Este NPC não tem missões disponíveis.");
                    } else {
                        StringBuilder textoMissoes = new StringBuilder("=== MISSÕES DO NPC ===\n");
                        for (Missao missao : missoes) {
                            textoMissoes.append("• ").append(missao.getNome()).append("\n");
                        }
                        JOptionPane.showMessageDialog(this, textoMissoes.toString(), "Missões do NPC", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    adicionarTexto("[NPC] Número inválido!");
                }
            }
        });

        fecharBtn.addActionListener(e -> npcDialog.dispose());

        buttonPanel.add(falarBtn);
        buttonPanel.add(missaoBtn);
        buttonPanel.add(fecharBtn);

        npcDialog.add(buttonPanel, BorderLayout.SOUTH);
        npcDialog.setLocationRelativeTo(this);
        npcDialog.setVisible(true);
    }

    private void abrirChefe() {
        if (jogador == null) {
            adicionarTexto("Crie um personagem primeiro!");
            return;
        }

        List<TipoChefe> chefesDisponiveis = FabricaChefes.getChefesDisponiveis(jogador.nivel);
        if (chefesDisponiveis.isEmpty()) {
            adicionarTexto("[CHEFE] Nenhum chefe disponível no seu nível! (Nível mínimo: 5)");
            return;
        }

        StringBuilder texto = new StringBuilder("=== CHEFES DISPONÍVEIS ===\n");
        for (int i = 0; i < chefesDisponiveis.size(); i++) {
            texto.append(i).append(". ").append(chefesDisponiveis.get(i)).append("\n");
        }

        String input = JOptionPane.showInputDialog(this, texto.toString() + "\nDigite o número do chefe para enfrentar:");
        if (input != null) {
            try {
                int indice = Integer.parseInt(input);
                if (indice >= 0 && indice < chefesDisponiveis.size()) {
                    TipoChefe tipoChefe = chefesDisponiveis.get(indice);
                    
                    // Criar chefe
                    ImageIcon imagemChefe = new ImageIcon("img/dragon.png"); // Imagem genérica
                    ChefeEpico chefe = FabricaChefes.criarChefe(tipoChefe, jogador.nivel, imagemChefe);
                    
                    // Substituir inimigo atual pelo chefe
                    inimigoAtual = chefe;
                    atualizarInimigoPanel();
                    
                    adicionarTexto("=== CHEFE ÉPICO ===");
                    adicionarTexto(chefe.getStatusChefe());
                    adicionarTexto("Prepare-se para a batalha mais épica de sua vida!");
                    
                } else {
                    adicionarTexto("[CHEFE] Índice inválido!");
                }
            } catch (Exception ex) {
                adicionarTexto("[CHEFE] Número inválido!");
            }
        }
    }

    private void menuPrincipal() {
        limparTela();
        adicionarTexto("=== MENU PRINCIPAL ===");
        adicionarTexto("1. Novo Jogo");
        adicionarTexto("2. Carregar Jogo");
        adicionarTexto("3. Listar Personagens");
        adicionarTexto("4. Sair");

        String[] opcoes = {"Novo Jogo", "Carregar Jogo", "Listar Personagens", "Sair"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Escolha uma opção:",
                "Menu Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 0:
                novoJogo();
                break;
            case 1:
                carregarJogo();
                break;
            case 2:
                listarPersonagens();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                menuPrincipal();
                break;
        }
    }

    private void novoJogo() {
        criarPersonagem();

        if (jogador != null && jogador.estaVivo()) {
            runAtual = 1;
            monstrosDerrotados = 0;

            jogador.inventario.adicionarItem(ItemFactory.criarPocao("cura"));
            jogador.inventario.adicionarItem(ItemFactory.criarPocao("mana"));
            jogador.inventario.adicionarItem(ItemFactory.criarArma("espada"));

            int idSalvo = saveManager.salvarPersonagem(jogador, runAtual, monstrosDerrotados);
            jogador.id = idSalvo;

            adicionarTexto("Jogo salvo com ID: " + idSalvo);
            adicionarTexto("[STATUS] " + jogador.getStatusText());

            primeiraMissao();
        }
    }

    private void criarPersonagem() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do seu herói:");
        if (nome == null || nome.trim().isEmpty()) {
            jogador = null;
            return;
        }

        ImageIcon[] imagens = {
                new ImageIcon(new ImageIcon("img/cavaleiroimg.png").getImage().getScaledInstance(LARGURA_IMG, ALTURA_IMG, Image.SCALE_SMOOTH)),
                new ImageIcon(new ImageIcon("img/mago.png").getImage().getScaledInstance(LARGURA_IMG, ALTURA_IMG, Image.SCALE_SMOOTH)),
                new ImageIcon(new ImageIcon("img/ladino.png").getImage().getScaledInstance(LARGURA_IMG, ALTURA_IMG, Image.SCALE_SMOOTH))
        };
        String[] classes = {"Guerreiro", "Mago", "Ladino"};
        int[] statsVida = {120, 80, 100};
        int[] statsAtaque = {15, 25, 18};
        int[] statsDefesa = {10, 5, 7};

        JPanel classePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        Border pad = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        classePanel.setBorder(pad);

        final JDialog dialog = new JDialog(this, "Escolha sua classe", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        for (int i = 0; i < 3; i++) {
            JButton botao = new JButton(classes[i]);
            botao.setIcon(imagens[i]);
            botao.setVerticalTextPosition(SwingConstants.BOTTOM);
            botao.setHorizontalTextPosition(SwingConstants.CENTER);

            final int index = i;
            botao.addActionListener(e -> {
                jogador = new Personagem(nome, statsVida[index], statsAtaque[index], statsDefesa[index], classes[index], imagens[index]);
                adicionarTexto("Você escolheu ser um " + classes[index] + "!");
                atualizarPersonagemPanel();
                dialog.dispose();
            });

            classePanel.add(botao);
        }

        dialog.add(classePanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void primeiraMissao() {
        adicionarTexto("\n=== PRIMEIRA MISSÃO ===");
        adicionarTexto("Você entra em uma floresta sombria e encontra um inimigo!");
        adicionarTexto("Prepare-se para a batalha!");
        criarProximoInimigo();
    }

    private void limparTela() {
        textoArea.setText("");
    }

    private void carregarJogo() {
        saveManager.listarPersonagens(textoArea);

        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do personagem para carregar:");
        if (idStr == null || idStr.trim().isEmpty()) {
            adicionarTexto("Carregamento cancelado.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            SaveManager.SaveData saveData = saveManager.carregarPersonagem(id);

            if (saveData != null) {
                jogador = saveData.getPersonagem();
                runAtual = saveData.getRunAtual();
                monstrosDerrotados = saveData.getMonstrosDerrotados();

                adicionarTexto("Personagem carregado com sucesso!");
                adicionarTexto("[STATUS] " + jogador.getStatusText());
                adicionarTexto("[SAVE] Run atual: " + runAtual + " | Monstros derrotados: " + monstrosDerrotados);

                atualizarPersonagemPanel();
                primeiraMissao();
            } else {
                adicionarTexto("Personagem não encontrado!");
            }
        } catch (NumberFormatException e) {
            adicionarTexto("ID inválido!");
        }
    }

    private void listarPersonagens() {
        JTextArea area = new JTextArea();
        saveManager.listarPersonagens(area);
        JOptionPane.showMessageDialog(this, area.getText(), "Personagens Salvos", JOptionPane.INFORMATION_MESSAGE);
        menuPrincipal();
    }

    private void atualizarPersonagemPanel() {
        if (jogador != null) {
            personagemImage.setIcon(redimensionarIcone(jogador.getImagem(), LARGURA_IMG, ALTURA_IMG));

            String html = "<html><div style='text-align:center; font-size:16px; width:300px;'>"
                    + "<b>" + jogador.nome + " (Lv." + jogador.nivel + " " + jogador.classe + ")</b><br><br>"
                    + "HP: " + jogador.vida + "/" + jogador.vidaMaxima
                    + " | MP: " + jogador.mana + "/" + jogador.manaMaxima + "<br>"
                    + "ATK: " + (jogador.ataque
                    + (jogador.armaEquipada != null ? jogador.armaEquipada.getBonusAtaque() : 0)
                    + (jogador.acessorioEquipado != null ? jogador.acessorioEquipado.getBonusAtaque() : 0))
                    + " | DEF: " + (jogador.defesa
                    + (jogador.armaduraEquipada != null ? jogador.armaduraEquipada.getBonusDefesa() : 0)
                    + (jogador.acessorioEquipado != null ? jogador.acessorioEquipado.getBonusDefesa() : 0))
                    + "<br>XP: " + jogador.experiencia + "/" + (jogador.nivel * 100)
                    + " | Ouro: " + jogador.inventario.getOuro()
                    + "</div></html>";

            statusLabel.setText(html);
        } else {
            personagemImage.setIcon(null);
            statusLabel.setText("<html><div style='text-align:center;'>Crie ou carregue um personagem</div></html>");
        }
    }

    private void atualizarInimigoPanel() {
        if (inimigoAtual != null) {
            inimigoImage.setIcon(redimensionarIcone(inimigoAtual.getImagem(), LARGURA_IMG, ALTURA_IMG));

            String html = "<html><div style='text-align:center; font-size:16px; width:300px;'>"
                    + "<b>" + inimigoAtual.nome + "</b><br><br>"
                    + "HP: " + inimigoAtual.vida + "/" + inimigoAtual.vidaMaxima + "<br>"
                    + "ATK: " + inimigoAtual.ataque + " | DEF: " + inimigoAtual.defesa
                    + "</div></html>";

            inimigoStatus.setText(html);
        } else {
            inimigoImage.setIcon(null);
            inimigoStatus.setText("<html><div style='text-align:center;'>Nenhum inimigo no momento</div></html>");
        }
    }
}

public class RpgGraficoNovo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogoGrafico::new);
    }
}   