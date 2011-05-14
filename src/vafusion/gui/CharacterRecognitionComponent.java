package vafusion.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import jm.music.data.Note;

import vafusion.data.Score;
import vafusion.recog.CharacterRecognizer;

@SuppressWarnings("serial")
public class CharacterRecognitionComponent extends RecognitionComponent implements Runnable {
	volatile int[][] pixels;
	final int height = 147, width = 512; //enforced by neural network size
	int x, y;
	boolean running = true;

	private Score score;
	public CharacterRecognitionComponent(int xOff, int yOff, Score s) {
		
		pixels = new int[width][height]; //yes, address this array pixels[x][y]
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				pixels[x][y] = 0;
		
		this.x = xOff;
		this.y = yOff;
		
//		this.x = getX();
//		this.y = getY();
		this.addMouseMotionListener(createMouseMotionListener());
		this.addMouseListener(createMouseListener());
		this.setPreferredSize(new Dimension(width, height));
		
		recog = new CharacterRecognizer("char_recog.network");
		score = s;
		Thread t = new Thread(this);
		t.start();

	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(this.x, this.y, this.width, this.height);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(this.x, this.y, this.width, this.height);
		
		draw(g2d);
		
	}
	
	private MouseMotionListener createMouseMotionListener() {
		
		return new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				
				System.out.println("mouse drag detected");
				pixels[e.getX()][e.getY()] = 1;
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
			
		};
		
	}
	
	private MouseListener createMouseListener() {
		final CharacterRecognitionComponent temp = this;
		
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
				System.out.println("mouse click detected!");
				pixels[arg0.getX()][arg0.getY()] = 1;
				temp.notify();
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		};

		
	}

	/*
	 * Attach multitouch gesture recognizers later.
	 */
	
	
	public BufferedImage getImage() {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics2D g2d = (Graphics2D)img.getGraphics();
		draw(g2d);
		
		return img;
		
	}
	
	private void draw(Graphics2D g2d) {
		
		g2d.setColor(Color.BLACK);
		/*
		 * 
		 * I'm not actually sure that this is how it works
		 * I want to draw a bunch of points, but I don't have a draw point method
		 * to work with.
		 * 
		 */
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)				
				if(pixels[x][y] != 0)
					g2d.draw(new Line2D.Double(x + this.x, y + this.y, x + this.x, y + this.y));		
		
	}

	@Override
	public void run() {
		
		while(running) {
			try {
				this.wait(10);
				vafusion.recog.Character c = (vafusion.recog.Character)recog.match(getImage());

				Note n;
				if(c.isRest())
					n = new Note(Note.REST, c.getRhythmValue());
				else
					n = new Note(Note.DEFAULT_PITCH, c.getRhythmValue());
				
				if(c.getRhythmValue() != 0)
					score.addNote(n);
			
			} catch (InterruptedException e) {
				continue;
			}
			
			clear();
		}
		
	}
	
	public void stop() {
		
		running = false;
		
	}
	
	public void clear() {
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				pixels[i][j] = 0;
		
	}
	
//	public int getWidth() {
//		
//		return width;
//		
//	}
//	
//	public int getHeight() {
//		
//		return height;
//		
//	}
//	
	public int getRealX() {
		
		return x;
		
	}
	
	public int getRealY() {
		
		return y;
		
	}
	
}
