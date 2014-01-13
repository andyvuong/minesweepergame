/**
 * @author Andy
 */

public class Minefield {

	private int[][] dataField; //data defines game board
	private int size,length,width;
	private int totalMines; //total number of mines on the field
	
	/**
	 * Creates an instance of the Minefield class with the length, size, and width parameter
	 * being equivalent to one another. Creates two 2D arrays with the width and length
	 * equivalent to one another. The 2D arrays are data representations of the Minesweeper
	 * game.
	 * @param size
	 * @param totalMines
	 */
	
	public Minefield(int size, int totalMines) {
		this.size = size*size;
		this.length = size;
		this.width = size;
		this.totalMines = totalMines;
		this.dataField = new int[this.width][this.length];
		
		
		//initiate the game
		generateMineField(this.totalMines);
		generateFieldStatistics();
	}
	
	/**
	 * The mine field is represented as a 2D array with each element in the array holding a value. Each
	 * element represents a specific position of the mine field.
	 * A mine has a value of -1. 
	 * 
	 * Mines are randomly distributed throughout the matrix by placing a negative 1 in a random position.
	 * @param mines  
	 */
	public void generateMineField(int mines) {
		int value;
		MAIN:
		while(mines>0) {
			for(int x=0; x<this.width; x++) {
				for(int y=0; y<this.length; y++) {
					value = placeRandomMine();
					if(this.dataField[x][y]!=-1 && value==-1) {
						this.dataField[x][y] = -1;
						mines--;
					}
					if(mines==0) {
						break MAIN;
					}
				}
			}
		}	
	}	
	
	/**
	 * Generate a random integer from 0 to 20. If the integer is 0, return a -1.
	 * @return -1 or 0 given the conditions of the methods
	 */
	public int placeRandomMine() {
		int a = (int) (Math.random()*20);
		return (a>1 ? 0 : -1);
	}
	
	/**
	 * Each position in the dataField matrix that does not hold a -1 (a mine) holds a value that indicates
	 * the number of mines adjacent or diagonal to the element. 
	 * 
	 * Each position is iterated through and assigned a value that indicates surrounding mines.
	 */
	public void generateFieldStatistics() {
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.length; y++) {
				if(this.dataField[x][y]!=-1)
					this.dataField[x][y] = numberOfMines(x,y);
			}
		}
	}
	
	/**
	 * Checks 8 cases; 8 positions that are adjacent and diagonal to a given position and
	 * returns the number of mines surrounding it.
	 * @return the number of mines surrounding the given position
	 */
	public int numberOfMines(int x, int y) {
		int mines = 0;
		mines+= getMineAtPos(x+1,y);
		mines+= getMineAtPos(x-1,y);
		mines+= getMineAtPos(x,y+1);
		mines+= getMineAtPos(x,y-1);
		mines+= getMineAtPos(x+1,y-1);
		mines+= getMineAtPos(x-1,y+1);
		mines+= getMineAtPos(x+1,y+1);
		mines+= getMineAtPos(x-1,y-1);
		return mines;
	}
	
	/**
	 * Helper method: returns 1 if the current position contains a negative 1.
	 * @param x
	 * @param y
	 * @return -1 if the current position is a mine
	 */
	public int getMineAtPos(int x, int y) {
		if(x>=this.width || y>=this.length || x<0 || y<0) //return the 0 if the position is invalid(index out of bounds)
			return 0;
		else
			return (this.dataField[x][y]==-1 ? 1: 0);
	}
	
	/**
	 * Print the contents of the dataField matrix as a representation of the mine field. 
	 */
	public void printDataField() {
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.length; y++) {
				System.out.print("	" + this.dataField[x][y]);
			}
			System.out.println();
		}
		
	}
	
	//getter methods
	public int getSize() {
		return this.size;
	}

	public int getLength() {
		return this.length;
	}

	public int getWidth() {
		return this.width;
	}

	public int getTotalMines() {
		return this.totalMines;
	}
	
	public int getValueAtPos(int x, int y){
		return this.dataField[x][y];
	}

}
