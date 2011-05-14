package vafusion.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import jm.music.data.Note;
import jm.util.Play;

@SuppressWarnings("serial")
public class KeyComponent extends JComponent{
	
	private vafusion.data.Key key;
	
	public KeyComponent(int x, int y, int height, int width, int note, boolean isWhite){
		this.key = new vafusion.data.Key(x, y, height, width, note, isWhite);
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		
		if(key.isWhite()){
			g2d.draw(key.getClickableArea());
		}else{
			g2d.fill(key.getClickableArea());
		}
		
		if(key.isPressed()){
			g2d.setColor(Color.cyan);
			g2d.fill(key.getHighlightArea());
		}else{
			if(key.isWhite()){
				g2d.setColor(Color.white);
				g2d.fill(key.getHighlightArea());
			}else{
				g2d.setColor(Color.black);
				g2d.fill(key.getHighlightArea());
			}
		}
	}

	public void press(){
		key.setPressed(true);
		((PianoComponent) this.getParent()).getPiano().getCurrentChord().add(new Note(key.getNote() , ((PianoComponent) this.getParent()).getPiano().getCurrentRhythm()));
		
		if(((PianoComponent)getParent()).getPiano().isRecording())
			((PianoComponent)this.getParent()).getScore().addNote(new Note(key.getNote() , ((PianoComponent) this.getParent()).getPiano().getCurrentRhythm()));
		this.repaint();
		this.getParent().repaint();
	}
	
	public void unpress(){
		key.setPressed(false);
//		if(((PianoComponent)getParent()).getPiano().isRecording()) {
//    		System.out.println("Recording current phrase");
//    		((PianoComponent)getParent()).getPiano().getRecordedPhrase().addNoteList(((PianoComponent)getParent()).getPiano().getCurrentChord().toArray(new Note[0]));
//        }
		
		Note curr = new Note(key.getNote(), ((PianoComponent) this.getParent()).getPiano().getCurrentRhythm());
		for(Note n : ((PianoComponent) this.getParent()).getPiano().getCurrentChord())
			if(n.samePitch(curr)) {
				((PianoComponent) this.getParent()).getPiano().getCurrentChord().remove(n);
				System.out.println("Removed note: " + n);
			}
		
		try {
			Play.stopMidi();
		} catch (NullPointerException e) {
			//e.printStackTrace();
		}
		
		this.repaint();
	}
	
	public vafusion.data.Key getKey(){
		return key;
	}

}
