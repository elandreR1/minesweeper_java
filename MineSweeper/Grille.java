import java.util.Random;

public class Grille {
	// ATTRIBUTS
	private Case[][] grille; // grille, représentée par un tableau de cases en deux dimensions
	private int tailleX; // taille de la grille (largeur) <- X ->
	private int tailleY; // taille de la grille (hauteur)
	private int nbMinesTotal; // nb de mines sur la grille (fixe)
	private int nbMinesRestantes; // nb de mines pas encore flags (nbMinesTotal - nbFlags posés)
	private int nbFlagsPlaced; // nb de flags actuellement posés sur la mine (compris entre 0 et tailleX *
								// tailleY)
	private int nbCasesTotal; // nb de cases sur la grille (correspond à tailleX * tailleY)
	private int nbCasesUndiscovered; // nb de cases pas encore découvertes -> win quand arrive à 0
	private boolean partieEnCours; // true si partie en cours, false si partie terminée (plutôt dans Game.java ?)
	private boolean gameWon; // true si partie gagnée, false si le joueur est mort (plutôt dans Game.java ?)

	private int xDeathMineWiped = -1 ; 	// deux int qui stockent la coordonnée d'une potentielle mine responsable de la défaite
    private int yDeathMineWiped = 0 ;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GETTERS / SETTERS

	public void setTailleX(int tailleX) { // définir la largeur du grille (ne doit pas être modifié pendant la partie !)
		this.tailleX = tailleX;
	}

	public void setTailleY(int tailleY) { // définir la hauteur de la grille (ne doit pas être modifié pendant la partie
											// !)
		this.tailleY = tailleY;
	}

	public void setNbCasesTotal() { // définir le nombre total de cases dans la grille
		this.nbCasesTotal = tailleX * tailleY;
	}
	public void setNbCasesUndiscovered() { // définir le nombre total de cases non mines dans la grille
		this.nbCasesUndiscovered = nbCasesTotal - nbMinesTotal;
	}

	public void setNbCasesUndiscovered( int nbCasesUndiscovered ){
		this.nbCasesUndiscovered = nbCasesUndiscovered ;
	}

	public void setNbMinesTotal(int nbMinesTotal) { // définir le nb de mines sur la grille (entre 1 et tailleX *
													// tailleY)
		this.nbMinesTotal = nbMinesTotal;
	}

	public void setPartieEnCours(boolean partieEnCours) { // définir l'état d'une partie en cours
		this.partieEnCours = partieEnCours;
	}

	public void setGameWon(boolean gameWon) { // définir l'état d'une partie gagnée
		this.gameWon = gameWon;
	}

	public void setXCoordDeathMine(int newXCoord){
		xDeathMineWiped = newXCoord ; 
	}

	public void setYCoordDeathMine(int newYCoord){
		yDeathMineWiped = newYCoord ; 
	}

	////
	public int getTailleX() { // obtenir la largeur de la grille
		return tailleX;
	}

	public int getTailleY() { // obtenir la hauteur de la grille
		return tailleY;
	}

	public int getNbMinesTotal() { // obtenir le nombre total de mines
		return nbMinesTotal;
	}

	public int getNbMinesRestantes() { // obtenir le nombre de mines restantes supposées (nbMinesTotal - NbFlagsPlaced)
		return nbMinesRestantes;
	}

	public int getNbFlagsPlaced() { // obtenir le nombre de drapeaux placés dans la grille
		return nbFlagsPlaced;
	}

	public int getNbCasesTotal() { // obtenir le nombre total de cases de la grille (tailleX * tailleY)
		return nbCasesTotal;
	}

	public int getNbCasesUndiscovered() { // obtenir le nombre de cases pas encore découvertes (win quand il arrive à 0)
		return nbCasesUndiscovered;
	}

	public Case getCaseByIndex(int indX, int indY) {
		return grille[indX][indY];
	}

	public boolean getGameWonState(){
		return gameWon ; 
	}

