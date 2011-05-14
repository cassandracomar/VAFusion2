package vafusion.data;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
 // generate phrase subset

import vafusion.music.Measure;


public class Line {
		private int x, y, width, height;
		private Line2D.Double[] lines;
		private Clef clef;
		private ArrayList<Measure> measures;
		private int[] noteRange;		// if the note is outside of the range, it needs ledger lines
		private Graphics2D g2d;
		private int currentMeasure;		// track which measure you're on, tell you when to move to next line
		private int maxMeasures;
		
		protected enum Clef {TREBLE, BASS};
		protected HashMap<Integer, Integer> treblePositionsFlat;
		protected HashMap<Integer, Integer> bassPositionsFlat;
		protected HashMap<Integer, Integer> treblePositionsSharp;
		protected HashMap<Integer, Integer> bassPositionsSharp;
		
		public Line(int x, int y, int width, int height, int clef){
			this.x = x;
			this.y = y;
			this.height = 50;
			this.width = width;
			this.measures = new ArrayList<Measure>();
			this.currentMeasure = 0;
			this.maxMeasures = 5;
			
			if(clef == 1){
				this.clef = Clef.TREBLE;
			}else{
				this.clef = Clef.BASS;
			}
			
			this.noteRange = getNoteRange(this.clef);
			
			this.treblePositionsFlat = initTreblePositionsFlat();
			this.bassPositionsFlat = initBassPositionsFlat();
			this.treblePositionsSharp = initTreblePositionsSharp();
			this.bassPositionsSharp = initBassPositionsSharp();
			
			/*for(int i = 0; i< 5; i ++){
				this.lines[i] = new Line2D.Double(0, y + (this.height/5)*i, this.width, y + (this.height/5)*i);
			}*/
			
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
		
		public int addNote(int pitch, int rhythm, boolean flat, int staffNum){	// TODO: replace int with note
			// try to add the note to the measure
			// if the last measure was consumed, return staffNum + 1
			
			return 0;
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
		
		public int getX() {
			
			return x;
			
		}
		
		public int getY() {
			
			return y;
			
		}
	}