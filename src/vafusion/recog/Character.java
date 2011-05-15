package vafusion.recog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Character implements Unit {
	
	public static final int NUM_CHARACTERS = 20;
	private static HashMap<java.lang.Character, Character> charmap = null;
	private Rhythm rhythm;
	
	
	private Character(Rhythm r) {
		
		rhythm = r;
		
	}
	
	public static enum Rhythm implements Type {
		
		NONE,
		SIXTEENTH,
		DOTTED_SIXTEENTH,
		EIGTH,
		DOTTED_EIGTH,
		QUARTER,
		DOTTED_QUARTER,
		HALF,
		DOTTED_HALF,
		WHOLE,
		DOTTED_WHOLE,
		WHOLE_REST,
		DOTTED_WHOLE_REST,
		HALF_REST,
		DOTTED_HALF_REST,
		QUARTER_REST,
		DOTTED_QUARTER_REST,
		EIGTH_REST,
		DOTTED_EIGTH_REST,
		SIXTEENTH_REST,
		DOTTED_SIXTEENTH_REST
		
	}
	
	public static List<String> getCharacters() {
		
		String[] chars = {"N", "0", "7", "1", "2", "3", "4", "5", "6", "8", "9", "Q", "W", "E", "R", "S", "D", "Z", "X", "C", "H"};
		return Arrays.asList(chars);
		
	}
	
	public static Character mapCharacter(java.lang.Character c) {
		
		if(charmap == null) {
			
			charmap = new HashMap<java.lang.Character, Character>();
			String[] chars = {"N", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Q", "W", "E", "R", "S", "D", "Z", "X", "C", "H"};
			Character[] notes = {
				new Character(Rhythm.NONE), 
				new Character(Rhythm.DOTTED_SIXTEENTH), 
				new Character(Rhythm.DOTTED_HALF), 
				new Character(Rhythm.HALF_REST), 
				new Character(Rhythm.DOTTED_HALF_REST), 
				new Character(Rhythm.QUARTER_REST), 
				new Character(Rhythm.DOTTED_QUARTER_REST), 
				new Character(Rhythm.SIXTEENTH_REST), 
				new Character(Rhythm.DOTTED_EIGTH), 
				new Character(Rhythm.EIGTH_REST), 
				new Character(Rhythm.DOTTED_EIGTH_REST), 
				new Character(Rhythm.QUARTER), 
				new Character(Rhythm.WHOLE), 
				new Character(Rhythm.EIGTH), 
				new Character(Rhythm.DOTTED_SIXTEENTH_REST), 
				new Character(Rhythm.SIXTEENTH), 
				new Character(Rhythm.DOTTED_QUARTER), 					
				new Character(Rhythm.DOTTED_WHOLE), 
				new Character(Rhythm.WHOLE_REST), 
				new Character(Rhythm.DOTTED_WHOLE_REST), 					
				new Character(Rhythm.HALF)
			};
			
			for(int i = 0; i < chars.length; i++)
				charmap.put(chars[i].charAt(0), notes[i]);
			
		}
		return charmap.get(c);
		
	}
	
	public Rhythm getRhythm() {
		
		return rhythm;
		
	}
	
	public double getRhythmValue() {
		
		switch(rhythm) {
		
		case DOTTED_WHOLE:
		case DOTTED_WHOLE_REST:
			return 6.0;
		case WHOLE:
		case WHOLE_REST:
			return 4.0;
		case DOTTED_HALF:
		case DOTTED_HALF_REST:
			return 3.0;
		case HALF:
		case HALF_REST:
			return 2.0;
		case DOTTED_QUARTER:
		case DOTTED_QUARTER_REST:
			return 1.5;
		case QUARTER:
		case QUARTER_REST:
			return 1.0;
		case DOTTED_EIGTH:
		case DOTTED_EIGTH_REST:
			return 0.75;
		case EIGTH:
		case EIGTH_REST:
			return 0.5;
		case DOTTED_SIXTEENTH:
		case DOTTED_SIXTEENTH_REST:
			return 6.0;
		case SIXTEENTH:
		case SIXTEENTH_REST:
			return 0.25;
		default:
			return 0;
		
		}
		
	}
	
public String toString() {
		
		switch(rhythm) {
		
		case DOTTED_WHOLE:
			return "Dotted Whole Note";
		case DOTTED_WHOLE_REST:
			return "Dotted Whole Rest";
		case WHOLE:
			return "Whole Note";
		case WHOLE_REST:
			return "Whole Rest";
		case DOTTED_HALF:
			return "Dotted Half Note";
		case DOTTED_HALF_REST:
			return "Dotted Half Rest";
		case HALF:
			return "Half Note";
		case HALF_REST:
			return "Half Rest";
		case DOTTED_QUARTER:
			return "Dotted Quarter Note";
		case DOTTED_QUARTER_REST:
			return "Dotted Quarter Rest";
		case QUARTER:
			return "Quarter Note";
		case QUARTER_REST:
			return "Quarter Rest";
		case DOTTED_EIGTH:
			return "Dotted Eigth Note";
		case DOTTED_EIGTH_REST:
			return "Dotted Eigth Rest";
		case EIGTH:
			return "Eigth Note";
		case EIGTH_REST:
			return "Eigth Rest";
		case DOTTED_SIXTEENTH:
			return "Dotted Sixteenth Note";
		case DOTTED_SIXTEENTH_REST:
			return "Dotted Sixteenth Rest";
		case SIXTEENTH:
			return "Sixteenth Note";
		case SIXTEENTH_REST:
			return "Sixteenth Rest";
		default:
			return "Not a recognized character.";
		
		}
		
	}
	
	public boolean isRest() {
		

		switch(rhythm) {
		
		
		case DOTTED_WHOLE_REST:
		case WHOLE_REST:
		case DOTTED_HALF_REST:
		case HALF_REST:
		case DOTTED_QUARTER_REST:
		case QUARTER_REST:
		case DOTTED_EIGTH_REST:
		case EIGTH_REST:
		case DOTTED_SIXTEENTH_REST:
		case SIXTEENTH_REST:
			return true;
		default:
			return false;
		
		}
		
		
	}
	
}