	public boolean getPartieEnCoursState(){
		return partieEnCours;
	}

	public int getXCoordDeathMine(){
		return xDeathMineWiped;
	}

	public int getYCoordDeathMine(){
		return yDeathMineWiped;
	}

	/////////////////////////////////////
	public void incrMinesRestantes() { // incrémenter de 1 le nombre de mines restantes (quand on pose/enlève un
										// drapeau)
		nbMinesRestantes++;
	}

	public void decrMinesRestantes() { // décrémenter de 1 le nombre de mines restantes
		nbMinesRestantes--;
	}

	public void incrFlagsPoses() { // incrémenter de 1 le nombre de drapeaux posés (quand on pose un drapeau)
		nbFlagsPlaced++;
	}

	public void decrFlagsPoses() { // décrémenter de 1 le nombre de drapeaux posés (quand on enlève un drapeau)
		nbFlagsPlaced--;
	}

	public void incrCasesUndiscovered() {
		nbCasesUndiscovered++;
	}

	public void decrCasesUndiscovered() {
		nbCasesUndiscovered--;
	}

	////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTEUR
	public Grille(int tailleX, int tailleY, int nbMines) {
		this.tailleX = tailleX;
		this.tailleY = tailleY;
		this.nbMinesTotal = nbMines;
		setPartieEnCours(true);
		setGameWon(false);
		setNbCasesTotal();
		setNbCasesUndiscovered();
		nbMinesRestantes = nbMinesTotal - nbFlagsPlaced ; 
		Case grille[][] = new Case[tailleX][tailleY]; // création de la grille (tab2dim de Cases) avec les tailles de X
														// et Y
		for (int i = 0; i < tailleX; i++) {
			for (int j = 0; j < tailleY; j++) {
				grille[i][j] = new Case(i, j);
			}
		}
		this.grille = grille;
		computeMinesAround();
	}

	

	////////////////////////////////////////////////////////////////////////////////
	// METHODES


	public void placerMinesHorsFirstClic(int xFirstClick, int yFirstClick){
		for(int i=0 ; i < tailleX ; i++){
			for(int j=0 ; j<tailleY ; j++){
				grille[i][j].setMineState(false); //enlever toutes les mines existantes par précaution
			}
		}

		int minesToPlace = getNbMinesTotal();		//recup le nb de mines à placer dans la grille

		if(minesToPlace > nbCasesTotal - 1){
			System.out.println("Erreur : nombre de mines à placer supérieur au nombre de cases dans la grille.");
			//note : le " - 1 " correspond à la première case sélectionnée
		}

		Random r = new Random();
		while (minesToPlace > 0) {
			int randX = r.nextInt(tailleX); // sélection random d'un entier X temporaire entre 0 et tailleX - 1 inclus
			int randY = r.nextInt(tailleY); // sélection random d'un entier Y temporaire

			
			if(mineTooCloseFromFirstClick(randX, randY, xFirstClick, yFirstClick) || grille[randX][randY].getMineState()) continue;

       		grille[randX][randY].setMineState(true);
			grille[randX][randY].setDiscoveredState(false);	
        	minesToPlace--;
		}

		computeMinesAround();
	}

	private boolean mineTooCloseFromFirstClick(int randX, int randY, int xFirstClick, int yFirstClick){
		return ( randX >= xFirstClick - 1 && randX <= xFirstClick + 1) && (randY >= yFirstClick -1 && randY <= yFirstClick + 1);
	}


	public void computeMinesAround(){
		for(int i=0 ; i<tailleX ; i++){
			for(int j=0 ; j<tailleY ; j++){
				int cmptTempMines = 0 ;
				for(int k=i-1 ; k<=i+1 ; k++){
					for(int l = j-1 ; l<=j+1 ; l++){
						if((k!=i || j!=l) && isInGrid(k, l) && grille[k][l].getMineState()){
							cmptTempMines++;
						}
					}
				}
				grille[i][j].setMinesAround(cmptTempMines);


			}
		}
	}

