package vafusion.music;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Staff {
	private Line2D.Double[] lines;
	private Graphics2D g2d;
	private List<Measure> measures;
	private int width, height;
	private vafusion.data.Line staffData;
	
	public Staff(int x, int y, int width, int height, int clef){
		this.lines = new Line2D.Double[5];
		this.width = width;
		this.height = height;
		staffData = new vafusion.data.Line(x, y, width, height, clef);

		
		for(int i = 0; i< 5; i ++){
			this.lines[i] = new Line2D.Double(x, y + (height/5)*i, x + width, y + (height/5)*i);
		}
		
		measures = new ArrayList<Measure>();
		
	}
	
	public void paint(Graphics g){
		this.g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		for(Line2D.Double line : this.lines){
			g2d.draw(line);
		}
		
		for(Measure m : measures)
			m.paint(g2d);
		
		measures.clear();
		
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
		
		int currX = this.staffData.getX();
		System.out.println(measures.size());
		for(Measure m : measures) {
			
			m.update(currX, staffData.getY(), this.height);
			currX += m.getWidth();
			
		}
		
	}
	
	public int getLineSeparation() {
		
		return height / 5;
		
	}
	
}

