package de.stylextv.snake.render;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.stylextv.snake.main.Main;

public class Renderer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		Main.drawFrame((Graphics2D) g);
	}
	
}
