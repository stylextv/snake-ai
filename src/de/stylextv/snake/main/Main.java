package de.stylextv.snake.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import de.stylextv.snake.game.Game;
import de.stylextv.snake.render.Renderer;

public class Main {
	
	private static boolean running=true;
	private static long lastUpdate;
	
	private static Renderer renderer;
	
	private static Game game;
	private static int speed;
	
	public static void main(String[] args) {
		try {
			game = new Game();
			createWindow();
			startGameLoop();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	private static void createWindow() {
		JFrame frame = new JFrame(Vars.NAME);
		frame.setSize(Vars.WINDOW_WIDTH+16, Vars.WINDOW_HEIGHT+39);
		Dimension monitor=Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((monitor.width-frame.getWidth())/2, (monitor.height-40)/2-frame.getHeight()/2);
		
		frame.add(renderer = new Renderer());
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_LEFT) {
					speed--;
					if(speed<0) speed=4;
				} else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
					speed++;
					if(speed>4) speed=0;
				} else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					game.reset();
				}
			}
		});
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent event) {
		    	frame.setVisible(false);
		    	running=false;
		    }
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.setResizable(false);
		frame.setVisible(true);
	}
	private static void startGameLoop() {
		while(running) {
			runGameLoop();
		}
		System.exit(0);
	}
	
	private static void runGameLoop() {
		long before=System.nanoTime();
		
		long now=System.currentTimeMillis();
		boolean b=speed>1;
		if(lastUpdate==0 || b || now-lastUpdate>400-speed*300) {
			lastUpdate=now;
			if(b) {
				int l=1;
				if(speed==3) l=100;
				else if(speed==4) l=300;
				for(int i=0; i<l; i++) game.update();
			} else game.update();
		}
		renderer.paintImmediately(0, 0, renderer.getWidth(), renderer.getHeight());
		
		long after=System.nanoTime();
		long sleep=16666667-(after-before);
		if(sleep>0) {
			try {
				Thread.sleep(sleep/1000000, (int)(sleep%1000000));
			} catch (InterruptedException ex) {}
		}
	}
	public static void drawFrame(Graphics2D graphics) {
		int width = renderer.getWidth();
		int height = renderer.getHeight();
		
		graphics.setColor(Vars.GAME_COLOR_BACKGROUND);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Vars.GAME_COLOR_TILE);
		graphics.fillRect(17, 17, width-34, height-34);
		graphics.setColor(Vars.GAME_COLOR_BACKGROUND);
		graphics.fillRect(23, 23, width-46, height-46);
		game.draw(graphics);
	}
	
}
