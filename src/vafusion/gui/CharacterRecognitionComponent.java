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
	final int height = 74, width = 128; //enforced by neural network size
	int x, y;
	boolean running = true;
	long time = System.currentTimeMillis();
	CharacterRecognizer charRecog;

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
		charRecog = (CharacterRecognizer)recog;
		score = s;
		Thread t = new Thread(this);
		t.start();

	}
	
	@Override
	public void paint(Graphics g) {
		
		this.x = getX();
		this.y = getY();
		//System.out.println("charRecog location: x: " + this.x + " y: " + this.y);
		//this.width = getWidth();
		//this.height = getHeight();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.width, this.height);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, this.width, this.height);
		
		draw(g2d);
		
	}
	
	public MouseMotionListener createMouseMotionListener() {
		final CharacterRecognitionComponent temp = this;

		return new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				
				if(arg0.getX() < temp.x + temp.width && arg0.getY() < temp.y + temp.height 
						&& arg0.getX() >= temp.x && arg0.getY() >= temp.y) {
					System.out.println("mouse drag (charRecog): x: " + arg0.getX() + " y: " + arg0.getY());
					System.out.println("component location: x0: " + temp.x + " y0: " + temp.y 
							+ " x1: " + (temp.x + temp.width) + " y1: " + (temp.y + temp.height));
					temp.pixels[arg0.getX() - temp.x][arg0.getY() - temp.y] = 1;
					time = System.currentTimeMillis();
					//temp.notify();
					temp.repaint();
					
				}

			}

			@Override
			public void mouseMoved(MouseEvent e) {}
			
		};
		
	}
	
	public MouseListener createMouseListener() {
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
				
				
				if(arg0.getX() < temp.x + temp.width && arg0.getY() < temp.y + temp.height 
						&& arg0.getX() >= temp.x && arg0.getY() >= temp.y) {
					System.out.println("mouse press (charRecog): x: " + arg0.getX() + " y: " + arg0.getY());
					System.out.println("component location: x0: " + temp.x + " y0: " + temp.y 
							+ " x1: " + (temp.x + temp.width) + " y1: " + (temp.y + temp.height));
					temp.pixels[arg0.getX() - temp.x][arg0.getY() - temp.y] = 10;
					time = System.currentTimeMillis();
					//temp.notify();
					temp.repaint();
					
				}
				
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
				if(pixels[x][y] != 0) {
					//System.out.println("draw point (char recog): x: " + x + " y: " + y);
					g2d.fillOval(x, y, 10, 10);
					//g2d.draw(new Line2D.Double(x + this.x, y + this.y, x + this.x + 1, y + this.y + 1));
				}
		
	}

	@Override
	public void run() {
		
		while(running) {
			if(System.currentTimeMillis() - time < 10000)
				continue;
			
//			for(int i = 0; i < pixels.length; i++) {
//				for(int j = 0; j < pixels[0].length; j++)
//					System.out.print(pixels[i][j] + " ");
//				System.out.println();
//			}
			
			vafusion.recog.Character c = charRecog.match(getImage());
			System.out.println(c);
			clear();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void stop() {
		
		running = false;
		
	}
	
	public void clear() {
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				pixels[i][j] = 0;
		
		repaint();
		
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
