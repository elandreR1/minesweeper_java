package resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Assets {
    public static ImageIcon flagIcon;
    public static ImageIcon mineWipedIcon;
    public static ImageIcon mineUnflaggedIcon;

    public static ImageIcon SunglassesSmiley;
    public static ImageIcon DeathSmiley;
    public static ImageIcon InGameSmiley;
    public static ImageIcon OnClickSmiley;
    public static ImageIcon clock;
    public static ImageIcon topPanelMine;
    public static ImageIcon wrongFlag;

    static {

        //le drapeau mal placé responsable de la mort
        try {
            BufferedImage mauvaisFlag = ImageIO.read(Assets.class.getResource("WrongFlag.png"));
            Image scaled = mauvaisFlag.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            wrongFlag = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de WrongFlag.png : " + e.getMessage());
            wrongFlag = null;
        }

        //la mine du topPanel
        try {
            BufferedImage TPM = ImageIO.read(Assets.class.getResource("TopPanelMine.png"));
            Image scaled = TPM.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            topPanelMine = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de TopPanelMine.png : " + e.getMessage());
            topPanelMine = null;
        }


        
        //l'horloge du topPanel
        try {
            BufferedImage horloge = ImageIO.read(Assets.class.getResource("Clock.png"));
            Image scaled = horloge.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            clock = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de Clock.png : " + e.getMessage());
            clock = null;
        }

        //le smiley lunettes de soleil
        try {
            BufferedImage GameWonSmiley = ImageIO.read(Assets.class.getResource("Sunglasses.png"));
            Image scaled = GameWonSmiley.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            SunglassesSmiley = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de Sunglasses.png : " + e.getMessage());
            SunglassesSmiley = null;
        }

        //le smiley mort
        try {
            BufferedImage GameLostSmiley = ImageIO.read(Assets.class.getResource("DeadSmiley.png"));
            Image scaled = GameLostSmiley.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            DeathSmiley = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de DeadSmiley.png : " + e.getMessage());
            DeathSmiley = null;
        }

        //le smiley classique
        try {
            BufferedImage GameOnGoingSmiley = ImageIO.read(Assets.class.getResource("SmileInGame.png"));
            Image scaled = GameOnGoingSmiley.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            InGameSmiley = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de SmileInGame.png : " + e.getMessage());
            InGameSmiley = null;
        }

        //le smiley stressé onclick
        try {
            BufferedImage NerveuxSmiley = ImageIO.read(Assets.class.getResource("SmileyOnClick.png"));
            Image scaled = NerveuxSmiley.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            OnClickSmiley = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de SmileyOnClick.png : " + e.getMessage());
            OnClickSmiley = null;
        }

        // le flag
        try {
            BufferedImage Flag = ImageIO.read(Assets.class.getResource("Flag.png"));
            Image scaled = Flag.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            flagIcon = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de Flag.png : " + e.getMessage());
            flagIcon = null;
        }

        // la mine explosée
        try {
            BufferedImage Mine = ImageIO.read(Assets.class.getResource("Mine.png"));
            Image scaled = Mine.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            mineWipedIcon = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de Mine.png : " + e.getMessage());
            mineWipedIcon = null;
        }

        //la mine non explosée après défaite
        try {
            BufferedImage MineUnflagged = ImageIO.read(Assets.class.getResource("MineUnflagged.png"));
            Image scaled = MineUnflagged.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            mineUnflaggedIcon = new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Assets : Erreur lors du chargement de MineUnflagged.png : " + e.getMessage());
            mineUnflaggedIcon = null;
        }

        

    }
}
