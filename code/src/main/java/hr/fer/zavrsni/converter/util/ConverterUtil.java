package hr.fer.zavrsni.converter.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import hr.fer.zavrsni.converter.converting.PitchConverter;
import hr.fer.zavrsni.converter.model.note.Note;
import hr.fer.zavrsni.converter.model.note.Pitch;
import hr.fer.zavrsni.converter.model.song.Song;

/**
 * Pomoćni razred za operacije obrade videozapisa i prepoznavanje nota iz njega.
 * 
 * @author Ana Bagić
 *
 */
public class ConverterUtil {
	
	/** Komparator po x koordinati gornje lijeve točke pravokutnika. */
	private static final Comparator<Rect> byX = (n1, n2) -> Integer.compare(n1.x, n2.x);
	/** Komparator po y koordinati gornje lijeve točke pravokutnika. */
	private static final Comparator<Rect> byY = (n1, n2) -> Integer.compare(n1.y, n2.y);
	
	/**
	 * Pomoćna metoda koja danu sliku obrezuje na pravokutnik određen gornjom lijevom točkom i donjom desnom točkom.
	 * 
	 * @param originalFrame slika koja se želi obrezati
	 * @param tl gornja lijeva točka pravokutnika
	 * @param br donja desna točka pravokutnika
	 * @return obrezana slika
	 */
	public static Mat cropFrame(Mat originalFrame, Point tl, Point br) {
		return originalFrame.submat(new Rect(tl, br));
	}
	
	/**
	 * Metoda priprema sliku za obradu.
	 * Originalna slika se zamućuje i pretvara u HSV format slike.
	 * Procesirana slika se pretvara u canny oblik za temelju V vrijednosti HSV slike dobivene pretvaranjem originalne slike.
	 * 
	 * @param originalFrame slika koja se želi pripremiti
	 * @param processedFrame pripremljena slika
	 */
	public static void prepareFrameForProcessing(Mat originalFrame, Mat processedFrame) {
		Imgproc.GaussianBlur(originalFrame, originalFrame, new Size(Constants.GAUSS_BLUR_SIZE, Constants.GAUSS_BLUR_SIZE), Constants.GAUSS_BLUR_DEV);
		Imgproc.cvtColor(originalFrame, originalFrame, Imgproc.COLOR_BGR2HSV);
		
		List<Mat> hsv = new LinkedList<>();
		Core.split(originalFrame, hsv);
		
		//HighGui.imshow("Image", hsv.get(2));
		//HighGui.waitKey();
		
        Imgproc.Canny(hsv.get(2), processedFrame, Constants.CANNY_MIN, Constants.CANNY_MAX, Constants.CANNY_APERTURE, false);
        
		//HighGui.imshow("Image", processedFrame);
		//HighGui.waitKey();
	}
	
	/**
	 * Metoda iz poslane slike traži konture i za svaku pronađenu određuje pravokutnik koji ju opisuje,
	 * te ako on zadovoljava poslani predikat, dodaje ga se u povratni {@link Set}.
	 * 
	 * @param tester predikat koji određuje zadovoljava li pravokutnik ograničenja
	 * @param processedFrame slika iz koje se traže konture
	 * @return {@link Set} pravokutnika koji opisuju konture, a zadovoljavaju pravilo predikata
	 */
	public static Set<Rect> getBoundingRectangles(Predicate<Rect> tester, Mat processedFrame) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processedFrame, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
		Set<Rect> rectangles = new TreeSet<>(byY.reversed().thenComparing(byX));
        for (int i = 0; i < contours.size(); i++) {
        	Rect bounding = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray())).boundingRect();
        	if(tester.test(bounding)) rectangles.add(bounding);
        }
        
        return rectangles;
	}
	
	/**
	 * Pronalazi ton svakog pravokutnika (note), te izbacuje notu ako se cijela nalazi ispod kontrolne linije, a nije trenutno praćena.
	 * 
	 * @param notes note čiji se notovi žele pronaći
	 * @param currNotes note koje se trenutno prate
	 * @param controlY y koordinata kontrolne linije
	 * @return mapa koja preslikava pravokutnike na njihove tonove
	 */
	public static Map<Rect, Pitch> findPitchesAndFilter(Set<Rect> notes, Map<Pitch, Note> currNotes, int controlY) {
		Map<Rect, Pitch> filtered = new TreeMap<>(byX.thenComparing(byY).reversed());
		Set<Pitch> pitches = new HashSet<>();
		
		for(Rect r : notes) {
			Pitch p = PitchConverter.getInstance().findPitch(r);
			if(r.tl().y > controlY && !currNotes.containsKey(p)) continue;
			if(!pitches.contains(p)) {
				filtered.put(r, p);
				pitches.add(p);
			}
		}
		
		return filtered;
	}
	
	/**
	 * Provjeri note kojih boja se pretežito sviraju na lijevoj a koje na desnoj strani, i ako je početna pretpostavka kriva, zamijene se ruke.
	 * 
	 * @param hands mapira dvije boje na listu x koordinata nota
	 * @param song pjesma čije se note treba eventualno korigirati
	 */
	public static void correctHands(Map<Boolean, List<Double>> hands, Song song) {
		double meanFirst = hands.get(true).stream().mapToDouble(d -> d).average().orElse(0.0);
		double meanSecond = hands.get(false).stream().mapToDouble(d -> d).average().orElse(0.0);
		
		if(meanFirst > meanSecond) {
			song.forEach(n -> n.setLeftHand(!n.isLeftHand()));
		}
	}
}
