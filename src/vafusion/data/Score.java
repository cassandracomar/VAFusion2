package vafusion.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jm.music.data.Note;

import vafusion.music.Measure;
import vafusion.music.Staff;

public class Score {
	private int numLines;
//	private int numParts;
	private int height, width, x, y;
	private boolean defaultFlat;
//	private jm.music.data.Part[] parts;
//	private int currentPart;
	private int num;
	private int denom;
	private List<Staff> staves;
	protected enum Clef {TREBLE, BASS};
	Phrase notes;
	
	
	public Score(int x, int y, int width, int height, int numLines){
		this.setHeight(height);
		this.setWidth(width);
		this.setX(x);
		this.setY(y);
		this.defaultFlat = true;
		this.numLines = numLines;
		staves = new ArrayList<Staff>();
		this.num = 4;
		this.denom = 4;
		notes = new Phrase();
		
		int betweenSpace = ((height / numLines + 1) / 5) * 2;
		int fromTop = betweenSpace;
		int staffHeight = (height - betweenSpace * (numLines - 1) - fromTop * 2) / numLines;
		
		int leftPadding = (int)(width * 0.025);
		

		for(int i = 0; i< numLines; i++){
			staves.add(new Staff(x + leftPadding, y + staffHeight * (i) + (betweenSpace * i) + fromTop, 
					width - leftPadding * 2, staffHeight, 1));
		}
	}
	
	public void setDefaultFlat(boolean bool){
		this.defaultFlat = bool;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}
	
	public List<Staff> getStaves() {
		
		return staves;
		
	}
	
	public void addNote(Note n) {
		
		notes.add(n);
		
	}
	
	public void deleteNote(int pos) {
		
		notes.remove(pos);
		
	}
	
	/*
	 * 
	 * Use for more complicated manipulations of the notes
	 * 
	 */
	public Phrase getPhrase() {
		
		return notes;
		
	}
	
	/*
	 * 
	 * measure width = number of notes * 2.5 * staffLineSeparation + (numberofnotes +1)*width between notes
	 * 
	 */
	public void drawNotes() {
		
		List<Note> remainingNotes = new LinkedList<Note>();
		remainingNotes.addAll(notes.getAll());
		List<Measure> measures = new ArrayList<Measure>();
		int staffIndex = 0;
		
		Note remainder = null;
		while(remainingNotes.size() > 0) {
			
			Measure m = new Measure(1, defaultFlat, staves.get(staffIndex).getLineSeparation());
			if(remainder != null) {
				m.addNote(remainder);
				m.setTieIn(true);
				remainder = null;
			}
			while(remainder == null && remainingNotes.size() > 0)
				remainder = m.addNote(remainingNotes.remove(0));
			
			if(!staves.get(staffIndex).addMeasure(m)) {
				
				//add to the next line if possible
				if(!(staffIndex < staves.size() - 1)) {//need to add a new staff...
					System.out.println("Adding new staff!");
					int betweenSpace = ((height / numLines + 1) / 5) * 2;
					int fromTop = betweenSpace;
					int staffHeight = (height - betweenSpace * (numLines - 1) - fromTop * 2) / numLines;
					
					int leftPadding = (int)(width * 0.025);
					
					staves.add(new Staff(x + leftPadding, y + staffHeight * (staves.size()) + 
							(betweenSpace * staves.size()) + fromTop, width - leftPadding * 2, staffHeight, 1));

				}
				staves.get(++staffIndex).addMeasure(m);
				
			}
			measures.add(m);
			
		}
		
		for(Staff s : staves)
			s.update();
			
		
	}
}
