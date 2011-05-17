package vafusion.gui;

// 331-346

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    public PhraseComponent phrase;
    boolean blackPattern[] = {true, true, false, true, true, true, false, false};
    double twoThirds = (double)2/3;
    double oneThird = (double)1/3;
    boolean running = true;
    JMenuBar menuBar;
    boolean piano = true;
    
    
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
        showScore.setText("Switch Views");
        showScore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(piano){
					//switch to phrase view and character recognition
					getContentPane().add(charRecog);
					getContentPane().add(phrase);
					getContentPane().remove(pianoComponent);
					repaintMe();
					piano = false;
				}else{
					//switch to piano
					getContentPane().remove(charRecog);
					getContentPane().remove(phrase);
					getContentPane().add(pianoComponent);
					repaintMe();
					piano = true;
				}
			}
			
		});
		
	}
	
	void repaintMe(){
		this.repaint();
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
        
        getContentPane().add(menuBar, null);
        
        getContentPane().add(recordingButton);
        getContentPane().add(rhythmSelector);
        getContentPane().add(showScore);
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
        int height = 575, width = 967;
        this.xOffset = 0;
        this.yOffset = 0;
        this.offset = offset;
        
        menuBar = this.getJMenuBar();
        menuBar = menuBar == null ? new JMenuBar() : menuBar;
        
        setupMenuBar();
        this.setJMenuBar(menuBar);
//        menuBar.setVisible(true);
        
        
        int anotherOffset = 30; // arbitrary num to make room for buttons
        
        this.wholePhrase = new Phrase();
        //this.piano = new Piano(height/2, width, numKeys);
        this.pianoComponent = new PianoComponent(xOffset, yOffset, width, height / 2 - anotherOffset, this);
        //this.pianoComponent.setBounds(xOffset, yOffset, width, height/2);
        this.phrase = new PhraseComponent(0, 0, width / 2, height / 2 - anotherOffset / 2);
        yOffset += 50;
        this.staff = new StaffComponent(xOffset, (int) (yOffset + height/2 - anotherOffset * 3), width, height / 2 - anotherOffset/2);
        this.pianoComponent.setScore(this.staff.getScore());
        xOffset += staff.getRealWidth() + 40;
        yOffset += height / 2;
        this.charRecog = new CharacterRecognitionComponent(0, 0, staff.getScore());
//        this.charRecog = new CharacterRecognitionComponent((int) (width * 0.625), (int) (height * .125), staff.getScore());
      this.charRecog.setBounds((int) (width * 0.7), (int) (height * .125), 512, 147);
        
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
        
        this.charRecog.setBounds(charRecog.getRealX(), charRecog.getRealY(), 512, 147);
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
        int frameWidth = 967;
        int frameHeight = 575;
        this.setSize(frameWidth, frameHeight);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
       
    }

    private void setupMenuBar() {

        
        JMenu file = new JMenu("File");
        JMenu music = new JMenu("Music");
        
        JMenuItem clear = new JMenuItem("Clear");
        JMenuItem save = new JMenuItem("Save to Midi");
        JMenuItem load = new JMenuItem("Load from Midi");
        JMenu select = new JMenu("Select Instrument");
        JMenuItem range = new JMenuItem("Set Piano Range");
        JMenuItem acc = new JMenuItem("Set Accidental Default");
        clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
        });
        
        save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Write.midi(pianoComponent.getPiano().getRecordedScore(), "Untitled Score.midi");
			}
        });
        
        load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
//				Write.midi(pianoComponent.getPiano().getRecordedScore(), "Untitled Score.midi");
			}
        });
        
        
        file.add(clear);
        file.add(save);
        file.add(load);
        music.add(select);
        music.add(range);
        music.add(acc);
        

//        menuBar.setBounds(0, 0, 967, 30);

        this.menuBar.add(file);
        this.menuBar.add(music);
        
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
