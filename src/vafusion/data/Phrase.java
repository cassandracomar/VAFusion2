package vafusion.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jm.music.data.Note;

public class Phrase {

	List<Note> notes;
	
	public Phrase() {
		
		notes = new LinkedList<Note>();
		
	}
	
	public Phrase(jm.music.data.Phrase phrase) {
		
		this();
		notes.addAll(phrase.getNoteList());
		
	}
	
	public Phrase(List<Note> notes) {
		
		this();
		this.notes.addAll(notes);
		
	}
	
	public jm.music.data.Phrase getJMPhrase() {
		
		return new jm.music.data.Phrase(notes.toArray(new Note[0]));
		
	}
	
	public void add(Note n) {
		
		notes.add(n);
		
	}
	
	public void add(Note n, int pos) {
		
		notes.add(pos, n);
		
	}
	
	public Note remove(int pos) {
		
		return notes.remove(pos);
		
	}
	
	public void addAll(List<Note> notes) {
		
		this.notes.addAll(notes);
		
	}
	
	public void addAll(Note[] notes) {
		
		this.notes.addAll(Arrays.asList(notes));
		
	}
	
	public void addAll(jm.music.data.Phrase p) {
		
		this.notes.addAll(p.getNoteList());
		
	}
	
	public void addAll(List<Note> notes, int pos) {
		
		this.notes.addAll(pos, notes);
		
	}
	
	public void addAll(Note[] notes, int pos) {
		
		this.notes.addAll(pos, Arrays.asList(notes));
		
	}
	
	public void addAll(jm.music.data.Phrase p, int pos) {
		
		this.notes.addAll(pos, p.getNoteList());
		
	}
	
	public Note get(int pos) {
		
		return this.notes.get(pos);
		
	}
	
	public jm.music.data.Phrase getRange(int pos, int endPos) {
		
		return new jm.music.data.Phrase(notes.subList(pos, endPos).toArray(new Note[0]));
		
	}
	
	public void replaceRange(int pos, int endPos, List<Note> newNotes) {
		
		removeRange(pos, endPos);
		notes.addAll(pos, newNotes);
		
	}
	
	public void removeRange(int pos, int endPos) {
		
		for(int i = pos; i <= endPos; i++)			
			notes.remove(i);
		
	}
	
	public List<Note> getAll() {
		
		return notes;
		
	}
	
}
