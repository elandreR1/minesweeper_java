/**
	--- tous les imports ---
*/

public class Case {
	//ATTRIBUTS
	private int coordX ;	//coord X dans la grille
	private int coordY ;	//coord Y dans la grille
	private int casesAround ; //entre 3 et 8
	private int minesAround ; //entre 0 et 8
	private int flagsAround ; //entre 0 et 8		(utile pour le middle click)
	private boolean isFlagged ;	//est-ce qu'il y a un drapeau sur la case
	private boolean isMine ;	//est-ce que la case est une mine
	private boolean isDiscovered ;	//est-ce que la case est révélée
	private boolean isCleared ;	//est-ce que la case est nettoyée (ie révélée et pas de mine)
	private boolean isFirstCase ; //est-ce que la case est la première case révélée par le joueur 
	private boolean isDeathMine ; //est-ce que la case est la mine responsable 

	//CONSTRUCTEUR
	public Case( int coordX, int coordY ) {			//je pense que la gestion de tout le reste se fait après le premier clic
		this.coordX = coordX ; 				//quand la grille est générée
		this.coordY = coordY ;
	}

	//GETTERS / SETTERS
	public void setCasesAround( int nbCasesAround ){		//affecter le nb de cases avoisinant de la case
		casesAround = nbCasesAround ;
	}

	public void setMinesAround( int nbMinesAround ) {		//affecter le nb de mines avoisinant la case
		minesAround = nbMinesAround ; 
	}

	public void setFlagged( boolean newFlagState ){			//affecter l'état Flag d'une case, pas forcément utile, peut-être pour du			isFlagged = newFlagState ;				//debug ou bien forcer un état
		this.isFlagged = newFlagState;
}
	public void switchFlagState(){				//permet d'inverser l'état Flag d'une case (clic droit pour placer/supprimer drapeau)
		isFlagged = !isFlagged ; 
	}

	public void setMineState( boolean newMineState ){	//affecter l'état estMine d'une case pendant la génération de la grille
		isMine = newMineState ;
	}

	public void setFlagsAround( int newFlagsNb ){		//affecter le nombre de drapeaux avoisinant la case (utile pour le middle click)
		flagsAround = newFlagsNb ;
	}

	public void setClearedState( boolean newClearState ){
		isCleared = newClearState ;
	}

	public void setDiscoveredState( boolean newDiscoverState ){
		isDiscovered = newDiscoverState ;
	}

	public void setFirstCaseState(boolean newState){
		isFirstCase = newState ;
	}

	public void setDeathMine( boolean newState ){
		isDeathMine = newState ; 
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	public int getCoordX(){					//obtenir la coord X de la case
		return coordX ;
	}
	public int getCoordY(){					//obtenir la coord Y de la case
		return coordY ;
	}
	public int getCasesAround(){				//obtenir le nb de cases avoisinant la case
		return casesAround ;
	}
	public int getMinesAround(){				//obtenir le nb de mines avoisinant la case
		return minesAround ;
	}
	public int getFlagsAround(){				//obtenir le nb de flags avoisinant la case
		return flagsAround ;
	}
	public boolean getFlagState(){				//obtenir l'état isFlagged de la case
		return isFlagged ;
	}
	public boolean getMineState(){				//obtenir l'état isMine de la case
		return isMine ;
	}
	public boolean getDiscoverState(){			//obtenir l'état isDiscovered de la case
		return isDiscovered ;
	}
	public boolean getClearState(){				//obtenir l'état isCleared de la case
		return isCleared ;
	}

	public boolean getFirstCaseState(){
		return isFirstCase;
	}

	public boolean getDeathMine(){
		return isDeathMine ; 
	}


}