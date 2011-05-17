package vafusion.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JComponent;

import vafusion.music.Staff;

@SuppressWarnings("serial")
public class StaffComponent extends JComponent{
	
	private vafusion.data.Score score;
	int x, y, width, height;
	
	StaffComponent(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.score = new vafusion.data.Score(x, y, width, height, 5);
	}
	
	public void paint(Graphics g){

		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(score.getX(), score.getY()+5, score.getWidth(), score.getHeight());
		
		g2d.setColor(Color.BLACK);
		g2d.drawRect(score.getX(), score.getY()+5, score.getWidth(), score.getHeight());

		
		score.drawNotes();
		
		
		for(Staff staff: score.getStaves())			
			staff.paint(g2d);
		
	}

	public vafusion.data.Score getScore() {
		return score;
	}

	public void setScore(vafusion.data.Score score) {
		this.score = score;
	}
	
	/*public int getX() {
		
		return x;
		
	}
	
	public int getY() {
		
		return y;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}*/
	
	public int getRealWidth() {
		
		return width;
		
	}
	
	public int getRealHeight() {
		
		return height;
		
	}

}
