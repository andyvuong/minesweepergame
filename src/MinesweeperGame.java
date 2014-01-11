	/**
	 * @author Andy Vuong
	 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
	
public class MinesweeperGame {
	
	JFrame window;
	int size;
	int difficulty;
	boolean reset = false;
	
	public static void main(String args[]) {
		MinesweeperGame game = new MinesweeperGame();
	}

		
	public MinesweeperGame() {
		
		//JFrame 
		window = new JFrame("Minesweeper");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Components
		
		//button Bar
		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//create buttons
		JButton settingsButton = new JButton("Settings");
		JButton aboutButton = new JButton("About");
		
		//add buttons to bar
		buttonBar.add(settingsButton);
		buttonBar.add(aboutButton);
		
		//action listeners for buttons
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
					}
				});
				
				settingWindow.setContentPane(content);
				settingWindow.pack();
				settingWindow.setResizable(false);
				settingWindow.setVisible(true);
			}
		});
		
		
		
		JPanel content = new JPanel(new BorderLayout());
		content.add(buttonBar,BorderLayout.NORTH);
		window.setContentPane(content);
		window.setSize(250,100);
		window.setResizable(false);
		window.setVisible(true);
	}
	
	
}

	



