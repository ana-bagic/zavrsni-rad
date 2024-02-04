package hr.fer.zavrsni.converter.converting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import hr.fer.zavrsni.converter.model.note.Note;
import hr.fer.zavrsni.converter.model.note.Pitch;
import hr.fer.zavrsni.converter.model.song.Song;
import hr.fer.zavrsni.converter.model.song.SongMetadata;
import hr.fer.zavrsni.converter.util.Constants;
import hr.fer.zavrsni.converter.util.ConverterUtil;

/**
 * Glavni razred za obradu videozapisa te generiranje notnog zapisa iz njega.
 * 
 * @author Ana Bagić
 *
 */
public class Converter {
	
	/** Jedinstveni objekt ovog razreda. */
	private static Converter converter = new Converter();

	/** Putanja do videozapisa. */
	private String pathToVideo;
	/** Pjesma koja se gradi iz videozapisa.  */
	private Song song = new Song();
	
	/** Originalna slika videozapisa. */
	private Mat originalFrame = new Mat();
	/** Procesirana slika videozapisa. */
	private Mat processedFrame = new Mat();
	
	/** Kolekcija svih trenutno promatranih nota i njihovi tonovi. */
	private Map<Pitch, Note> currentNotes = new HashMap<>();
	/** Broj slika koliko promatrana nota još može postojati, a da nije prepoznata na slici. */
	private Map<Pitch, Integer> currentNotesExistence = new HashMap<>();
	
	/** Brojač trenutne slike koja se obrađuje.  */
	private int frameCounter = 1;
	/** Prva slika u kojoj se pojavila nota. */
	private int firstFrame = -1;
	/** Broj slika po sekundi koje video ima. */
	private int fps;
	
	/** Boja nota sviranih desnom rukom. */
	private double firstHue = -1;
	/** Boja nota sviranih lijevom rukom. */
	private double secondHue = -1;
	/** Mapa kojom se određuje s kojom rukom se svira koja boja note. */
	private Map<Boolean, List<Double>> hands = new HashMap<>();
	
	/**
	 * Stvara novi konverter i puni mapu kojom se određuje s kojom rukom se svira koja boja note početnim vrijednostima.
	 */
	private Converter() {
		hands.put(true, new LinkedList<>());
		hands.put(false, new LinkedList<>());
	}
	
	/**
	 * Vraća instancu {@link Converter}-a (jedinstveni objekt).
	 * 
	 * @return instancu {@link Converter}-a
	 */
	public static Converter getInstance() {
		return converter;
	}
	
	/**
	 * Vraća putanju na disku do videozapisa iz kojega se generira notni zapis.
	 * 
	 * @return putanja na disku do videozapisa
	 */
	public String getPathToVideo() {
		return pathToVideo;
	}
	
	/**
	 * Postavlja putanju na disku do videozapisa iz kojega se generira notni zapis.
	 * 
	 * @param pathToVideo putanja na disku do videozapisa
	 */
	public void setPathToVideo(String pathToVideo) {
		this.pathToVideo = pathToVideo;
	}
	
	/**
	 * Postavlja metapodatke pjesme.
	 * 
	 * @param metadata metapodatci pjesme
	 */
	public void setSongMetadata(SongMetadata metadata) {
		song.setMetadata(metadata);
	}
	
	/**
	 * Vraća instancu pjesme korištenu u {@link Converter}-u.
	 * 
	 * @return instancu pjesme korištenu u {@link Converter}-u
	 */
	public Song getSong() {
		return song;
	}
	
	/**
	 * Vraća redni broj slike u kojoj se prvi put pojavila nota.
	 * 
	 * @return redni broj slike u kojoj se prvi put pojavila nota
	 */
	public int getFirstFrame() {
		return firstFrame;
	}
	
	/**
	 * Vraća broj slika po sekundi videozapisa.
	 * 
	 * @return broj slika po sekundi videozapisa
	 */
	public int getFramesPerSecond() {
		return fps;
	}
	
