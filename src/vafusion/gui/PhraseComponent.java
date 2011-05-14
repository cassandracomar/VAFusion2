package vafusion.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import vafusion.music.Staff;


@SuppressWarnings("serial")
public class PhraseComponent extends JComponent{
	
	private vafusion.data.Score score;
	
	PhraseComponent(final int x, final int y,final int height, final int width){
		this.score = new vafusion.data.Score(x, y, height, width, 1);
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(score.getX(), score.getY()+5, score.getWidth(), score.getHeight());
		
		for(Staff staff: score.getStaves()){
			staff.paint(g2d);
		}		
	}
}
