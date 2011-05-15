package vafusion.recog;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JFrame;
import org.neuroph.contrib.imgrec.ColorMode;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.contrib.ocr.OcrHelper;
import org.neuroph.contrib.ocr.OcrUtils;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.util.TransferFunctionType;

@SuppressWarnings("serial")
public class NetworkTrainer extends JFrame {

	/**
	 * Creates a new network, trains it using the data provided in the args, and saves it to a file.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String filename = args[0];
		String[] data = Arrays.copyOfRange(args, 1, args.length);

		List<HashMap<String, BufferedImage>> imageMaps = new ArrayList<HashMap<String, BufferedImage>>();
		HashMap<String, BufferedImage> currentImageMap = new HashMap<String, BufferedImage>();
		imageMaps.add(currentImageMap);
		
		List<List<String>> labelsList = new ArrayList<List<String>>();
		List<String> currentLabels = new ArrayList<String>();
		labelsList.add(currentLabels);
		
		List<TrainingSet> trainingSets = new ArrayList<TrainingSet>();
		for(int i = 0; i < data.length; i++) {
			if(currentLabels.contains(data[i].substring(4, 5))) {
				currentLabels = null;
				currentImageMap = null;
				
				for(int j = 0; j < labelsList.size(); j++)
					if(!((currentLabels = labelsList.get(j)).contains(data[i].substring(4, 5)))) {
						
						currentImageMap = imageMaps.get(j);
						break;
						
					}
				
				if(currentImageMap == null) {
					
					currentLabels = new ArrayList<String>();
					labelsList.add(currentLabels);
					currentImageMap = new HashMap<String, BufferedImage>();
					imageMaps.add(currentImageMap);
					
				}
			}

			currentLabels.add(data[i].substring(4, 5));
			currentImageMap.put(data[i].substring(4, 5), OcrUtils.loadImage(new File(data[i])));
			currentLabels = labelsList.get(0); //reset the two lists so you're always 
											   //filling the initial lists before moving forward
			currentImageMap = imageMaps.get(0);
				
			
			
			System.out.println(labelsList.size());
		}
		
		//System.out.println(OcrUtils.getFractionRgbDataForImages(imageMaps.get(0)));
		for(int i = 0; i < 10; i++)
			System.out.println(OcrUtils.getFractionRgbDataForImages(imageMaps.get(i)).size());
		FractionRgbData frd = OcrUtils.getFractionRgbDataForImages(imageMaps.get(0)).get(labelsList.get(0).get(0));
		
		Dimension size = new Dimension(frd.getWidth(), frd.getHeight());
		System.out.println(size.getWidth() * size.getHeight());
		Vector<Integer> layerCounts = new Vector<Integer>();
		layerCounts.add(100);
		
//		NeuralNetwork nnet = OcrHelper.createNewNeuralNetwork("CharacterRecognizer", size, ColorMode.BLACK_AND_WHITE, 
//				Character.getCharacters(), layerCounts , TransferFunctionType.TANH);
		
		NeuralNetwork nnet = NeuralNetwork.load(filename);
		
		System.out.println(nnet.getInputNeurons().size());
		
		for(int i = 0; i < labelsList.size(); i++)			
			trainingSets.add(OcrHelper.createBlackAndWhiteTrainingSet(labelsList.get(i), 
					OcrUtils.getFractionRgbDataForImages(imageMaps.get(i))));
		
		System.out.println(trainingSets.size());
		
//		nnet.initializeWeights(-100, 100);
		
		for(int i = 0; i < 20; i++) {
			System.out.println("Pass: " + (i + 1));
			Set<Thread> threadList = new HashSet<Thread>();
			System.out.println("Spawning training threads.");
			for(TrainingSet ts : trainingSets) {
				nnet.learnInNewThread(ts);
				threadList.add(nnet.getLearningThread());
			}
			System.out.println("All threads spawned. Count: " + threadList.size());
			
			boolean learning = true;
			Set<Thread> finishedList = new HashSet<Thread>();
			int oldCount = -1;
			while(learning) {
				learning = false;
				Thread k;
				if(!threadList.contains(k = nnet.getLearningThread())) //in case we missed any of them.
						threadList.add(k);
				
				for(Thread t : threadList) {
					
					if(t.isAlive())
						learning = true;
					else if(!finishedList.contains(t))
						finishedList.add(t);
					
				}
				
				Thread.sleep(10000);
				if(finishedList.size() > oldCount)
					System.out.println("Finished count: " + (oldCount = finishedList.size()));
				
			}
		}

//		nnet.save(filename);
		
	}

}
