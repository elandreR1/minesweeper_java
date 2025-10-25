import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import java.awt.Color;
import resources.Assets;

public class GameControllerGUI{
    private Grille grille;
    private GridPanel gridPanel;
    private TopPanel topPanel;
    private boolean gameOver = false;
    private boolean premierClic = true ;


    

    public GameControllerGUI(JFrame frame,Grille grille, GridPanel gridPanel, TopPanel topPanel) {
        this.grille = grille;
        this.gridPanel = gridPanel;
        this.topPanel = topPanel;
        
    }

    

    public void reveler(int x, int y) {

        //cas normal où il n'y a pas trop de mines
        if(premierClic && (grille.getNbMinesTotal()< (grille.getTailleX() * grille.getTailleY() - 9))){
            
            grille.placerMinesHorsFirstClic(x, y);      //on génère les mines après le premier clic pour empêcher de tomber direct sur une mine
            premierClic = false ;
            grille.computeMinesAround();                //on calcule les mines avoisinantes pour toutes les cases de la grille
            grille.computeCasesUndiscovered();          //on calcule le nombre de cases vides à découvrir pour la winCond
            topPanel.setMinesRestantes(grille.getNbMinesRestantes());   //on affiche sur le panneau supérieur le nb de mines dans la grille 
            if(grille.getNbCasesUndiscovered() == 0) {              //si toutes les mines sont découvertes au premier clic
                gameOver = true ; 
                topPanel.getTimerLabel().setText("0      ");
                return ; 
            }
        }


        if(gameOver) return ;
        
        


        
        if (grille.getCaseByIndex(x, y).getMineState()) {       //Si on clique sur une mine
            
            try (InputStream is = getClass().getResourceAsStream("/resources/Mine.png")) {
                CelluleButton tmp = gridPanel.getCelluleButtonByIndex(x, y);
                tmp.setIcon(Assets.mineWipedIcon);
                tmp.setBackground(Color.RED);
                tmp.setText("");
                grille.setXCoordDeathMine(x);   //on stocke les coord de la mine qui a fait perdre le joueur
                grille.setYCoordDeathMine(y);
            } catch (IOException e) {       //try catch indispensable car assez instable, dépend de l'humeur de VSCode
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }

            computeDefeat(x,y);
            
        
        }  else if(!grille.getCaseByIndex(x, y).getMineState()){           //si on clique sur une case non mine
            
            grille.getCaseByIndex(x, y).setClearedState(true);
            grille.getCaseByIndex(x, y).setDiscoveredState(true);
            grille.propagerZero(grille.getCaseByIndex(x, y));
        }
        grille.computeCasesUndiscovered();
        gridPanel.refresh(); 
        

        

        if (grille.getNbCasesUndiscovered() == 0) {  //si la grille est nettoyée
            computeVictory();
        }
    }

