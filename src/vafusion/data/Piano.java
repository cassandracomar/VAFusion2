package vafusion.data;

import java.util.ArrayList;
import java.util.HashSet;

import jm.audio.Instrument;
import jm.music.data.Note;
import jm.music.data.Phrase;
import jm.music.data.Score;

import vafusion.gui.KeyComponent;


public class Piano {
	private vafusion.gui.KeyComponent[] blackKeys, whiteKeys;
	private static vafusion.gui.KeyComponent[] allKeys = new vafusion.gui.KeyComponent[88];
	private int x, y, height, width, offset, numKeys;
	private boolean recording;
	private HashSet<Note> currentChord = new HashSet<Note>();
	private double currentRhythm;
	Score recordedScore = new Score();
    Phrase recordedPhrase = new Phrase();


	public Piano(int x, int y, int width, int height, int offset){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.offset = offset;
		this.numKeys = 8; //number of white keys -- eight fits comfortably on the screen.
		this.recording = false;
		ArrayList<vafusion.gui.KeyComponent> blackTemp = new ArrayList<vafusion.gui.KeyComponent>();
		ArrayList<vafusion.gui.KeyComponent> whiteTemp = new ArrayList<vafusion.gui.KeyComponent>();
		recordedPhrase.setInstrument(jm.music.data.Phrase.PIANO);
		
		if(allKeys[0] == null)
			generateAllKeys();
		
		int whiteKeys = 0, count = 0;
		while(whiteKeys < 8){
			Key key = allKeys[offset+count].getKey();
			if(key.isWhite()){
				whiteTemp.add(new vafusion.gui.KeyComponent(x + whiteKeys*width/numKeys, y, key.getHeight(), key.getWidth(), key.getNote(), true));
				whiteKeys++;
			}else{
				blackTemp.add(new vafusion.gui.KeyComponent(x + whiteKeys*(width/numKeys), y, key.getHeight(), key.getWidth(), key.getNote(), false));
			}
			
			count ++;
		}
		
		this.blackKeys = blackTemp.toArray(new KeyComponent[0]);
		this.whiteKeys = whiteTemp.toArray(new KeyComponent[0]);
	}
	public void generateAllKeys(){
		//offset for pitch value is 21 (a0 = 21)
		boolean isWhite;
//		int pitchOffset = 21;
		int pitchOffset = 1;
		int keyWidth = this.width / this.numKeys;
		
		for(int i = 0; i<88; i++){
			switch (i%12){
			case 1:
			case 3:
			case 6:
			case 8:
			case 10:	isWhite = false;
						break;
			default:	isWhite = true;
			}
			
			if(isWhite)
				allKeys[i] = new vafusion.gui.KeyComponent(0, 0, this.height, keyWidth, i+ pitchOffset, isWhite);
			else
				allKeys[i] = new vafusion.gui.KeyComponent(0, 0, this.height / 2, (keyWidth * 2)/3, i+ pitchOffset, isWhite);
		}
	}
		
	public vafusion.gui.KeyComponent[] getBlackKeys() {
		return blackKeys;
	}

	public void setBlackKeys(vafusion.gui.KeyComponent[] blackKeys) {
		this.blackKeys = blackKeys;
	}

	public vafusion.gui.KeyComponent[] getWhiteKeys() {
		return whiteKeys;
	}

	public void setWhiteKeys(vafusion.gui.KeyComponent[] whiteKeys) {
		this.whiteKeys = whiteKeys;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public void setRecording(boolean recording){
		this.recording = recording;
		
		if(recording){
			System.out.println("Started recording");
		}else{
			System.out.println("Stopped recording");
		}
	}
	
	public boolean isRecording(){
		return recording;
	}
	
	public vafusion.gui.KeyComponent getKeyComponentAt(int x, int y){
		for(vafusion.gui.KeyComponent k: blackKeys){
			if(k.getKey().getClickableArea().contains(x, y)){
				return k;
			}
		}
		
		for(vafusion.gui.KeyComponent k: whiteKeys){
			if(k.getKey().getClickableArea().contains(x, y)){
				return k;
			}
		}
		
		return null;
	}
	public HashSet<Note> getCurrentChord() {
		return currentChord;
	}
	public void setCurrentChord(HashSet<Note> currentChord) {
		this.currentChord = currentChord;
	}
	public double getCurrentRhythm() {
		return currentRhythm;
	}
	public void setCurrentRhythm(double currentRhythm) {
		this.currentRhythm = currentRhythm;
	}
	public Score getRecordedScore() {
		return recordedScore;
	}
	public void setRecordedScore(Score recordedScore) {
		this.recordedScore = recordedScore;
	}
	public Phrase getRecordedPhrase() {
		return recordedPhrase;
	}
	public void setRecordedPhrase(Phrase recordedPhrase) {
		this.recordedPhrase = recordedPhrase;
	}

	public void setInstrument(int instrument) {
		
		this.recordedPhrase.setInstrument(instrument);
		
	}
	
	public int getInstrument() {
		
		return this.recordedPhrase.getInstrument();
		
	}
}
