package de.stylextv.snake.game;

public class Node {
	
	private int x;
	private int y;
	
	private Node child;
	
	public Node(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public Node removeLastNode() {
		if(child.getChild() == null) {
			Node n = child;
			child = null;
			return n;
		} else return child.removeLastNode();
	}
	public Node getLastNode() {
		if(child.getChild() == null) {
			return child;
		} else return child.getLastNode();
	}
	public void insertAfter(Node n) {
		n.setChild(child);
		child = n;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
	
}
