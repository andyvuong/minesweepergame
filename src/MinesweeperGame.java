	/**
	 * @author Andy Vuong
	 */
import java.awt.GridLayout;
import javax.swing.*;
	
public class MinesweeperGame {
	
	public static void main(String args[]) {	
		
		Minefield game = new Minefield(10, 20);
		JPanel field = new JPanel(new GridLayout(1,3));
		
		for(int x=0; x<game.getSize(); x++) {
			for(int y=0; y<game.getSize(); y++) {
				field.add(new FieldSquare(x,y,))
			}
		}
		
		JFrame window = new JFrame("Minesweeper");
		window.setSize(300,400);
		window.setResizable(false);
		window.setLocation(100,100);
		window.setVisible(true);
	}

	
	@SuppressWarnings("serial")
	public class FieldSquare extends JPanel {
		int posX, posY;
		int arrX, arrY;
		boolean revealed;
		
		public FieldSquare(int arrX, int arrY, int posX, int posY) {
			this.arrX=arrX;
			this.arrY=posY;
			this.posX=posX;
			this.posY=posY;
			revealed=false;
		}	
		
		public void paintComponent() {
			
		}
	}
}
