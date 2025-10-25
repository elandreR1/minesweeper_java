import javax.swing.*;
import resources.Assets;
import java.awt.*;


public class TopPanel extends JPanel {
    private final JLabel minesLabel;
    private final SmileyButton smiley;
    private final JLabel timerLabel;
    private DemineurGUI demineurGUI ;
    private GameResetListener resetListener;




    public TopPanel(DemineurGUI demineurGUI) {
        this.demineurGUI = demineurGUI;
        setLayout(new BorderLayout());

        //minesLabel = new JLabel("💣 10");
        minesLabel = new JLabel("");
        minesLabel.setIcon(Assets.topPanelMine);
        timerLabel = new JLabel("-      ");
        timerLabel.setIcon(Assets.clock);
        smiley = new SmileyButton();
        smiley.setIcon(Assets.InGameSmiley);

        //forcer le button du smiley a pas être plus grand que l'emote
        Icon icone = smiley.getIcon();
        int largeur = icone.getIconWidth();
        int hauteur = icone.getIconHeight();
        Dimension taille = new Dimension(largeur, hauteur);
        smiley.setPreferredSize(taille);
        smiley.setMinimumSize(taille);
        smiley.setMaximumSize(taille);
        smiley.setSize(taille);
        // Optionnel : forcer la mise à jour de l'interface
        smiley.revalidate();
        smiley.repaint();
        
        // Conteneur intermédiaire avec FlowLayout centré
        JPanel smileyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        smileyPanel.add(smiley);

        add(minesLabel, BorderLayout.WEST);
        add(smileyPanel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.EAST);

        
        smiley.addActionListener(e -> {
            if (resetListener != null) {
                resetListener.resetGame();
                timerLabel.setText("0      ");
                minesLabel.setText("   "+demineurGUI.getNbMines());
            }        
        });

        

    }

    public SmileyButton getSmiley(){
        return smiley;
    }

    public void stopTimer(){
        demineurGUI.stopTimer();
    }

    public void setMinesRestantes(int nb) {
        minesLabel.setText("   "+nb);
    }

    public void afficherVictoire() {
        smiley.setIcon(Assets.SunglassesSmiley);
        demineurGUI.getTimer().stop();
    }

    public void afficherDefaite() {
        smiley.setIcon(Assets.DeathSmiley);
        demineurGUI.getTimer().stop();
    }

    public void setTemps(int secondes) {
        timerLabel.setText(secondes+"      ");
    }

    public void setTimer(boolean newState){
        demineurGUI.setTimerStarted(newState);
    }

    public void setGameResetListener(GameResetListener listener) {
    this.resetListener = listener;
    }

    public JLabel getTimerLabel(){
        return timerLabel;
    }
    
}
