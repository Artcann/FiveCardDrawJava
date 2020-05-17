package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {
	public void paintComponent(Graphics g) {
		Font font = new Font("Impact", Font.PLAIN, 40);
		g.setFont(font);
		g.setColor(Color.black);
		
		g.drawString("Salut Gabin!", 100, 100);
	}
}
