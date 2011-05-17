package vafusion.gui;

// 331-346

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import jm.JMC;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;

@SuppressWarnings("serial")
public class Pianel extends JFrame implements JMC {
    
    public static void main(String args[]) throws InterruptedException {
    
        Pianel pianel = new Pianel(0, 8);
        while(pianel.running) Thread.sleep(1000);
        pianel.shutdown();
    
    }

    PianoComponent pianoComponent;
    public StaffComponent staff;
    public CharacterRecognitionComponent charRecog;
    @SuppressWarnings("unused")
	private Phrase wholePhrase;
    @SuppressWarnings("unused")
	private int offset, xOffset, yOffset;
    boolean blackPattern[] = {true, true, false, true, true, true, false, false};
    double twoThirds = (double)2/3;
    double oneThird = (double)1/3;
    boolean running = true;
    
    Score recordedScore = new Score();
    Phrase recordedPhrase = new Phrase();
	private JButton recordingButton, showScore;
	boolean recording;
	HashMap<String, Double> rhythmMap = new HashMap<String, Double>();
	JComboBox rhythmSelector;
	
	void makeRecordingButton() {
		
		recordingButton = new JButton();
        recording = false;
        recordingButton.setText("Record");
        recordingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!pianoComponent.getPiano().isRecording()) {
					
					pianoComponent.getPiano().setRecording(true);
					recordingButton.setText("Stop");
					pianoComponent.getPiano().setRecordedPhrase(new Phrase());
				
					
				} else {
					
					pianoComponent.getPiano().setRecording(false);
					recordingButton.setText("Record");
					if(pianoComponent.getPiano().getRecordedPhrase().getSize() != 0)
						pianoComponent.getPiano().getRecordedScore().getPart(0).add(pianoComponent.getPiano().getRecordedPhrase());
					
				}	
			}
        });
	}
	
	void makeScoreButton() {
		
		pianoComponent.getPiano().getRecordedScore().createPart();
        
        showScore = new JButton();
        showScore.setText("Show Piece");
        showScore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(recordedScore);
				View.show(pianoComponent.getPiano().getRecordedScore());
				Write.midi(pianoComponent.getPiano().getRecordedScore(), "Untitled Score.midi");
				
			}
			
		});
		
	}
	
	void prepRhythms() {
		
		String[] rhythms = {"Sixteenth Note", "Eighth Note", "Quarter Note", "Half Note", "Whole Note"};
        double startDuration = SIXTEENTH_NOTE;
        for(int i = 0; i < rhythms.length; i++)
        	rhythmMap.put(rhythms[i], (startDuration * Math.pow(2, i)));
        rhythmSelector = new JComboBox(rhythms);
        rhythmSelector.setSelectedItem(rhythms[2]);
        pianoComponent.getPiano().setCurrentRhythm(C);
        
        rhythmSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String newRhythm = (String)rhythmSelector.getSelectedItem();
				pianoComponent.getPiano().setCurrentRhythm(rhythmMap.get(newRhythm));
				rhythmSelector.setSelectedItem(newRhythm);
				
			}
        	
        });
		
	}
	
	void setupLayout() {
		
		SpringLayout spring = new SpringLayout();

        // Add a component with a custom paint method
        this.getContentPane().setLayout(spring);
        
        getContentPane().add(recordingButton);
        getContentPane().add(rhythmSelector);
        getContentPane().add(showScore);
        //getContentPane().add(piano);
        getContentPane().add(pianoComponent);
        getContentPane().add(staff);
        getContentPane().add(charRecog);
        
        spring.putConstraint(SpringLayout.NORTH, pianoComponent, pianoComponent.getY(), SpringLayout.NORTH, getContentPane());
        spring.putConstraint(SpringLayout.WEST, pianoComponent, pianoComponent.getX(), SpringLayout.WEST, getContentPane());
        spring.putConstraint(SpringLayout.HEIGHT, pianoComponent, pianoComponent.getHeight(), SpringLayout.HEIGHT, getContentPane());
        spring.putConstraint(SpringLayout.WIDTH, pianoComponent, pianoComponent.getWidth(), SpringLayout.WIDTH, getContentPane());
        
        spring.putConstraint(SpringLayout.NORTH, staff, staff.getY(), SpringLayout.NORTH, getContentPane());
        spring.putConstraint(SpringLayout.WEST, staff, staff.getX(), SpringLayout.WEST, getContentPane());
        spring.putConstraint(SpringLayout.HEIGHT, staff, staff.getHeight(), SpringLayout.HEIGHT, getContentPane());
        spring.putConstraint(SpringLayout.WIDTH, staff, staff.getWidth(), SpringLayout.WIDTH, getContentPane());
        
        spring.putConstraint(SpringLayout.NORTH, charRecog, charRecog.getY(), SpringLayout.NORTH, getContentPane());
        spring.putConstraint(SpringLayout.WEST, charRecog, charRecog.getX(), SpringLayout.WEST, getContentPane());
        spring.putConstraint(SpringLayout.HEIGHT, charRecog, charRecog.getHeight(), SpringLayout.HEIGHT, getContentPane());
        spring.putConstraint(SpringLayout.WIDTH, charRecog, charRecog.getWidth(), SpringLayout.WIDTH, getContentPane());

        spring.putConstraint(SpringLayout.WEST, recordingButton, 0, SpringLayout.WEST, getContentPane());
        spring.putConstraint(SpringLayout.SOUTH, recordingButton, 0, SpringLayout.SOUTH, getContentPane());
        
        spring.putConstraint(SpringLayout.EAST, rhythmSelector, 0, SpringLayout.EAST, getContentPane());
        spring.putConstraint(SpringLayout.SOUTH, rhythmSelector, 0, SpringLayout.SOUTH, getContentPane());
        
        spring.putConstraint(SpringLayout.SOUTH, showScore, 0, SpringLayout.SOUTH, getContentPane());
        spring.putConstraint(SpringLayout.HORIZONTAL_CENTER, showScore, 0, SpringLayout.HORIZONTAL_CENTER, getContentPane());
	
	}

    Pianel(int offset, int numKeys) {
        super();
        int height = 600, width = 800;
        this.xOffset = 20;
        this.yOffset = 0;
        this.offset = offset;
        
        this.wholePhrase = new Phrase();
        //this.piano = new Piano(height/2, width, numKeys);
        this.pianoComponent = new PianoComponent(xOffset, yOffset, width, height / 2, this);
        //this.pianoComponent.setBounds(xOffset, yOffset, width, height/2);
        yOffset += 50;
        this.staff = new StaffComponent(xOffset, yOffset + height/2, width, height / 2);
        this.pianoComponent.setScore(this.staff.getScore());
        xOffset += staff.getRealWidth() + 40;
        yOffset += height / 2;
        this.charRecog = new CharacterRecognitionComponent(xOffset, yOffset, staff.getScore());
        
//        if(true) {
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(61, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//	        staff.getScore().addNote(new jm.music.data.Note(70, 4));
//        }
        
        this.pianoComponent.setBounds(20, 0, width, height/2);
        this.staff.setBounds(20, 50, width, height /2);
        this.charRecog.setBounds(60 + staff.getWidth(), 110 + height / 2, 512, 147);
        makeRecordingButton();
        makeScoreButton();
        prepRhythms();
        setupLayout();
        pianoComponent.addMouseMotionListener(charRecog.createMouseMotionListener());
        
        System.out.println("Piano Component: x: " + pianoComponent.getX() + " y: " + pianoComponent.getY() +
        		" width: " + pianoComponent.getWidth() + " height: " + pianoComponent.getHeight());

        System.out.println("Staff Component: x: " + staff.getX() + " y: " + staff.getY() +
        		" width: " + staff.getWidth() + " height: " + staff.getHeight());
        
        System.out.println("charRecog Component: x: " + charRecog.getX() + " y: " + charRecog.getY() +
        		" width: " + charRecog.getWidth() + " height: " + charRecog.getHeight());
        
        
        // Display the frame
        int frameWidth = 1500;
        int frameHeight = 1000;
        this.setSize(frameWidth, frameHeight);
        this.setVisible(true);
        
       
    }

    public static Pianel createPianel(int offset, int numKeys) {
        return new Pianel(offset, numKeys);
    }
    
    public void shutdown() {
    	
    	System.exit(0);
    	
    }

	public PianoComponent getPianoComponent() {
		return pianoComponent;
	}

	public void setPianoComponent(PianoComponent pianoComponent) {
		this.pianoComponent = pianoComponent;
	}

	public StaffComponent getStaff() {
		return staff;
	}

	public void setStaff(StaffComponent staff) {
		this.staff = staff;
	}
    
}
