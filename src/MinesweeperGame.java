	/**
	 * @author Andy
	 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
	
public class MinesweeperGame {
	
	JFrame window; //main window of the game
	JPanel gameDisplay; //where the mine field is displayed
	static int size = 10; //default size of the game is 10*10
	static int difficulty = 2; //default difficulty
	boolean reset = false;
	Minefield minefield; //mine field data
	
	
	public static void main(String args[]) {
		MinesweeperGame game = new MinesweeperGame();
	}

	//constructor for the main game window	
	public MinesweeperGame() {
		minefield = new Minefield(size, (int) ((size*size)*this.getDifficultyMultiplier(difficulty)));
		
		//JFrame 
		window = new JFrame("Minesweeper");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		////////////////////////Components
		
		//button Bar
		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//create buttons
		JButton settingsButton = new JButton("Settings");
		JButton aboutButton = new JButton("About");
		
		//add buttons to bar
		buttonBar.add(settingsButton);
		buttonBar.add(aboutButton);
		
		//main game component
		JPanel field = new Field(size);
		
		
		///////////////////////action listeners for buttons
		//about
		aboutButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(window, "Minesweeper 1.0\nCreated by Andy Vuong\nhttps://github.com/andyvuong");
			}
		});
		
		//settings
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
				//diff drop down
				String[] diffValues = {"1","2","3","4","5"};
				final JComboBox diffDropDown = new JComboBox(diffValues);
				//input field
				String[] sizeValues = {"5","10","15","20","25","30"};
				final JComboBox sizeDropDown = new JComboBox(sizeValues);
				//confirm button
				JButton okButton = new JButton("OK");
				JButton cancelButton = new JButton("CANCEL");
				//add components
				content.add(diffLabel);
				content.add(diffDropDown);
				content.add(sizeLabel);
				content.add(sizeDropDown);
				content.add(okButton);
				content.add(cancelButton);
				
				//action listeners
				//cancel
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						settingWindow.dispose();
					}
				});
				
				//ok
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						size = Integer.parseInt((String) sizeDropDown.getSelectedItem());
						difficulty = Integer.parseInt((String) diffDropDown.getSelectedItem());
						reset=true;
						settingWindow.dispose();
						reset();
					}
				});
				
				settingWindow.setContentPane(content);
				settingWindow.pack();
				settingWindow.setResizable(false);
				settingWindow.setVisible(true);
			}
		});
		
		
		///////////////add components to the window		
		JPanel content = new JPanel(new BorderLayout());
		content.add(buttonBar,BorderLayout.NORTH);
		content.add(field, BorderLayout.CENTER);
		window.setContentPane(content);
		window.setSize(75+(size*20),200+(size*20));
		if(size==5)
			window.setSize(150,205);
		window.setResizable(false);
		window.setVisible(true);
		minefield.printDataField();
	}
	
	/**
	 * This inner class defines the display for the game field
	 */
	private class Field extends JPanel {
		
		FieldSquare[][] squareArray;
		
		public Field(int size) {
			squareArray = new FieldSquare[size][size];
			this.setLayout(new GridLayout(size,size));
			for (int y=0; y<size; y++) {
				for(int x=0; x<size; x++) {
				FieldSquare a = new FieldSquare(y,x);
				a.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						FieldSquare source = (FieldSquare) e.getSource();
						source.revealed=true;
						revealSquares(squareArray, source, source.getArrX(),source.getArrY());
						source.repaint();
						MinesweeperGame.gameCheck(source, minefield,window);
						}
				});
				squareArray[x][y] = a;
				this.add(a);
				}
			}
		}
		
		public void revealSquares(FieldSquare[][] field, FieldSquare square, int x, int y) {
			
			int val = minefield.getValueAtPos(square.getArrX(),square.getArrY());
			reveal(field,field[x][y],x+1,y,val);
			reveal(field,field[x][y],x-1,y,val);
			reveal(field,field[x][y],x,y+1,val);
			reveal(field,field[x][y],x,y-1,val);	
		}
		
		public void reveal(FieldSquare[][] field, FieldSquare square, int x, int y, int val) {

			if(x<0 || y<0 || x>field.length || y>field.length || square.revealed==true) 
				return;
			if((minefield.getValueAtPos(square.getArrX(),square.getArrY()))==val) {
				square.setRevealed(true);
				square.repaint();
				revealSquares(squareArray, square, x,y);
			}
			else
				return;
		}
	}
	
	
	
	/**
	 * This inner class defines the the games field objects. Each object is represented as a square and given a position. When clicked, 
	 * the square is is removed to reveal what value is represented by the object in the game (mine or number of mines surrounding object).
	 */
	private class FieldSquare extends JButton {
		int arrX, arrY;
		boolean revealed;
		
		public FieldSquare(int arrX, int arrY) {
			this.arrX=arrX; 
			this.arrY=arrY;
			this.revealed = false;
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(this.revealed==false) {
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
		
		public int getArrX() {
			return arrX;
		}
		
		public int getArrY() {
			return arrY;
		}
		
		public FieldSquare getFieldSquare() {
			return this;
		}
		
		public void setRevealed(boolean cond) {
			this.revealed=cond;
		}
	}

	
	
	//static method
	public boolean isMine(int x, int y) {
		if(minefield.getValueAtPos(x, y)==-1) {
			
			return true;
		}
		return false;
	}
	
	public double getDifficultyMultiplier(int difficulty) {
		if(difficulty==1)
			return 0.2;
		if(difficulty==2)
			return 0.4;
		if(difficulty==3)
			return 0.6;
		if(difficulty==4)
			return 0.8;
		else
			return 0.9;
	}
	
	public static void gameCheck(FieldSquare square, Minefield field, JFrame window) {
		if(field.getValueAtPos(square.getArrX(), square.getArrY()) == -1) {
			//temp
			JOptionPane.showMessageDialog(window, "BOOM. GG FRIEND");
		}
	}
	
	public void reset() {
		window.dispose();
		MinesweeperGame game = new MinesweeperGame();
	}
	
}

	



