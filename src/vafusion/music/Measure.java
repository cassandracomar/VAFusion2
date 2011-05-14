package vafusion.music;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



public class Measure {
	private List<Note> notes;
	private boolean tieIn;
	private boolean tieOut;		
	private boolean defaultFlat;
	private int num = 4;
	private int denom = 4;
	private double timeSignature;
	private double duration = 0;
	private int x, y, width, height;
	private int noteSeparation, staffLineHeight;
	public static double NOTE_SEPARATION_CONSTANT = 3.5;
	
	protected enum Clef {TREBLE, BASS};
	private Clef clef;
	protected HashMap<Integer, Integer> treblePositionsFlat;
	protected HashMap<Integer, Integer> bassPositionsFlat;
	protected HashMap<Integer, Integer> treblePositionsSharp;
	protected HashMap<Integer, Integer> bassPositionsSharp;
	private int[] noteRange;
	
	public Measure(int clef, boolean defaultFlat, int lineSeparation) {
		
		timeSignature = (double)num / denom;
		
		if(clef == 1){
			this.clef = Clef.TREBLE;
		}else{
			this.clef = Clef.BASS;
		}
		
		this.noteRange = getNoteRange(this.clef);
		this.defaultFlat = defaultFlat;
		
		this.treblePositionsFlat = initTreblePositionsFlat();
		this.bassPositionsFlat = initBassPositionsFlat();
		this.treblePositionsSharp = initTreblePositionsSharp();
		this.bassPositionsSharp = initBassPositionsSharp();
		this.staffLineHeight = lineSeparation;
		this.noteSeparation = (int)(NOTE_SEPARATION_CONSTANT * lineSeparation);
		
		notes = new LinkedList<Note>();
		
	}
	
	Measure(boolean tieIn){
		this.tieIn = tieIn;
	}
	
	public boolean isTieIn() {
		return tieIn;
	}
	
	public void setTieIn(boolean tieIn) {
		this.tieIn = tieIn;
	}
	
	public boolean isTieOut() {
		return tieOut;
	}
	
	public void setTieOut(boolean tieOut) {
		this.tieOut = tieOut;
	}
	
	public jm.music.data.Note addNote(jm.music.data.Note n) {
		
		if(duration == timeSignature) //this measure is already full!
			return n;
		
		if(n.getRhythmValue() / denom + duration <= timeSignature) {
			
			//we're golden, add the note directly and return null
			int width = (int) (staffLineHeight * NOTE_SEPARATION_CONSTANT); // FIXME
			int height = staffLineHeight; //FIXME
			int pos = getPos(n.getPitch()); //FIXME
			int note = n.getPitchValue();
			double rhythm = n.getRhythmValue();
			
			notes.add(new Note(pos, rhythm, !n.isRest()));
			duration += rhythm / denom;
			
			return null;
			
		} else {
			
			//the note needs to be split
			jm.music.data.Note remainder = new jm.music.data.Note(n.getPitch(), (duration + n.getRhythmValue() / denom) - timeSignature);
			jm.music.data.Note keep = new jm.music.data.Note(n.getPitch(), timeSignature - duration);
			int width = (int)(staffLineHeight * NOTE_SEPARATION_CONSTANT); // FIXME
			int height = 6; //FIXME
			int pos = getPos(n.getPitch()); //FIXME
			int note = keep.getPitchValue();
			double rhythm = keep.getRhythmValue();
			notes.add(new Note(pos, rhythm, !keep.isRest()));
			duration += rhythm / denom;
			setTieOut(true);
				
			return remainder;
		}
		
	}
	
	public int getPos(int note){
		if(defaultFlat){
			if(this.clef == Clef.TREBLE){
				System.out.println("note: " + note);
				return treblePositionsFlat.get(note);
			}else{
				return bassPositionsFlat.get(note);
			}
		}else{
			if(this.clef == Clef.TREBLE){
				return treblePositionsSharp.get(note);
			}else{
				return bassPositionsSharp.get(note);
			}
		}
	}
	
