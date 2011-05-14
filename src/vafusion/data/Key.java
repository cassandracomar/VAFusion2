package vafusion.data;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Key {
	private int note, x, y, height, width;
	private boolean isWhite, isPressed;
	private RoundRectangle2D.Double clickableArea;
	private RoundRectangle2D.Double highlightArea;
	
	
	public Key(int x, int y, int width, int height, int note, boolean isWhite){
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.note = note;
		this.isWhite = isWhite;
		
		double fivePerc = (int) (width * 0.05);
		
		if(isWhite){
			this.clickableArea = new RoundRectangle2D.Double(x, y - 5 * fivePerc, width, height + 5 * fivePerc, fivePerc * 5, fivePerc * 5);
			this.highlightArea = new RoundRectangle2D.Double(x + fivePerc, y + fivePerc + height/2, width-(2*fivePerc), height/2 -(2*fivePerc), fivePerc * 10, fivePerc * 10);
		}else{
			this.clickableArea = new RoundRectangle2D.Double(x - width/2, y - 5 * fivePerc, width, height + 5 * fivePerc, fivePerc * 10, fivePerc * 10);
			this.highlightArea = new RoundRectangle2D.Double(x + fivePerc - width/2, y + fivePerc, width-(2*fivePerc), height-(2*fivePerc), fivePerc * 10, fivePerc * 10);
		}
	}
	
	public int getNote(){
		return note;
	}

	public boolean isPressed() {
		return isPressed;
	}

	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}
	
	public boolean isWhite(){
		return this.isWhite;
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
	
	public RoundRectangle2D.Double getClickableArea(){
		return this.clickableArea;
	}
	
	public RoundRectangle2D.Double getHighlightArea(){
		return this.highlightArea;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
}
