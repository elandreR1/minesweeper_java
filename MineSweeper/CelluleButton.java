import javax.swing.*;
import resources.Assets;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class CelluleButton extends JButton {
    @SuppressWarnings("unused") //évite les warning sur x et y
    private final int x, y;
    private final Case caseLogique;
    

    public CelluleButton(int x, int y, Case caseLogique, GameControllerGUI controller, Grille grille, GridPanel gridPanel, DemineurGUI demineurGUI) {
        this.x = x;
        this.y = y;
        this.caseLogique = caseLogique;
        setPreferredSize(new Dimension(30, 30));
        setMargin(new Insets(0, 0, 0, 0));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isEnabled()) return;   //empêche de cliquer sur un case vide

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if(!grille.getCaseByIndex(caseLogique.getCoordX(), caseLogique.getCoordY()).getFlagState()){
                        controller.reveler(x, y);
                        if(!demineurGUI.isTimerStarted() && !controller.getGameOverState())demineurGUI.startTimer();
                        else if(!demineurGUI.isTimerStarted() && controller.getGameOverState())controller.getTopPanel().setTemps(0);
                        
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if(!grille.getCaseByIndex(x, y).getClearState())controller.toggleDrapeau(x, y);
                }
                
            }

            @Override
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isMiddleMouseButton(e)){
                    controller.revelerMiddleClick(x, y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(SwingUtilities.isMiddleMouseButton(e)){
                    controller.relacherMiddleClick(x,y);
                }
            }

        });
    }


    public void mettreAJour(Grille grille) {
        Case caseTemporaire = grille.getCaseByIndex(caseLogique.getCoordX(), caseLogique.getCoordY());
        setOpaque(true);
        if (caseTemporaire.getClearState()) { 
            setBackground(Color.WHITE);
            if (caseTemporaire.getMineState()) {
                setText("💣");
            } else {
                int nbMines = grille.getCaseByIndex(caseTemporaire.getCoordX(), caseTemporaire.getCoordY()).getMinesAround(); //le pb vient du fait que caseLogique ne contient pas le nb de mines autour
                if(nbMines == 0){ 
                setEnabled(false);
                }
                setContentAreaFilled(true);   // autorise le remplissage de fond
                setBorderPainted(true);       // garde la bordure visible (facultatif)
                grille.getCaseByIndex(caseTemporaire.getCoordX(), caseTemporaire.getCoordY()).setDiscoveredState(true);
                setText(nbMines > 0 ? String.valueOf(nbMines) : "");
                
                //Pour gérer la couleur des chiffres
                if(nbMines == 1){
                    setForeground(Color.BLUE);
                } else if(nbMines == 2){
                    setForeground(new Color(0, 100, 0));
                } else if(nbMines == 3){
                    setForeground(Color.RED);
                } else if(nbMines == 4){
                    setForeground(new Color(128, 0, 128)); // Violet
                } else if(nbMines == 5){
                    setForeground(new Color(139, 69, 19)); // Marron
                } else if(nbMines == 6){
                    setForeground(new Color(135, 206, 235)); //bleu ciel
                } else if(nbMines == 7){
                    setForeground(Color.BLACK);
                } else if(nbMines == 8){
                    setForeground(Color.LIGHT_GRAY);
                } 

                setFont(new Font(getFont().getName(), Font.BOLD, 14)); //permet d'agrandir le chifre
                    
            }
        } 

        //CAS où la mine n'a pas été flag
        else if (!grille.getPartieEnCoursState() && !grille.getGameWonState() && caseTemporaire.getMineState() && caseTemporaire.getFlagState() && !caseTemporaire.getDiscoverState() && (caseTemporaire.getCoordX() != grille.getXCoordDeathMine() || caseTemporaire.getCoordY() != grille.getYCoordDeathMine())){        
            try (InputStream is = getClass().getResourceAsStream("/resources/MineUnflagged.png")) {
                setIcon(Assets.mineUnflaggedIcon);
                setText("");
            } catch (IOException e) {       //try catch indispensable car assez instable, dépend de l'humeur de VSCode
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }       //FONCTIONNEL
        
        //CAS où la mine est flag pendant la partie (placer drapeau)
        
        else if (grille.getPartieEnCoursState() && caseTemporaire.getFlagState()) {        //ici pour modifier le png du drapeau (clic droit)
            try (InputStream is = getClass().getResourceAsStream("/resources/Flag.png")) {
                setIcon(Assets.flagIcon);
                setText("");
            } catch (IOException e) {       //try catch indispensable car assez instable, dépend de l'humeur de VSCode
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
        
        //CAS où la mine est unflag pendant la partie (enlever drapeau)
        else if (grille.getPartieEnCoursState() && !caseTemporaire.getFlagState() ) {        //ici pour modifier le png du drapeau (clic droit)
            setIcon(null);
        }  else {
            setContentAreaFilled(true);   // autorise le remplissage de fond
            setBorderPainted(true);       // garde la bordure visible (facultatif)

            setText("");
        }
    }
}
