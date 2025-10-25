import javax.swing.*;

import resources.Assets;

import java.awt.*;

public class GridPanel extends JPanel {
    private JFrame frame ;
    private final CelluleButton[][] boutons;
    private Grille grille;
    private final GameControllerGUI controller;

    public GridPanel(Grille grille, TopPanel topPanel, DemineurGUI demineurGUI) {
        this.grille = grille;
        this.controller = new GameControllerGUI(frame ,grille, this, topPanel);
        this.boutons = new CelluleButton[grille.getTailleX()][grille.getTailleY()];
        setOpaque(true);
        

        setLayout(new GridLayout(grille.getTailleX(), grille.getTailleY()));

        for (int i = 0; i < grille.getTailleX(); i++) {
            for (int j = 0; j < grille.getTailleY(); j++) {
                Case c = grille.getCaseByIndex(i, j);
                CelluleButton bouton = new CelluleButton(i, j, c, controller, grille, this, demineurGUI);
                bouton.setBackground(Color.DARK_GRAY);    
                boutons[i][j] = bouton;
                add(bouton);
            }
        }
    }

    public CelluleButton[][] getCelluleButtons(){
        return boutons;
    }

    public CelluleButton getCelluleButtonByIndex(int x, int y){
        return boutons[x][y];
    }

    public GameControllerGUI getController(){
        return controller;
    }

    public void refresh() {
        for (int i = 0; i < boutons.length; i++) {
            for (int j = 0; j < boutons[i].length; j++) {
                boutons[i][j].mettreAJour(grille);
            }
        }
    }

    public void desactiverTout() {
        
        for( int i = 0 ; i< grille.getTailleX() ; i++){
            for(int j=0; j < grille.getTailleY() ; j++){
                if(!grille.getCaseByIndex(i, j).getMineState()  && !grille.getCaseByIndex(i, j).getFlagState() && grille.getCaseByIndex(i, j).getMinesAround() == 0 ){boutons[i][j].setEnabled(false); }
                else if(!grille.getCaseByIndex(i, j).getMineState()  && grille.getCaseByIndex(i, j).getFlagState()){boutons[i][j].setIcon(Assets.wrongFlag);}
            }
        }
    }

    public void desactiverAutour(int x, int y){
        for( int i = x-1 ; i<= x+1 ; i++){
            for(int j=y-1; j <= y+1 ; j++){
                if(grille.isInGrid(i, j)){boutons[i][j].setEnabled(false);}
            }
        }
    }

    public void setNewGrille( Grille grille ){
        this.grille = grille ;
    }
}