    public void revelerMiddleClick(int x, int y){
        /**
         * Cas 0 : Middle click sur une case vide (0 mines autour)
         *  -> il ne se passe rien (normalement c'est même pas détecté)
         * 
         * Cas 1 : Middle click sur une case non découverte ou un drapeau 
         *  -> les cases non découvertes et non flag autour sont mises en surbrillance 
         * 
         * Cas 2 : Middle click sur une case vide portant le chiffre x
         *  -> si une case autour est flag alors qu'elle est pas une mine -> explosion
         *  -> si il y a moins de drapeaux avoisinant la case que le chiffre x -> les cases autour non vides et non flags sont mises en surbrillance
         *  -> si toutes les mines avoisinant la case sont flags -> les cases non découvertes et non mines autour sont découvertes
         */

        if(gameOver)return; 
        topPanel.getSmiley().setIcon(Assets.OnClickSmiley);
        Case caseTemp = grille.getCaseByIndex(x, y);
        if(caseTemp.getClearState() && caseTemp.getMinesAround() == 0)return;
        //Cas 0 : 
        if(caseTemp.getClearState() && caseTemp.getMinesAround()==0){
            //vide, ne rien faire, supposément unreachable comme cas
            System.out.println("DEBUG : il y a un problème si tu vois ce message !");
        }

        //Cas 1 :   middle click sur une case pas découverte
        else if(!caseTemp.getDiscoverState() || (caseTemp.getClearState() && caseTemp.getDiscoverState() && grille.computeFlagsAround(x, y) < caseTemp.getMinesAround())){
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1 ; j++) {
                    if(grille.isInGrid(i, j) && !grille.getCaseByIndex(i, j).getClearState()){
                        CelluleButton tmpBouton = gridPanel.getCelluleButtonByIndex(i, j);
                        tmpBouton.setBackground(Color.LIGHT_GRAY);  
                    }    
                }
                
            }
        }

        //Cas 2 case vide avec un chiffre :     
        if((!caseTemp.getMineState() && caseTemp.getClearState() && caseTemp.getDiscoverState()) || caseTemp.getFlagState() ){
            boolean done = false ;
            for(int i = caseTemp.getCoordX() - 1 ; i <= caseTemp.getCoordX() + 1 ; i++){
                for(int j = caseTemp.getCoordY() - 1 ; j <= caseTemp.getCoordY() + 1 ; j++){

                    if(grille.isInGrid(i, j) && (i != x || j != y) && !grille.getCaseByIndex(i, j).getClearState()){       //autour de la case centrale
                        
                        //si une case est flag alors que c'est pas une mine
                        

                        if(grille.getCaseByIndex(i, j).getFlagState() && !grille.getCaseByIndex(i, j).getMineState()){ //si drapeau mais pas mine
                            done = true;
                            computeDefeat(x, y);
                        }

                        //si y'a moins de drapeaux autour que le nb de mines sur la case
                        else if(grille.computeFlagsAround(x, y) < grille.getCaseByIndex(x, y).getMinesAround()){
                            done = true;
                            for (int k = x-1; k <= x+1; k++) {
                                for (int l = y-1; l <= y+1 ; l++) {
                                    if(grille.isInGrid(k, l) && !grille.getCaseByIndex(k, l).getClearState()){
                                        CelluleButton tmpBouton = gridPanel.getCelluleButtonByIndex(k, l);
                                        tmpBouton.setBackground(Color.LIGHT_GRAY);  
                                    }    
                                }
                            }
                        }
                    }
                }
            }
            //si toutes les mines avoisinant la case sont flags -> les cases non découvertes autour sont découvertes
            if(!done && !grille.getCaseByIndex(x, y).getFlagState() && grille.checkIfAllMinesAroundFlagged(x,y)){
                grille.discoverAllAround(x,y);
                grille.computeCasesUndiscovered();
                gridPanel.refresh();
                done = true ;
                if(grille.getNbCasesUndiscovered() == 0){
                    computeVictory();
                }

        
        
            }
            done = false ;
        }

    }

    public boolean getGameOverState(){
        return gameOver;
    }

    public void relacherMiddleClick(int x, int y){
        if(gameOver)return;
        
        topPanel.getSmiley().setIcon(Assets.InGameSmiley);
        
        Case caseTemp = grille.getCaseByIndex(x, y);

        if(!caseTemp.getClearState() && !caseTemp.getDiscoverState() || (!caseTemp.getClearState() && !caseTemp.getDiscoverState() || (caseTemp.getClearState() && caseTemp.getDiscoverState() && grille.computeFlagsAround(x, y) < caseTemp.getMinesAround()) && grille.allFlagsAroundClear(x,y))){
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1 ; j++) {
                    if(grille.isInGrid(i, j) && !grille.getCaseByIndex(i, j).getClearState()){
                        CelluleButton tmpBouton = gridPanel.getCelluleButtonByIndex(i, j);
                        tmpBouton.setBackground(Color.DARK_GRAY);  
                    }    
                }
                
            }
        }
    }

    public void toggleDrapeau(int x, int y) {   
        if (gameOver) return;
        if(grille.getCaseByIndex(x, y).getFlagState() && !grille.getCaseByIndex(x, y).getDiscoverState()){ 
            grille.decrFlagsPoses();
        } else if(!grille.getCaseByIndex(x, y).getFlagState() && !grille.getCaseByIndex(x, y).getDiscoverState() ){
            grille.incrFlagsPoses();
        }
        grille.getCaseByIndex(x, y).switchFlagState();
        gridPanel.refresh();
        topPanel.setMinesRestantes(grille.getNbMinesTotal()-grille.getNbFlagsPlaced());
        
    }

    public void actOnSpaceClick(int x, int y){  
        if (gameOver) return;

        else if(grille.getCaseByIndex(x,y).getClearState() && !grille.allFlagsAroundClear(x,y)){
            computeDefeat(x, y);
        }


        else if(!grille.getCaseByIndex(x, y).getClearState()){
            if(!grille.getCaseByIndex(x, y).getFlagState()){
                grille.getCaseByIndex(x, y).switchFlagState();
                grille.incrFlagsPoses();
            } else if(grille.getCaseByIndex(x, y).getFlagState()){
                grille.getCaseByIndex(x, y).switchFlagState();
                grille.decrFlagsPoses();
            }
            gridPanel.refresh();
            topPanel.setMinesRestantes(grille.getNbMinesTotal()-grille.getNbFlagsPlaced());
        }

        else if(grille.getCaseByIndex(x, y).getClearState() && grille.checkIfAllMinesAroundFlagged(x,y)){
            grille.discoverAllAround(x,y);
            grille.computeCasesUndiscovered();
            gridPanel.refresh();
            if(grille.getNbCasesUndiscovered() == 0){
                computeVictory();
            }
        }
    }

    public void computeVictory(){
        gameOver = true; 
        grille.setGameWon(true);
        grille.revealMinesRemaining(); 
        topPanel.setMinesRestantes(0);  //on met la valeur en haut à gauche à 0
        gridPanel.refresh();           
        gridPanel.desactiverTout();     
        topPanel.afficherVictoire();       //update le smiley avec un smiley content
        //JOptionPane.showMessageDialog(null, " Grille nettoyée. "); -> pour afficher une popup

    }

    public void computeDefeat(int x, int y){
        gameOver = true;

        for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1 ; j++) {
                    if(grille.isInGrid(i, j) && !grille.getCaseByIndex(i, j).getMineState() && !grille.getCaseByIndex(i, j).getClearState()){
                        CelluleButton tmpBouton = gridPanel.getCelluleButtonByIndex(i, j);
                        tmpBouton.setBackground(Color.DARK_GRAY);  
                    }    
                }
                
            }
        grille.getCaseByIndex(x, y).setDeathMine(true);
        gridPanel.desactiverTout();
        topPanel.afficherDefaite();     //update le smiley à la mort
        //JOptionPane.showMessageDialog(null, "💥 BOOM "); -> pour afficher une popup mais chiant à la longue
        grille.setPartieEnCours(false);
        grille.setGameWon(false);
        grille.revealMinesRemaining();
        grille.setDeathMine(x, y);
        gridPanel.refresh();
    }

    public TopPanel getTopPanel(){
        return topPanel;
    }
}
