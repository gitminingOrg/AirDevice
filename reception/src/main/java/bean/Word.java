package bean;

import java.awt.Color;

public class Word{
	private String content;
	private int x;
	private int y;
	private int size;
	private Color color;
	public String getContent() {
		return content;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getSize() {
		return size;
	}
	public Color getColor() {
		return color;
	}
	public Word(String content, int x, int y, int size, Color color) {
		super();
		this.content = content;
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}
	
}
