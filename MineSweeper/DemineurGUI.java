import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import resources.Assets;

public class DemineurGUI extends JFrame implements GameResetListener {
    /**
     * POUR CHOISIR LES PARAMETRES
     * - tailleX        -> NOMBRE DE LIGNES
     * - tailleY        -> NOMBRE DE COLONNES
     * - nbMinesTotal   -> NOMBRE DE MINES PLACEES ALEATOIREMENT DANS LA GRILLE
     */ 
                                            //config standard
    private int tailleX = 16;               //16                         
    private int tailleY = 30;               //30    
    private int nbMinesTotal = 99;          //99

    /**
     * POUR LANCER LE DEMINEUR, CLIQUER SUR RUN 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DemineurGUI::new); 
        
    }

    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private GridPanel gridPanel;
    private TopPanel topPanel;
    private Grille grille;
    private GameControllerGUI controller;

    // Timer
    private Timer timer;
    private int secondes = 0;
    private boolean timerStarted = false;

    public DemineurGUI() {

        //trop de mines
        if (nbMinesTotal > (tailleX * tailleY - 10)) {
        JOptionPane.showMessageDialog(
            null,
            "Le nombre de mines doit être inférieur à " + (tailleX * tailleY - 9) + " pour assurer une bonne génération de la grille.",
            "Erreur de configuration",
            JOptionPane.ERROR_MESSAGE
        );
        System.exit(1); //mieux qu'un return;
        } 

        //pas assez de mines
        if(nbMinesTotal < 1){
           JOptionPane.showMessageDialog(
            null,
            "Le nombre de mines doit être supérieur à 0 pour assurer une bonne génération de la grille.",
            "Erreur de configuration",
            JOptionPane.ERROR_MESSAGE
        ); 
        System.exit(1);
        }

        setTitle("Java Minesweeper | Made in Courbevoie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialisation de l'interface
        grille = new Grille(tailleX, tailleY, nbMinesTotal);
        topPanel = new TopPanel(this);
        topPanel.setGameResetListener(this);
        topPanel.setMinesRestantes(nbMinesTotal);

        gridPanel = new GridPanel(grille, topPanel, this);
        controller = gridPanel.getController();

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // Ajout du listener pour la touche espace
        installSpaceKeyListener();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        timer = new Timer(1000, e -> {
            if (secondes < 999) {
                secondes++;
                topPanel.setTemps(secondes);
            }
        });
    }
    
    
    private void installSpaceKeyListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gridPanel == null || grille == null) return false;

                    PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    if (pointerInfo == null) return false;

                    Point screenPoint = pointerInfo.getLocation();

                    for (int i = 0; i < grille.getTailleX(); i++) {
                        for (int j = 0; j < grille.getTailleY(); j++) {
                            CelluleButton btn = gridPanel.getCelluleButtonByIndex(i, j);
                            Point btnScreen = btn.getLocationOnScreen();
                            Rectangle bounds = new Rectangle(btnScreen, btn.getSize());
                            if (bounds.contains(screenPoint)) {
                                controller.actOnSpaceClick(i, j);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void resetGame() {
        System.out.println("Reset de grille...");
        topPanel.getSmiley().setIcon(Assets.InGameSmiley);
        remove(gridPanel);

        grille = new Grille(tailleX, tailleY, nbMinesTotal);
        gridPanel = new GridPanel(grille, topPanel, this);
        controller = gridPanel.getController(); // MAJ du controller

        add(gridPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        secondes = 0;
        topPanel.setTemps(0);
        timer.stop();
        timerStarted = false;
    }

    public Timer getTimer() {
        return timer;
    }

    public void startTimer() {
        timerStarted = true;
        timer.start();
    }

    public void stopTimer() {
        timerStarted = false;
        timer.stop();
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(boolean newState) {
        timerStarted = newState;
    }

    public int getNbMines(){
        return grille.getNbMinesTotal();
    }

    

    
}