	public int getWidth() {
		
		return (int)(notes.size() * NOTE_SEPARATION_CONSTANT * staffLineHeight + (notes.size() + 1) * noteSeparation);
		
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		//FIXME: Add vertical lines for measure + plus ties, etc.
		
		for(Note n : notes){
			n.paint(g2d);
			//System.out.println(n);
		}
		
		
	}
	
	public List<Note> getNotes() {
		
		return notes;
		
	}
	
	public void update(int x, int y, int height) {
		
		width = getWidth();
		this.height = height;
		//System.out.println(notes.size());
		this.noteSeparation = (int)(height/5 * NOTE_SEPARATION_CONSTANT);
		
		for(int i = 0; i < notes.size(); i++)
			notes.get(i).update(x + i * noteSeparation, y, height / 6);
		
	}
	
	private HashMap<Integer, Integer> initTreblePositionsFlat(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(61, 10); //Db
		map.put(62, 10); //D
		map.put(63, 9); //Eb
		map.put(64, 9); //E
		map.put(65, 8); //F
		map.put(66, 7); //Gb
		map.put(67, 7); //G
		map.put(68, 6); //Ab
		map.put(69, 6); //A
		map.put(70, 5); //Bb
		map.put(71, 5); //B
		map.put(72, 4); //C
		map.put(73, 3); //Db
		map.put(74, 3); //D
		map.put(75, 2); //Eb
		map.put(76, 2); //E
		map.put(77, 1); //F
		map.put(78, 0); //Gb
		map.put(79, 0); //G
		
		return map;
	}
	
	private HashMap<Integer, Integer> initTreblePositionsSharp(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(62, 10); //D
		map.put(63, 10); //D#
		map.put(64, 9); //E
		map.put(65, 8); //F
		map.put(66, 8); //F#
		map.put(67, 7); //G
		map.put(68, 7); //G#
		map.put(69, 6); //A
		map.put(70, 6); //A#
		map.put(71, 5); //B
		map.put(72, 4); //C
		map.put(73, 4); //C#
		map.put(74, 3); //D
		map.put(75, 3); //D#
		map.put(76, 2); //E
		map.put(77, 1); //F
		map.put(78, 1); //F#
		map.put(79, 0); //G
		map.put(80, 0); //G#
		
		return map;
	}
	
	private HashMap<Integer, Integer> initBassPositionsFlat(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(41, 10); //F
		map.put(42, 9); //Gb
		map.put(43, 9); //G
		map.put(44, 8); //Ab
		map.put(45, 8); //A
		map.put(46, 7); //Bb
		map.put(47, 7); //B
		map.put(48, 6); //C
		map.put(49, 5); //Db
		map.put(50, 5); //D
		map.put(51, 4); //Eb
		map.put(52, 4); //E
		map.put(53, 3); //F
		map.put(54, 2); //Gb
		map.put(55, 2); //G
		map.put(56, 1); //Ab
		map.put(57, 1); //A
		map.put(58, 0); //Bb
		map.put(59, 0); //B
		
		return map;
	}
	
	private HashMap<Integer, Integer> initBassPositionsSharp(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(41, 10); //F
		map.put(42, 10); //F#
		map.put(43, 9); //G
		map.put(44, 9); //G#
		map.put(45, 8); //A
		map.put(46, 8); //A#
		map.put(47, 7); //B
		map.put(48, 6); //C
		map.put(49, 6); //C#
		map.put(50, 5); //D
		map.put(51, 5); //D#
		map.put(52, 4); //E
		map.put(53, 3); //F
		map.put(54, 3); //F#
		map.put(55, 2); //G
		map.put(56, 1); //Ab
		map.put(57, 1); //A
		map.put(58, 0); //Bb
		map.put(59, 0); //B
		
		return map;
	}
	
	int[] getNoteRange(Clef clef){
		int[] range = new int[2];
		
		switch (clef){
			case TREBLE:	range[0] = 61;		// numeric value of CS4/DF4
							range[1] = 80;		// value of GS5/AF5
							break;
				
			case BASS:		range[0] = 41;		// value of F2
							range[1] = 59;		// value of B3
							break;
				
			default:	System.out.println("Unrecognized clef in getNoteRange()");
		}
		
		return range;
	}
}
