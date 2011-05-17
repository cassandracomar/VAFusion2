package vafusion.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;

import vafusion.music.Staff;

@SuppressWarnings("serial")
public class StaffComponent extends JComponent{
	
	private vafusion.data.Score score;
	int x, y, width, height;
	ArrayList<vafusion.music.Note> selectedNotes;
	
	StaffComponent(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.score = new vafusion.data.Score(x, y, width, height, 5);
		this.selectedNotes = new ArrayList<vafusion.music.Note>();
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(StaffComponent.this.contains(e.getPoint())) {
					
					System.out.println("Staff clicked!");
					//staff clicked
					vafusion.music.Note clickedNote = score.getNoteAtPos(e.getPoint());
					if(clickedNote != null) {
						clickedNote.select();
						selectedNotes.add(clickedNote);
					}
					
				}
				
			}

		});
	}
	
	public void paint(Graphics g){
		this.x = getX();
		this.y = getY();

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