	public int computeFlagsAround(int x, int y){
		int cmptFlags = 0 ; 
		for(int i= x-1 ; i<= x+1 ; i++){
			for(int j = y - 1 ; j <= y + 1 ; j++){
				if((x!=i || j!=y) && isInGrid(i, j) && grille[i][j].getFlagState()){
					cmptFlags++;
				}
			}
		}
		return cmptFlags;
	}

	//fonction qui vérifie si tous les flags autour d'une case sont bien des mines
	public boolean allFlagsAroundClear(int x, int y){
		for(int i = x-1 ; i <= x+1 ; i++){
			for(int j = y-1 ; j <= y+1 ; j++){
				if(isInGrid(i, j) && (i != x || j != y) && grille[i][j].getFlagState()){
					if(!grille[i][j].getMineState()) return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkIfAllMinesAroundFlagged(int x, int y){
		for(int i = x-1 ; i <= x+1 ; i++){
			for(int j = y-1 ; j <= y+1 ; j++){
				if((x!=i || j!=y) && isInGrid(i, j) && grille[i][j].getMineState()){
					if(!grille[i][j].getFlagState())return false;
				}

			}
		}
		return true;
	}

	public void discoverAllAround(int x, int y){
		for(int i = x-1 ; i <= x+1 ; i++){
			for(int j = y-1 ; j <= y+1 ; j++){
				if((x!=i || j!=y) && isInGrid(i, j) && !grille[i][j].getMineState()){
					grille[i][j].setDiscoveredState(true);
					grille[i][j].setClearedState(true);
					if(grille[i][j].getMinesAround() == 0){
						propagerZero(grille[i][j]);
					}
				}

			}
		}
	}

	/**
	 * @param coordXtoCheck un indice x
	 * @param coordYtoCheck un indice y
	 * @return true si la case dont les coordonnées sont coordXtoCheck ; coordYtoCheck est dans la grille
	 */
	public boolean isInGrid(int coordXtoCheck, int coordYtoCheck){
		return (coordXtoCheck >= 0 && coordXtoCheck < tailleX) /**coord x valide*/ && 
		(coordYtoCheck >= 0 && coordYtoCheck < tailleY); /**coord y valide */
	}

	public void propagerZero(Case c){
		if(c.getMinesAround() == 0){
			c.setClearedState(true);

			for(int i = c.getCoordX()-1 ; i <= c.getCoordX()+1 ; i++){
				for(int j = c.getCoordY()-1 ; j <= c.getCoordY()+1 ; j++){
					if(isInGrid(i,j) && !grille[i][j].getClearState()){
						grille[i][j].setClearedState(true);
						grille[i][j].setDiscoveredState(true);
						decrCasesUndiscovered();
						propagerZero(grille[i][j]);
					}
				}
			}
		}
	} 

	public void computeCasesUndiscovered(){
		int cmptTemp = 0 ;
		for(int i = 0 ; i < tailleX ; i++){
			for(int j = 0 ; j<tailleY ; j++){
				if(!grille[i][j].getMineState() && !grille[i][j].getDiscoverState()){
					cmptTemp++;
				}
			}
		}
		setNbCasesUndiscovered(cmptTemp);
	}

	public void revealMinesRemaining(){	//fonction pour révéler les mines restantes si toutes les cases vides ont été clear
		for( int i = 0 ; i < tailleX ; i++){
			for(int j = 0 ; j < tailleY ; j++){
				if(grille[i][j].getFlagState()){
					grille[i][j].setDiscoveredState(true);
				}

				else if(grille[i][j].getMineState() && !grille[i][j].getFlagState()){
					grille[i][j].setFlagged(true);
					grille[i][j].setDiscoveredState(false);
				}
			}
		}
	}

	public void setDeathMine( int x, int y){
		grille[x][y].setDeathMine(true);
	}
}