	/**
	 * Metoda obrađuje svaku sliku videozapisa, te iz njih skuplja informacije o notama i pohranjuje ih u instancu razreda {@link Song}.
	 * Svaka slika se obrezuje na pravokutnik koji određuju poslane točke.
	 * 
	 * @param tl gornja lijeva točka pravokutnika
	 * @param br donja desna točka pravokutnika
	 * @return instanca razreda {@link Song} koja sadrži informacije o notama dobivenim obrađivanjem videozapisa
	 */
	public Song extractNotes(Point tl, Point br) {
		VideoCapture cap = new VideoCapture(pathToVideo);
		fps = (int) cap.get(Videoio.CAP_PROP_FPS);
		
		for(frameCounter = 1; frameCounter < (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT) - 1; frameCounter++) {
        	cap.read(originalFrame);
        	processFrame(tl, br);
        	//System.out.println(frameCounter);
        }
		
		if(secondHue != -1) {
			song.setTwoHands(true);
			ConverterUtil.correctHands(hands, song);
		} else {
			song.setTwoHands(false);
		}
		
		return song;
	}

	/**
	 * Metoda obrezuje sliku na temelju poslanih točaka, procesira ju, te pronalazi note u dobivenim konturama. 
	 * 
	 * @param tl gornja lijeva točka pravokutnika
	 * @param br donja desna točka pravokutnika
	 */
	private void processFrame(Point tl, Point br) {
		int controlY = (int) (br.y - Constants.CONTROL_DISTANCE);
		
		originalFrame = ConverterUtil.cropFrame(originalFrame, tl, br);
		ConverterUtil.prepareFrameForProcessing(originalFrame, processedFrame);
		
		Set<Rect> notes = ConverterUtil.getBoundingRectangles(
				r -> r.br().y > controlY && r.width > Constants.MIN_WIDTH && r.height > Constants.MIN_HEIGHT,
				processedFrame);
		/*
		Mat drawing = Mat.zeros(originalFrame.size(), CvType.CV_8UC3);
		for(Rect r : notes) {
			Imgproc.rectangle(drawing, r.tl(), r.br(), new Scalar(255, 255, 255));
		}
		
		HighGui.imshow("Image", drawing);
		HighGui.waitKey();
		*/
		
		Map<Rect, Pitch> filteredNotes = ConverterUtil.findPitchesAndFilter(notes, currentNotes, controlY);
		
		for(var entry : filteredNotes.entrySet()) {
			Rect r = entry.getKey();
			Pitch pitch = entry.getValue();
			
			if(r.tl().y > controlY) {
 				Note n = currentNotes.remove(pitch);
 				currentNotesExistence.remove(pitch);
				n.setEndTime(frameCounter);
   				song.addNote(n);
			} else if(!currentNotes.containsKey(pitch)) {
				firstFrame = firstFrame == -1 ? frameCounter : firstFrame;
				
				boolean hand = getHand(r);
				hands.get(hand).add(r.tl().x);
				
				currentNotesExistence.put(pitch, Constants.EXISTENCE);
				currentNotes.put(pitch, new Note(pitch, hand, frameCounter));
			} else {
				currentNotesExistence.put(pitch, Constants.EXISTENCE);
			}
		}
		
		Set<Pitch> toRemove = new HashSet<>();
		for(var ex : currentNotesExistence.entrySet()) {
			if(ex.getValue() == 0) {
				currentNotes.remove(ex.getKey());
				toRemove.add(ex.getKey());
			} else {
				ex.setValue(ex.getValue() - 1);
			}
		}
		
		toRemove.forEach(p -> currentNotesExistence.remove(p));
	}
	
	/**
	 * Metoda vraća kojom rukom se svira poslana nota (kvadrat) na temelju njegove boje.
	 * 
	 * @param r kvadrat koji predstavlja notu za koju se želi odrediti kojom rukom se svira
	 * @return <code>true</code> ako se svira lijevom rukom, inače <code>false</code>
	 */
	private boolean getHand(Rect r) {
		double hue = originalFrame.get((int)r.tl().y + r.height/2, (int)r.tl().x + r.width/2)[0];
		if(firstHue == -1) {
			firstHue = hue;
			return false;
		}
		
		if(secondHue == -1) {
			if(hue > firstHue - Constants.HUE_ERROR && hue < firstHue + Constants.HUE_ERROR) {
				return false;
			}
			
			secondHue = hue;
			return true;
		}
		
		return Math.abs(firstHue - hue) > Math.abs(secondHue - hue);
	}
	
}
