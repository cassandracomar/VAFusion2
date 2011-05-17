package vafusion.music;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Note {
	private Graphics2D g2d;
	private boolean filled;
	private boolean isNote;
	int x, y, height, width, pos;
	int note, accidental;
	double rhythm;
	public static BufferedImage sharp, flat, eighth, sixteenth, quarter;
	
	boolean isSelected;
	
	
	public Note(int pos, double rhythm, boolean isNote, int accidental){
		
		this.filled = isFilled(rhythm);
		
		this.isNote = isNote;
		this.pos = pos;
		this.rhythm = rhythm;
		this.accidental = accidental;
		isSelected = false;
		
		if(sharp == null){
			try {
				sharp = loadImage(new File("img/Notes/sharp-small.gif"));
				flat = loadImage(new File("img/Notes/flat-small.gif"));
				eighth = loadImage(new File("img/Notes/EighthRest.gif"));
				quarter = loadImage(new File("img/Notes/quarter.gif"));
				sixteenth = loadImage(new File("img/Notes/sixteenth rest.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
		if(this.isSelected)
			g2d.setColor(Color.CYAN);
		else
			g2d.setColor(Color.BLACK);
		
		if(this.isNote){
			if(this.filled){
				//fillOval
				g2d.fillOval(x, y, width, height);
				g2d.draw(getStem());
				
				if(rhythm < 1){
					g2d.draw(getFlag());
					if(rhythm < .5)
						g2d.draw(getSixteenthFlag());
				}
			}else{
				//draw a ellipse2d
				g2d.draw(new Ellipse2D.Double(x, y, width, height));
				if(rhythm < 4)
					g2d.draw(getStem());
			}
			
			if(accidental != 0){
				if(accidental == -1){
					g2d.drawImage(flat, null, x - 5, y - 5);
					
					System.out.println("flat: " + x + ", "+ y);
				}else{
					g2d.drawImage(sharp, null, x, y);
				}
			}
		}else{
			if(this.filled){
				if(rhythm == .25)
					g2d.drawImage(sixteenth, null, x, y);
				if(rhythm == .5)
					g2d.drawImage(eighth, null, x, y);
				if(rhythm == 1)
					g2d.drawImage(quarter, null, x, y);
				
			}else{
				g2d.fillRect(x, y, width, height);
			}
			
		}
		
		
	}
	
	public Line2D.Double getFlag(){
		if(pos > 4) //stem goes up
			return new Line2D.Double(x+ width, y+height/2 - height * 5, x+ width * 2, y+ height/2-height * 2.5);
		
		return new Line2D.Double(x, y+height/2 + height*5, x + width, y + height/2 + height * 2.5);
	}
	
	public Line2D.Double getSixteenthFlag(){
		if(pos > 4) //stem goes up
			return new Line2D.Double(x+ width, y+height/2 - height * 2.5, x+ width * 2, y+ height/2);
		
		return new Line2D.Double(x, y+height/2 + height*2.5, x + width, y + height/2 + height);
	}
	
	public Line2D.Double getStem(){
		if(pos > 4) //stem goes up
			return new Line2D.Double(x+width, y + height/2, x+width, y+ height/2 - height * 5);
		
		return new Line2D.Double(x, y + height/2, x, y+ height/2 + height * 5);
	}
	
	public boolean isFilled(double rhythm){
		if(rhythm >= 2)
			return false;
		else
			return true;
	}
	
	public void update(int x, int y) {
		
		this.x = x;
		this.y = y + ((6/2) * pos-2) -1;
		this.width = (int)(6 * 2.5);
		this.height = 6;
		
	}
	
	public static BufferedImage loadImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		return image;
	}
	
	public void select() {
		
		isSelected = true;
		
	}
	
	public void unselect() {
		
		isSelected = false;
		
	}
	
	public boolean isSelected() {
		
		return isSelected;
		
	}
	
	public int getX() {
		
		return x;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
		
}