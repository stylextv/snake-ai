package de.stylextv.snake.game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import de.stylextv.snake.main.Vars;
import de.stylextv.snake.math.MathUtil;

public class Game {
	
	private static Point[] DIRECTIONS = new Point[] {
			new Point(1, 0),
			new Point(0, 1),
			new Point(-1, 0),
			new Point(0, -1)
	};
	
	//64*44
	private Node head;
	
	private int foodX;
	private int foodY;
	
	private boolean locked;
	
	public Game() {
		reset();
	}
	
	public void reset() {
		head = new Node(64/2, 44/2);
		pickNewFoodLocation();
	}
	
	public void update() {
		if(locked) return;
		
		int currentIndex=CycleManager.getIndex(head.getX(), head.getY());
		int foodIndex=CycleManager.getIndex(foodX, foodY);
		boolean hasTail=false;
		int tailIndex=0;
		int secondNodeIndex=0;
		if(head.getChild() != null) {
			hasTail=true;
			Node n=head.getLastNode();
			tailIndex=CycleManager.getIndex(n.getX(), n.getY());
			secondNodeIndex=CycleManager.getIndex(head.getChild().getX(), head.getChild().getY());
		}
		int jumpToTail;
		if(tailIndex>currentIndex) jumpToTail=tailIndex-currentIndex;
		else jumpToTail = (2815-currentIndex)+tailIndex+1;
		
		Point highestDir=null;
		int highestDirJump=0;
		Point highestDirWithSkip=null;
		int highestDirWithSkipJump=0;
		for(Point dir:DIRECTIONS) {
			int newX=head.getX()+dir.x;
			int newY=head.getY()+dir.y;
			if(newX>=0&&newY>=0 && newX<64&&newY<44) {
				int newIndex=CycleManager.getIndex(newX, newY);
				
				if(hasTail) {
					if(newIndex==secondNodeIndex) continue;
				}
				
				boolean skippedFood=false;
				if(newIndex>currentIndex) {
					skippedFood=foodIndex>currentIndex&&foodIndex<newIndex;
				} else {
					skippedFood=foodIndex<newIndex||foodIndex>currentIndex;
				}
				if(skippedFood) continue;
				
				int jump;
				if(newIndex>currentIndex) jump=newIndex-currentIndex;
				else jump = (2815-currentIndex)+newIndex+1;
				if(hasTail) {
					if(jumpToTail<=jump) continue;
				}
				if(highestDirWithSkip==null || highestDirWithSkipJump<jump) {
					highestDirWithSkip=dir;
					highestDirWithSkipJump=jump;
				}
				if(hasTail) {
					int jumpNewToTail;
					if(tailIndex>newIndex) jumpNewToTail=tailIndex-newIndex;
					else jumpNewToTail = (2815-newIndex)+tailIndex+1;
					if(jumpNewToTail<10) {
						continue;
					}
				}
				
				if(highestDir==null || highestDirJump<jump) {
					highestDir=dir;
					highestDirJump=jump;
				}
			}
		}
		if(highestDir!=null) moveSnake(highestDir.x, highestDir.y);
		else moveSnake(highestDirWithSkip.x, highestDirWithSkip.y);
	}
	private void pickNewFoodLocation() {
		ArrayList<Integer> spots = new ArrayList<Integer>();
		for(int i=0; i<2816; i++) {
			spots.add(i);
		}
		Node n=head;
		while(n!=null) {
			spots.remove(Integer.valueOf(n.getY()*64+n.getX()));
			n=n.getChild();
		}
		int index=spots.get(MathUtil.getRandom().nextInt(spots.size()));
		foodX = index%64;
		foodY = index/64;
	}
	private void moveSnake(int x, int y) {
		head.setX(head.getX()+x);
		head.setY(head.getY()+y);
		if(head.getX()==foodX && head.getY()==foodY) {
			head.insertAfter(new Node(head.getX()-x, head.getY()-y));
			int length=0;
			Node n=head;
			while(n!=null) {
				length++;
				n=n.getChild();
			}
			if(length==2816) locked=true;
			else pickNewFoodLocation();
		} else if(head.getChild() != null) {
			Node last=head.removeLastNode();
			last.setX(head.getX()-x);
			last.setY(head.getY()-y);
			head.insertAfter(last);
		}
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Vars.GAME_COLOR_FOOD);
		graphics.fillRect(foodX*8+25, foodY*8+25, 6, 6);
		
		graphics.setColor(Vars.GAME_COLOR_TILE);
		Node n=head;
		while(n!=null) {
			Node child=n.getChild();
			if(child == null) {
				graphics.fillRect(n.getX()*8+25, n.getY()*8+25, 6, 6);
			} else {
				if(child.getX()>n.getX()) graphics.fillRect(n.getX()*8+25, n.getY()*8+25, 8, 6);
				else if(child.getY()>n.getY()) graphics.fillRect(n.getX()*8+25, n.getY()*8+25, 6, 8);
				else if(child.getX()<n.getX()) graphics.fillRect(n.getX()*8+23, n.getY()*8+25, 8, 6);
				else graphics.fillRect(n.getX()*8+25, n.getY()*8+23, 6, 8);
			}
			n=child;
		}
	}
	
}
