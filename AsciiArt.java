import java.util.HashMap;
import java.util.Map;

public class AsciiArt {
    private static final Map<String, String[]> asciiArts = new HashMap<>();
    
    static {
        // Arte ASCII para Cavaleiro
        asciiArts.put("cavaleiro", new String[]{
            "                                                       ;;;;                                          ",
            "                                                       ;;;;   :::::::::::                           ",
            "                                                       ;;;;   :::::::::::                           ",
            "                                                   ;;;;;;;;;;;+++;::;+++;::::::;                    ",
            "                                             +++;;;;;;;;;;;;;;+++;;;;;;;;;;;:::;                    ",
            "                                            ++++;;;;;;;;;;;+++;;;;+++:::;+++:::;                    ",
            "                                         ++++;;;;;;;;;;++++;;;+++;::;+++;::::::;                    ",
            "                                         ++++;;;;;;;;;;+++;;;;;;;;;;;;;;;;;;:::;                    ",
            "                                         ++++;;;;;;;+++;;;;+++;::;+++:::;+++:::;                    ",
            "                                         ++++;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:::;                    ",
            "                                         ++++;;;;;;;+++;;;;;;;;;;;+++;;;;;;;;;:;                    ",
            "                                         ++++;;;:::;$$$;:::::::::x$$$::::                           ",
            "                                         ++++;;;:::;$$$;:::::::::+$$$:::::::                        ",
            "                                         ++++;;;:::;$$$::::::::::+$$X:::::::                        ",
            "                                         ++++;;;:::::::...........::::::::::                        ",
            "                                            ++++;:::::::::::::::::::::::::::                        ",
            "                                            ++++;:::::::::::::::::::::::::::                        ",
            "                                                :::::::::::::::::::::::::                           ",
            "                                            ;;;;;;;;;;;::::::::::::::+++++++                        ",
            "                                            ;;;;;;;;;;;::::::::::::::+++++++                        ",
            "                                            ;;;;++++;;;;;;;;;;;;;;:::+++++++                        ",
            "                                         ;;;;+++;;;;+++;;;;;;;;;;;:::xxx+++++++++++                 ",
            "                                         ;;;;+++;;;;+++;;;;;;;;;;;:::xxx+++++++++++                 ",
            "                                  xxxx:::::::;;;;  ;;;;;;;;;;;;;;;xxx;;;;::::::;xx+++++             ",
            "                                  xxxx;;;::::      ::::::::::::::::::;;;;::::::;+x+++++             ",
            "                                  xxxx;;;::::      ::::::::::::::::::;;;;::::::;xx+++++             ",
            "                               ;;;;;;;xxxxxxx      ;;;;;;;;::::::;xxx;;;;;;;;;;+xxxxxxx             ",
            "                           ;;;;;;;;;;;+         ++++++++      xxxxxxxxxxxxxxxxxx++xxxxx             ",
            "                           ;;;;;;;;;;;          ++++++++      xxxxxxxxx+xx+xx+xxxx+xxxx             ",
            "                        ;;;;;;;;;;;             ;;;;;;;;      +++++++xxxxxxxxxx++++                 ",
            "                    ;;;;;;;;;;;;         &&&&&&&x++++++X&&&&&&xxxxxxx&&&&&&&&&&$                    ",
            "                    ;;;;;;;;;;;          &&&&&&&x++++++$&&&&&&xxxxxxx&&&&&&&&&&&                    ",
            "             &&&&&&&x;;;;;;+&&&&&&&&&&&&&&&&&&&&+;;;;;;$&&&&&&++++++++++x&&&&&&&&&&                 ",
            "              &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&                 ",
            "                 &&&&&&&&&&&&&&          &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&                 "
        });
        
        // Arte ASCII para Dragão
        asciiArts.put("dragon", new String[]{
            "                                                                                                    ",
            "                                                                        &&&&&&                      ",
            "                     &&XX&&&&                                    &&&&XXX$&&&&&                      ",
            "                        &&$&&&&&                         &&&&&$$$$&&&xx$&&                          ",
            "                         &X$&$&&&&&                  &&&&&$$$$x+++;+x&&&                              ",
            "                            &&&$&&&&&           &&&$$&&&$x+;;;;;;;+x&&&                              ",
            "                          &xx&&&$&&$$$&&   &&&XXXX$&&&&&&&&&&&&x;;;;;x&&      ...                   ",
            "                                 &$$&$$$&&&$&&&&&&&$&&xxxxxxxxxxXXXXXX&&&&&   ..                    ",
            "                                  &&$&&$$$&&&   &$$&&xx&&$x;;;;;;;;;;+x&&$$$$$;.                     ",
            "                                  &&&&xx&&&&&&&&&$$&&&;++xx$$;;;;;;+x&:........                     ",
            "                              .&&&XX&&$&&&$$&&&$XX$&&x&+;;;+xX&x;;+x&...                            ",
            "                            &&&x&&$$$XX&&$$$$XXX$&&&;;x&x;;;;;xx&$+x&..                             ",
            "                        &&xxx&$$X&&$&&$$$XXXX$$&&&$;;;;x$X;;+xxxxx$&&..                             ",
            "                       &&&&&&&&&&$$$$&X$X&&$$X&&&&&XXX++x$X$&x:...;&$$x;;x&&&                       ",
            "                     && &&X$&$$&&&&&&  &&&&$x+&$$$$$$&&&x$&..        &&&&$;;...                     ",
            "                     &&x&$XXXXX$&&        &&&&&+$$$&X$$&&&           &&&&    ...                    ",
            "                      &&XXX&&&X$$&&        &&&&&&$$$&&X&&            &&&&     ..                    ",
            "                        &$&&$X$X&&    &&$&&XX$XX$&&$$$&x...           &&&&                          ",
            "                        &$$$&&$&&&&&&&&&&$XX$$&$XXX$&&&....           &&&&                          ",
            "                         &&&& &&&&$$&$XX&&&$&&&$$$$&&$X&&            &&&&                           ",
            "                                      &&&$X$&  &&&&&&$$$$$&        &&$$&&    ..                     ",
            "                          &&&xx&&&&&&&$&&&x;& ...x&$xx&&&&$X$&&&&&&$&x&&     ...                    ",
            "                          &&$& &&&&&&&&&& &&   .....:&&$$$$$$$$$$$$&&        ...                    ",
            "                                      &&&&                &&&&&&&            ....   ..              ",
            "                                                                               ..                   ",
            "                                                                                   ..               ",
            "                                                                                    ....            "
        });
        
        // Arte ASCII para Esqueleto
        asciiArts.put("esqueleto", new String[]{
            "                                                                                                    ",
            "                                             +++;;;;;;;;;;;;;                                        ",
            "                                         xxxxxxx;;;;;;;;;;;;;xxxx     ..:.      .:.:                ",
            "                                         +xxxxxx+;;;;;;:;;;;;xxxx;;;  ;;;;   +++::::                ",
            "                                         ++++;;;+xxxxxx:::xxx;;;;;;;  XXXX   +;+:::.                ",
            "                                         ;;;;;;;xXX$$$X::;$$$;;;;::;;;+XXX;;;::::..:                ",
            "                                         +;;;;;;;;;;;;;:::;;;;+;::::;;xXXX++;:::::::                ",
            "                                         ;;;;;;;:::::::::::::;;;:..:  XXXX   ::.::::                ",
            "                                             ;;;+++;;;;:::;;;::::.::  XXXX   :::::::                ",
            "                                                $$$x+++;++   :.:::::  XXXX   :::::::                ",
            "                                       &&&$$$$$$$$$Xxxxxxx$$$:::::::  XXXX   :::::::                ",
            "                                      &&&&$$$$$$$$$$$$$$$$$$$::::     XXXX      ::::                ",
            "                                   $$$$;;+$$$$$$$$$$$$$$$$$$$::::     XXXX      ;;;+                ",
            "                                   +++x$$$xxx$$$$$$$$$$$$$$$$xxxx     XXXX      ;+++                ",
            "                                   +++x&&$xxx$$$$$$$$$$$$$   xxxx     XXXX                          ",
            "                                   xxxx&&$xxx&&&xxxX&&$xxx      xxxx;;+XXX                          ",
            "                                   xxxx$$$xxx$$$xxxx$$$xxx      xxxx++xXXX                          ",
            "                                   xxxx$$$&&$xxxxxxxxxx$$$            XXXX                          ",
            "                                   ;+;x$$$$$$&&&$XX$$$$               XXXX                          ",
            "                                    ++X$$$$$$XX$xxxx$$$$$$            XXXX                          ",
            "                                      $$$$$$$XXXxxxx$$$$$$            XXXX                          ",
            "                                         $$$$XXXx++xXXXXXX            XXXX                          ",
            "                                         xxxx         xxxx            XXXX                          ",
            "                                         xxxx         xxxx            XXXX                          ",
            "                                         xxxx         xxxx            XXXX                          ",
            "                                         ++++         ++++            XXXX                          "
        });
        
        // Arte ASCII para Goblin
        asciiArts.put("goblin", new String[]{
            "                                                                                                    ",
            "                                             &&&&&&&&&&                                              ",
            "                                           &&&&&&&&&&&&&&                                            ",
            "                                           &&&&&$$$$&&&&&                                            ",
            "                                           &&&&&&&&&&&&&&                                            ",
            "                                         &&$$$$$$$$$$$$$$&&      &&&$$                              ",
            "                           &&&&&&&&&&&&&&$$xxxxxxxxxxxxxX$$&&&&&&&&&$$$$$                           ",
            "                            &&&&&&&$$$$xxXXxxxxx&&xxxxxxX&&$$$$&&&&&$$$$$                           ",
            "                              &&&&&&&&&&$XXxxxxx&&xxxxxx$&&XX&&&&&&&$$$$$                           ",
            "                                  &&&&&&&&&Xxxxxxxxxxxxxxxxxxxx$&&&&$$$$                            ",
            "                                         &&$XxxxxxxxxxxxX&&&&&&&&$$$$$                              ",
            "                                         &&$XxxxxxxxxxxxX&&  &&&&$$$$$                              ",
            "                                         &&$XXXXXXXXXXXX$&&  &&&&$$$                                ",
            "                                          &&&&&&&&&&&&&&&&   &&$$$$$                                ",
            "                                    &&&$$$$&&&XXXXXXXXXX$&&  &&&&&                                 ",
            "                                  &&&$$$$$$$$$XXXXXXxxxxxX$&&&&&&&                                 ",
            "                                &&$$$&&$$$$$$$$$XXxxxxxxxXXXXXX&&&                                 ",
            "                                &&$$$&&$$$$$$$$$XXxxxxxxX&&&&&&&&&                                 ",
            "                                &&$$$&&&&$$$$$$&&&++;;$&&&&                                      ",
            "                                  &&&$$$$$$$$&&&&&$$$$$$&&&                                      ",
            "                                    &&&&&$$&&&&&&&&&$$$$&&&                                      ",
            "                                           &&&&&&&&&&&&$&&&                                      ",
            "                                           &&&&&&&  &&&&&&&                                      "
        });
        
        // Arte ASCII para Elemento de Fogo
        asciiArts.put("fireelement", new String[]{
            "           /\\",
            "          /  \\",
            "         /    \\",
            "        /      \\",
            "       /        \\",
            "      /          \\",
            "     /            \\",
            "    /              \\",
            "   /                \\",
            "  /                  \\",
            " /                    \\",
            "/____________________\\",
            "      \\    /",
            "       \\  /",
            "        \\/"
        });
        
        // Arte ASCII para Elemento de Gelo
        asciiArts.put("iceelement", new String[]{
            "     ____",
            "    /    \\",
            "   /      \\",
            "  /        \\",
            " /          \\",
            "|            |",
            "|            |",
            "|            |",
            "|            |",
            " \\          /",
            "  \\        /",
            "   \\      /",
            "    \\____/",
            "    |    |",
            "    |    |",
            "    |____|"
        });
        
        // Arte ASCII para Ladino
        asciiArts.put("arqueiro", new String[]{
            "                                                                                                    ",
            "                                                                                                    ",
            "                                                                                                    ",
            "                                     +xx+                                                           ",
            "                                     +xx+                                                           ",
            "                                     +xx+             ;++++++XXXXXXXXXXXXXx                         ",
            "                                     +xx+             +++++++XXXXXXXXXXXXXx.                        ",
            "                                  xxxxxx+          +++++++XXXXXXXXXXXXXXXXXXXX                      ",
            "                               .:.xxx+++;          +++++x+XXXXXXXXXXXXXXXXXXX$:::                   ",
            "                               xxxxx+:             +++XXXXXXXXXXX$$$X$$$$$$$$$XXX.                  ",
            "                               xxx+++:             +++XXXXXXXXXX$$$$$$$$$$$$$$XXX:::.               ",
            "                               xxx                .$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$XXX;               ",
            "                           ;+++xxx                 ;;;$&&$;;;;::::::::::::::::$$$$$$+               ",
            "                           ;xxxxxx                 :::$&&$:::::::::::::.      $$$$$$+               ",
            "                        XXXXXXX                    ::::::::::;++:...:::.                            ",
            "                        XXXXXXX                    ::::::::::;+;:...:::.                            ",
            "                        XXXXXXX                    &&&&&&$:::++;xxx+...                             ",
            "                        XXXXXXX                    &&&&&&$:::;++xxx+...                             ",
            "                 ....   :::::::++++++++++++++++++++:::::::+++++;xxxXxxx:                            ",
            "                 ....   :::::::;++++;;;+;+;+;+;+;:::::::;;;+++xXXXxXx:                            ",
            "          .......$&$$$$$$&$&$$$$$$$$$$$$$$$$$$$$&$&$&$&$&$&:::;:;;XxXxxxXxXX                      ",
            "           ......xXXXXXXX$X$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$XXX:::;;;;xxxxxxxxxx:::;:::               ",
            "                 ....   ;+++XXX++++++++++++++++++++XxXxXx+::::::++++++++++++++xxxXXX;               ",
            "                   ..   XXXXXXX............:...:+++xXxXxxXxx++;++++++++++++++xxxxxxXXXX;               ",
            "                        XXXXXXX                :+x+XxXxxxxxxX++++++++++++++xxxxxxXXX;               ",
            "                        XXXXXXX                :x+++x+xXxxxxxxxxXXXXXXXXXXXXXXXXX....               ",
            "                        XXXXXXX                :++++++xXXXXxXxXxXXXXXXXXXXXXXXXXX                   ",
            "                           ;xxX                :++++++xXxXxXxXxXxXXXxXXXX$X                         ",
            "                           ;xxx                :++++++XxxxxxxxxxXxxxXXxX$$X                         ",
            "                           ;xxx                    +++++++$$$$$$$$$$X$$$$$X                         ",
            "                           ;xxx                    ++++++x$$$$$$$$$$$$$$$$X                         ",
            "                           ;xxxxxx                 $$$$XXX$X$XXX$$$$$$$:                            ",
            "                           :;;;xxx::;.             $$$$$$$$$$$$X$$$$$$$:                            ",
            "                               xxxxxx:            .$$$$X$$$$$$$$$$$$$$$:                            ",
            "                               ;;;xxx:         .;;;XXXXXXXXXXXXXXXXXXXX:                            ",
            "                                  xxx:         :xxxxxxxxxxxxxxxxxxxxxxx:                            ",
            "                                            +XXxxxxxxxXXXXXXXxxxxxxxxxx:                            ",
            "                                            +xxxxxxxxxXXXXXXXxxxxxxxxxx:                            ",
            "                                            +xxxxxxXXXXXXx  .xxxxxxxxxxXxx+                         ",
            "                                            +xxxxxxXXXXXXX   XxxXxxxxxxxxx+                         ",
            "                                            +xxXXXXXXX:         +xxxxxxxxx+                         ",
            "                                            +xxXXXXXXX:         +xxxxxxxxx+...                      ",
            "                                            +XXX$$$                :$$$$$$$$$$                      ",
            "                                            +XXX$$$                :$$$$$$$$$$                      ",
            "                                            +XXX$$$                    X$$$$$$                      ",
            "                                            +XXX$$$                    ::;;$$$xxx                   ",
            "                                        xXXX$$X                       :$$$$$$                   ",
            "                                    :XXXXXXXX$$                    xXXXXXXXXX                   ",
            "                                    .XXXXXXX$$$                    +XXXXXXXXX                   ",
            "                                    :X$X$X$$$$$                    x$$$$$$$$$                   ",
            "                                    :$$$$$$$$$$                    x$$$$$$$$$                   ",
        });
        
        // Arte ASCII para Mago
        asciiArts.put("mago", new String[]{
            "                           $$$&&                                                                    ",
            "                        $$&&++&&                &&&&&&&&                                            ",
            "                        $$&&XX&&              &&&&&&&&&&&&                                          ",
            "                        &&&&&&&&           &&&&&&&&&&&&&&&                                          ",
            "                          &&&&             &&&&&&&&&&&&&&&                                          ",
            "                          $$&&             &&&&&&&&&&&&&&&                                          ",
            "                          $$&&             &&&&&&&&&&&&&&&                                          ",
            "                          &&&&             &&&&&++++&&&&&&&&                                        ",
            "                          &&&&              &&&&&&xx&&&&&&&&&&&&                                    ",
            "                          $$&&&&          &&&&&&&&xxxx&&&&&&&&&&&&                                 ",
            "                           $$$&&          &&&&&&&&&&&&&&&&&&&&&&&&                                 ",
            "                           $$$&&        &&$$&&&&&&&&&&&&&&&&&&&&&&&&                                ",
            "                           &&&&&        &&$$$$&&$$$$$$$$$$&&$$&&&&&&                                ",
            "                           &&&&&        &&$$&&$$&&&&&&&&&&$$$$&&&&&&    ;;                          ",
            "                           ;;;xxxx      $$$$&&$$&&$$$$$$&&$$$$&&&&&&  ;;$&                        ",
            "                           +;+xx+;$$$$$$$$$$&&$$&&$$$$$$$$++++;;;;$$++&&&&                    ",
            "                           XXXXX+;$$$$&&&&&&&&$$$$&&&&xxxx&&&&&&&&xx&&&&                      ",
            "                           &&&&&  &&&&  &&&&++$$$$$$++$$&&&&&&&&xxxxxx                        ",
            "                           &&&&&        &&:;;;$$$$$$$$&&&&&&&&xx+;;+&&                      ",
            "                           $$$&&      &&&&:;;;$$$$$$$$$$$$$$$$+;+;&&&&                    ",
            "                           $$$&&      &&&&;;&&&&&&&&&&&&&&&&&&&&&&&&&&&&                      ",
            "                              &&&&    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&                      ",
            "                              &&&&    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&                      ",
            "                              &$&&    &&&&&&&&$$&&&&$$$$$$&&&&&&&&&&&&&&&&                      ",
            "                              &&&&    &&&&&&&&$$$$$$$$$$$$&&&&&&&&&&&&&&&&                      ",
            "                                &&    &&&&&&$$$$$$$$$$&&$$&&&&&&&&&&&&&&&&                      ",
            "                                &&    &&&&&&$$$$$$$$$$&&$$$$&&&&&&&&&&&&&&                      ",
            "                                &&    &&&&&&$$$$$$$$&&&&$$$$$$$$&&&&&&&&&&                      ",
            "                                &&      &&&&$$$$$$$$&&&&$$$$$$$$&&&&&&&&&&                      ",
            "                                  &&    &&&&&&$$$$&&&&&&&&$$$$&&&&&&&&&&&&                      ",
            "                                  &&    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&                            ",
            "                                  &&      &&&&&&&&&&&&&&&&&&&&&&&&&&&&                              ",
            "                                  &&            &&&&          &&&&                            ",
            "                                  &&          &&&&&&       &&&&&&&                            "
        });
        
        // Arte ASCII para Vampiro
        asciiArts.put("vampiro", new String[]{
            "                                                                                                    ",
            "                                               &&&&&&                                                ",
            "                                           &&&&&$$$$&&&$                                            ",
            "                                     &&$$&&$X$$$$xx$$$$X&&&x$&&                                      ",
            "                                    &$xxx$$xxx$XxxxxX$xxx$$xxX$&                                    ",
            "                                   &$$xx$$&&&&$$$$$$$$&&&&$$$$$&&                                    ",
            "                             :     &$$$$&x;.:.+X&$$&$x...;x&$$$$&     ::                            ",
            "                           &&::xx;&&$$$$:::::::.:$$;..::::::$&$$& $$X::$&                           ",
            "                            &&:xxx;&&&$::::::::..:..:..:::::.&&&&::;x:&&                            ",
            "                              ::;+x::x&::::::&&x:..:x&&&;:::.&x:xx;:;&&                             ",
            "                              &;;;;xx;::::x&&&::::::::.$&x;&:xxx;;::;&                               ",
            "                               &$x:;xxx:;:;$$;:..:.:..xX$x;;:xxx;;:;&                               ",
            "                          &&&$$$$$&xxxx:::;x+.::..:::::::;;;::+Xx$$$$$x&&&                          ",
            "                         &$XXX$$$$$&&&$;:::;:;::;;::::;:;:::;&&&$$$$$$$$X$&                         ",
            "                            &&XXXX$$$$$&&x;::&&Xx$$xx&&:::x&$$$$$$XXXx&&                            ",
            "                                &&&XX$$$$&&&::::;::;;::;$&$$$XXXX&&&                                ",
            "                                   &&$$XX&$+;Xx$&xX&&$X&xX&X$&&&&                                    ",
            "                                 &&$&&&&&x+;:x;x&&x&$x;:;x&&&&&$$&&                                 ",
            "                                &&&&$$$$x+;...$$xxxx$&:::;;;$$$$&$&&                                ",
            "                               &&$$$$$X;;:::::X$Xxxxxx:::::;;$$$&$$&&                               ",
            "                               $$$$$$$&&+:::::::;&$;:::::::;;$$XXX$$$                               ",
            "                              &&$$$$$&$;;::::::;x::x+::::::;;$$$XX&&$&                              ",
            "                            &&$$&&$$$&&&+;;::::;+:&x;::::::+&$$$$$&&$$                              ",
            "                           &&$&+;;xx&&&&&x++;::;+:;+;::;;;;+&&&&++;;;&$&&                           ",
            "                          &$$&&;..:x+X&&&&&&xxxxX&&Xxxxx&&&&&xxx;;:::&$&&&                          ",
            "                          X$&&&;:;+$x$&&$$$$$$$$XX$$$$$$$$$$&xxx&;:::&&$$$&                         ",
            "                         X$&&&&&x;;;&&&&$$X$$&$X$$$$$$$$$$$$&&&&+;;x&&&$$$$                         ",
            "                       X$$&&&&&&&&&&&&&&$$$$XX$$&&&&$$$$$$$$&&&&&&&&&&&&&$$&&&                       ",
            "                      X$$&&&&&&&&&&&&&&&$$$$$$$$&&&&$$$$$$$$&&&&&&&&&&&&&&&$$$                       ",
            "                      &&&&&&&&&&&&&&&&&&$$$$$$&&&&&&&&$$$$$&&&&&&&&&&&&&&&&&&&&                     ",
            "                     $&&&&&&&&&&&&&&&&&&&$$$$$&&&&&&&&$$$$$$&&&&&&&&&&&&&&&&&&$                     ",
            "                    &$$&&&& &&&&&&&  &&&&$$$$$$&&&&&&&$$$$$&&&&  &&&&&&& &&&$$$&&                   ",
            "                     $&&       &&       &&&&&$$&&  &&&$$$$&&       $&        &&                     ",
            "                                      $&$$&&&$&&&  &&&$$$$$$&$                                      ",
            "                                    &&x$$$$$$$$&&  &&$$$$$$$$&&&                                    ",
            "                                     &&&&&&&&&&&    &&&&&&&&&&&                                    "
        });
    }
    
