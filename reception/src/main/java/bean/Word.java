package bean;

import java.awt.Color;
import java.awt.Font;

public class Word{
	private String content;
	private int x;
	private int y;
	private int size;
	private Color color;
	private int weight;
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
	public int getWeight() {
		return weight;
	}
	public Word(String content, int x, int y, int size, Color color) {
		super();
		this.content = content;
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
		this.weight = Font.PLAIN;
	}
	public Word(String content, int x, int y, int size, Color color, int weight) {
		super();
		this.content = content;
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
		this.weight = weight;
	}
	
}
