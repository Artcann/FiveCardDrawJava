package gui;

import javax.swing.JFrame;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5297941702705072373L;

	public GUI() {
		
		this.setTitle("Poker Five-Card Draw");
		this.setSize(900, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.setContentPane(new Panel());
		this.setVisible(true);
		
	}
}
