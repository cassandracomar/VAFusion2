package vafusion.music;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Note {
	private Graphics2D g2d;
	private boolean filled;
	private boolean isNote;
	int x, y, height, width, pos;
	int note;
	double rhythm;
	
	
	public Note(int pos, double rhythm, boolean isNote){
		if(isNote){
			this.filled = isFilled(rhythm);
		}			
		
		this.isNote = isNote;
		this.pos = pos;
		this.rhythm = rhythm;
		
		if(isNote)
			this.pos = pos;
		else
			this.pos = 4;
	}
	
	public String toString(){
		return x +", "+ y+", "+pos;
	}
	
	public void paint(Graphics g){
		this.g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		if(this.isNote){
			if(this.filled){
				//fillOval
				g2d.fillOval(x, y, width, height);
				g2d.draw(getStem());
			}else{
				//draw a ellipse2d
				g2d.draw(new Ellipse2D.Double(x, y, width, height));
				if(rhythm < 4)
					g2d.draw(getStem());
			}
		}else{
			g2d.fillRect(x, y, width, height);
		}
		
		
	}
	
	public Line2D.Double getStem(){
		if(pos > 6) //stem goes up
			return new Line2D.Double(x+width, y + height/2, x+width, y+ height/2 - height * 5);
		
		return new Line2D.Double(x, y + height/2, x, y+ height/2 + height * 5);
	}
	
	public boolean isFilled(double rhythm){
		if(rhythm >= 2)
			return false;
		else
			return true;
	}
	
	public void update(int x, int y, int lineSpace) {
		
		this.x = x;
		this.y = y + (lineSpace/2) * pos;
		this.width = (int)(lineSpace * 2.5);
		this.height = lineSpace;
		
	}
		
}