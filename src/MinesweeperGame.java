	/**
	 * @author Andy
	 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
	
public class MinesweeperGame {
	
	JFrame window; //main window of the game
	JPanel gameDisplay; //where the mine field is displayed
	static int size = 10; //default size of the game is 10*10
	static int difficulty = 2; //default difficulty
	Minefield minefield; //mine field data
	
	
	public static void main(String args[]) {
		MinesweeperGame game = new MinesweeperGame();
	}

	//constructor for the main game window	
	public MinesweeperGame() {
		//create a new minefield class with specified parameters
		minefield = new Minefield(size, (int) ((size*size)*this.getDifficultyMultiplier(difficulty)));
		
		//create JFrame 
		window = new JFrame("Minesweeper");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		//Create main components
		//create the button Bar
		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//create buttons
		JButton settingsButton = new JButton("Settings");
		JButton aboutButton = new JButton("About");
		JButton resetButton = new JButton("Reset");
		
		//add buttons to bar
		buttonBar.add(settingsButton);
		buttonBar.add(aboutButton);
		buttonBar.add(resetButton);
		
		//create main game component
		gameDisplay = new JPanel(new BorderLayout());
		JPanel field = new Board(size);
		gameDisplay.add(field, BorderLayout.CENTER);
		
		//action listeners for buttons
		//about - display information
		aboutButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(window, "Minesweeper 1.0\nCreated by Andy Vuong\nhttps://github.com/andyvuong");
			}
		});
		
		//reset - restart the game
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		//settings - open the settings dialog
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create dialog window
				final JDialog settingWindow = new JDialog();
				settingWindow.setTitle("Settings");
				settingWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				//create JPanel 
				JPanel content = new JPanel(new GridLayout(3,2));
				
				//create labels
				JLabel diffLabel = new JLabel("Difficulty");
				JLabel sizeLabel = new JLabel("Size");
		
				//create input options
				//create difficulty drop down menu
				String[] diffValues = {"1","2","3","4","5"};
				final JComboBox diffDropDown = new JComboBox(diffValues);
				
				//create input field drop down menu
				String[] sizeValues = {"5","10","15","20","25","30"};
				final JComboBox sizeDropDown = new JComboBox(sizeValues);
				
				//create the confirm and cancel button
				JButton okButton = new JButton("OK");
				JButton cancelButton = new JButton("CANCEL");
				
				//add components to the JPanel
				content.add(diffLabel);
				content.add(diffDropDown);
				content.add(sizeLabel);
				content.add(sizeDropDown);
				content.add(okButton);
				content.add(cancelButton);
				
				//action listeners
				//cancel button - close the screen
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						settingWindow.dispose();
					}
				});
				
				//ok button - update the values of the two static variables "size" and "difficulty"
				//Close the current window and restart the game with these parameters.
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						size = Integer.parseInt((String) sizeDropDown.getSelectedItem());
						difficulty = Integer.parseInt((String) diffDropDown.getSelectedItem());
						settingWindow.dispose();
						reset();
					}
				});
				
				//set the JPanel "content" as the content for the setting window. Add adjustments. 
				settingWindow.setContentPane(content);
				settingWindow.pack();
				settingWindow.setResizable(false);
				settingWindow.setVisible(true);
			}
		});
		
		
		//add components to the main game window		
		JPanel content = new JPanel(new BorderLayout());
		content.add(buttonBar,BorderLayout.NORTH);
		content.add(gameDisplay, BorderLayout.CENTER);
	
		//adjust the main game window
		window.setContentPane(content);
		window.setSize(75+(size*20),200+(size*20));
		if(size==5)
			window.setSize(150,205);
		window.setResizable(false);
		window.setVisible(true);
	}
	
	
	/**
	 * This inner class defines the display for the game field
	 */
	private class Board extends JPanel {
		
		Tile[][] squareArray; //2D array of type Tile
		
		/**
		 * Creates a 2D array of type Tile. Initializes each Tile with the value of the
		 * equivalent x-y coordinates in the mine field 2D array. Adds a mouse listener
		 * to each Tile.
		 * @param size of the board
		 */
		public Board(int size) {
			squareArray = new Tile[size][size];
			this.setLayout(new GridLayout(size,size));
			for (int x=0; x<size; x++) {
				for(int y=0; y<size; y++) {
				Tile a = new Tile(x,y);
				a.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						Tile source = (Tile) e.getSource();
						source.revealed=true;
						revealSquares(squareArray, source, source.getArrX(),source.getArrY());
						source.repaint();
						MinesweeperGame.gameCheck(source, minefield,window, gameDisplay);
						}
				});
				squareArray[x][y] = a;
				this.add(a);
				}
			}
		}
		
		/**
		 * When a tile is clicked, any adjacent tile with the same value as the clicked
		 * one will be revealed.
		 * @param field
		 * @param square
		 * @param x
		 * @param y
		 */
		public void revealSquares(Tile[][] field, Tile square, int x, int y) {
			int val = minefield.getValueAtPos(square.getArrX(),square.getArrY());
			reveal(field,field[x][y],x+1,y,val);
			reveal(field,field[x][y],x-1,y,val);
			reveal(field,field[x][y],x,y+1,val);
			reveal(field,field[x][y],x,y-1,val);	
		}
		
		/**
		 * Helper method for revealSquares. If a tile has the same value as the tile
		 * that was clicked, it will be revealed.
		 * @param field
		 * @param square
		 * @param x
		 * @param y
		 * @param val
		 */
		public void reveal(Tile[][] field, Tile square, int x, int y, int val) {
			if(x<0 || y<0 || x>=field.length || y>=field.length || field[x][y].revealed==true)
				return;
			else if((minefield.getValueAtPos(field[x][y].getArrX(),field[x][y].getArrY()))==val) {
				field[x][y].setRevealed(true);
				field[x][y].repaint();
				revealSquares(squareArray, field[x][y], x,y);
			}
			else
				return;
		}
	}
	
	/**
	 * This inner class defines the the games field objects. Each object is represented as a square and given a position. When clicked, 
	 * the square is is removed to reveal what value is represented by the object in the game (mine or number of mines surrounding object).
	 */
	private class Tile extends JButton {
		int arrX, arrY; //stores the x-y coord of the value this tile represents in the mine field 2D array
		boolean revealed; //state of which the tile is revealed or not
		
		public Tile(int arrX, int arrY) {
			this.arrX=arrX; 
			this.arrY=arrY;
			this.revealed = false;
		}
		
		/**
		 * Paint the tile. If revealed is false, the tile is a gray block. Otherwise the tile
		 * is a white block with the value it represents.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(this.revealed==false) {
				this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, 40, 40);
			}
			else {
				int value = minefield.getValueAtPos(arrX, arrY);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 40, 40);
				g.setColor(Color.RED);
				g.drawString(Integer.toString(value), 10, 15);
			}
		}
		
		/**
		 * @return arrX
		 */
		public int getArrX() {
			return arrX;
		}
		
		/**
		 * @return arrY
		 */
		public int getArrY() {
			return arrY;
		}
		
		/**
		 * Set whether or not a tile instance is revealed.
		 * @param cond
		 */
		public void setRevealed(boolean cond) {
			this.revealed=cond;
		}
	}

	
	
	//MinesweeperGame methods
	
	/**
	 * Returns a double that modifies the number of mines in the current game.
	 * The number of mines is a product of the (size^2) and difficulty multiplier.
	 * @param difficulty
	 * @return
	 */
	public double getDifficultyMultiplier(int difficulty) {
		if(difficulty==1)
			return 0.1;
		if(difficulty==2)
			return 0.3;
		if(difficulty==3)
			return 0.5;
		if(difficulty==4)
			return 0.7;
		else
			return 0.9;
	}
	
	/**
	 * Checks if a tile clicked is or is not a mine. "Ends" the game if it is a mine.
	 * @param square
	 * @param field
	 * @param window
	 * @param source
	 */
	public static void gameCheck(Tile square, Minefield field, JFrame window, JPanel source) {
		if(field.getValueAtPos(square.getArrX(), square.getArrY()) == -1) {
			JOptionPane.showMessageDialog(window, "BOOM");
			source.setVisible(false);
		}
	}
	
	/**
	 * Closes the current game window and restarts the game by creating a new instance 
	 * of MinesweeperGame.
	 */
	public void reset() {
		window.dispose();
		MinesweeperGame game = new MinesweeperGame();
	}
	
}

	



