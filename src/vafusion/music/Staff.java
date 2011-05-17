package vafusion.music;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Staff {
	private Line2D.Double[] lines;
	private Graphics2D g2d;
	private List<Measure> measures, measureBackup;
	private int width, height, x, y;
	private vafusion.data.Line staffData;
	private BufferedImage clef;
	private static BufferedImage bass;
	private static BufferedImage treble;
	
	public Staff(int x, int y, int width, int height, int clef){
		this.lines = new Line2D.Double[6];
		this.width = width;
		this.height = height;
		staffData = new vafusion.data.Line(x, y, width, height, clef);
		this.x = x;
		this.y = y;
		
		for(int i = 0; i< 5; i ++){
			this.lines[i] = new Line2D.Double(x, y + (height/5)*i, x + width, y + (height/5)*i);
		}
		
		this.lines[5] = new Line2D.Double(x+width, y, x + width, y + (height/5) * 4);
		
		if(bass == null){
			try {
				bass = loadImage(new File("img/Notes/bassclef.gif"));
				treble = loadImage(new File("img/Notes/treble-clef.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(clef == 1){
			this.clef = treble;
		}else{
			this.clef = bass;
		}
		
		measures = new ArrayList<Measure>();
		measureBackup = new ArrayList<Measure>();
		
	}
	
	public void paint(Graphics g){
		this.g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		for(Line2D.Double line : this.lines){
			g2d.draw(line);
		}
		
		g2d.drawImage(clef, null, x, y);
		
		Measure prev = null;
		
		for(Measure m : measures){
			m.paint(g2d);
			
			if(prev != null){
				List<Note> notes1 = prev.getNotes();
				List<Note> notes2 = m.getNotes();
				int first = notes2.get(0).getX();
				int last = notes1.get(notes1.size()-1).getX();
				double avg = (first + last) * .5 + 10;
				
				g2d.draw(new Line2D.Double(avg, y, avg, y + (height/5) * 4));
			}
			
			prev = m;
		}
		
	}
	
	public boolean addMeasure(Measure m) {
		
		int measureWidthTotal = 0;
		for(Measure i : measures)
			measureWidthTotal += i.getWidth();
		
		if(m.getWidth() + measureWidthTotal > width)
			return false;
		else {
			
			measures.add(m);
			return true;
		
		}
			
		
	}
	
	public void update() {
		
		int currX = this.staffData.getX() + this.clef.getWidth() + 10;
		//System.out.println(measures.size());
		for(Measure m : measures) {
			
			m.update(currX, staffData.getY(), this.height);
			currX += m.getWidth();
			
		}
		
	}
	
	public int getLineSeparation() {
		
		return height / 5;
		
	}
	
	public static BufferedImage loadImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		return image;
	}
	
	public int getX() {
		
		return this.x;
		
	}
	
	public int getY() {
		
		return this.y;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public synchronized vafusion.music.Note getNote(int x) {
		
		System.out.println("Staff.getNote x: " + x);

		List<Measure> measureList = measures.size() == 0 ? measureBackup : measures;
		if(measureBackup.size() == 0)
			System.out.println("WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		System.out.println("Measures size: " + measureList.size());
		//figure out which measure the note is in
		for(Measure m : measureList){
			System.out.println("measure: x1: " + m.getX() + " x2: " + (m.getX() + m.getWidth()));
			if(x >= m.getX() && x <= m.getX() + m.getWidth())
				return m.getNote(x);
		}
		
		return null;
		
	}

	public synchronized void clearMeasures() {
		
		if(measures.size() != 0) {
			
			//measureBackup.clear();
			measureBackup.addAll(measures);
			measures.clear();
			System.out.println("Clearing measures..." + measureBackup.size());
			
		}
		
	}
	
	public int numMeasures() {
		
		return measures.size();
		
	}
}