    public static String[] getAsciiArt(String nome) {
        // Normalizar nome para lowercase
        String chave = nome.toLowerCase().replace(".png", "").replace("_", "");
        
        // Mapeamentos especiais
        switch (chave) {
            case "cavaleiroimg":
                chave = "cavaleiro";
                break;
            case "dragão":
                chave = "dragon";
                break;
            case "arqueiro":
                chave = "arqueiro";
                break;
            case "fireelement":
                chave = "fireelement";
                break;
            case "iceelement":
                chave = "iceelement";
                break;
        }
        
        return asciiArts.getOrDefault(chave, new String[]{
            "     IMAGEM NÃO ENCONTRADA",
            "     ====================",
            "           " + nome,
            "     ===================="
        });
    }
    
    public static void imprimirAsciiArt(String nome, int delayMs) {
        String[] arte = getAsciiArt(nome);
        
        for (String linha : arte) {
            System.out.println(linha);
            
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    public static void imprimirTextoDevagar(String texto, int delayMs) {
        for (char caractere : texto.toCharArray()) {
            System.out.print(caractere);
            System.out.flush();
            
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println();
    }
    
    public static void imprimirTextoDevagar(String texto) {
        imprimirTextoDevagar(texto, 30); // 30ms padrão
    }
    
    public static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: imprimir 50 linhas vazias
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
