package vafusion.music;

import java.util.ArrayList;

public class Score {
	private int numLines;
	private int numParts;
	private int height, width, x, y;
	private boolean defaultFlat;
	private ArrayList<Part> parts;
	private int currentPart;
	
	
	Score(int x, int y, int height, int width, int numParts){
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.defaultFlat = true;
		this.parts = new ArrayList<Part>();
		this.numParts = numParts;
		this.currentPart = 0;
	}
}
