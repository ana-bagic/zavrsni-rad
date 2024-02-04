package hr.fer.zavrsni.converter.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zavrsni.converter.converting.Converter;
import hr.fer.zavrsni.converter.model.note.Note;
import hr.fer.zavrsni.converter.model.note.NoteType;
import hr.fer.zavrsni.converter.model.song.Measure;
import hr.fer.zavrsni.converter.model.song.Song;
import hr.fer.zavrsni.converter.model.song.TimeSignature;

/**
 * Pomoćni razred za određivanje tempa pjesme i prilagođavanje nota ritmu.
 * 
 * @author Ana Bagić
 *
 */
public class TempoUtil {

	/** Tipovi nota po trajanju. */
	private static List<NoteType> types = new LinkedList<>();
	/** Prave vrijednosti trajanja tipova nota u broju slika. */
	private static Map<NoteType, Double> realDurations = new HashMap<>();
	/** Relativne vrijednosti trajanja tipova nota u broju slika. */
	private static Map<NoteType, Integer> relativeDurations = new HashMap<>();
	
	static {
		types.add(NoteType.WHOLE);
		types.add(NoteType.HALF);
		types.add(NoteType.QUARTER);
		types.add(NoteType.EIGHTH);
		types.add(NoteType.N16TH);
		types.add(NoteType.N32TH);
		
		relativeDurations.put(NoteType.WHOLE, 256);
		relativeDurations.put(NoteType.HALF, 128);
		relativeDurations.put(NoteType.QUARTER, 64);
		relativeDurations.put(NoteType.EIGHTH, 32);
		relativeDurations.put(NoteType.N16TH, 16);
		relativeDurations.put(NoteType.N32TH, 8);
	}
	
	/**
	 * Metoda računa najvjerojatnije trajanje jedinice mjere takta u slikama na temelju broja doba unutar minute.
	 * Postavlja novi tempo pjesme na temelju trajanja jedinice mjere takta i trajanje takta.
	 * 
	 * @param song pjesma za koju se traži tempo
	 */
	public static void findTempo(Song song) {
		TimeSignature ts = song.getTimeSignature();
		
		Map<Integer, Integer> durations = Util.getFrequencies(song, n -> n.getLength());
		List<Integer> sortedDurations = Util.getListSortedByValues(durations);
		List<Integer> firstFive = sortedDurations.size() < 5 ? sortedDurations : sortedDurations.subList(0, 5);
		
		int fps = Converter.getInstance().getFramesPerSecond();
		double guessedFPB = (60.0 * fps)/song.getBeatsPerMinute();
		
		double framesPerBeat = Integer.MAX_VALUE;
		for(Integer duration : firstFive) {
			if(Math.abs(duration - guessedFPB) < Math.abs(framesPerBeat - guessedFPB)) {
				framesPerBeat = duration;
			}
		}
		
		for(Integer duration : firstFive) {
			if(Math.abs(duration - framesPerBeat) < 2) {
				framesPerBeat = (1.0*framesPerBeat + duration)/2;
			}
		}
		
		setRealDurations(framesPerBeat, ts.getBeatUnit());
		song.setMeasureDuration(ts.getBeats() * relativeDurations.get(ts.getBeatUnit()));
		song.setBeatsPerMinute((int) ((60 * fps)/framesPerBeat));
	}
	
	/**
	 * Metoda postavlja stvarno trajanje svakog tipa note na temelju jedinice mjere i njenog trajanja.
	 * 
	 * @param framesPerBeat trajanje jedinice mjere
	 * @param beatUnit jedinica mjere
	 */
	private static void setRealDurations(double framesPerBeat, NoteType beatUnit) {
		realDurations.put(beatUnit, framesPerBeat);
		
		for(int i = types.indexOf(beatUnit) - 1; i >= 0; i--) {
			realDurations.put(types.get(i), realDurations.get(types.get(i+1))*2);
		}
		
		for(int i = types.indexOf(beatUnit) + 1; i < types.size(); i++) {
			realDurations.put(types.get(i), realDurations.get(types.get(i-1))/2);
		}
	}
	
	/**
	 * Metoda gradi vremensku crtu pojavljivanja nota i pauza na temelju izračunatog tempa.
	 * 
	 * @param song pjesma za koju se gradi vremenska crta
	 * @param startFrame slika u kojoj je prvi put zabilježena nota
	 */
	public static void createTimeline(Song song, int startFrame) {
		List<Note> prevMeasureNotes = new LinkedList<>();
		TimeSignature ts = song.getTimeSignature();
		
		double measureDuration = realDurations.get(ts.getBeatUnit()) * ts.getBeats();
		int firstFrameOfMeasure = startFrame;
		
		int measureCounter = 1;
		boolean lastMeasure;
		
		do {
			Measure measure = new Measure(measureCounter);
			lastMeasure = true;
			
			int lastFrameOfMeasure = (int) (firstFrameOfMeasure + measureDuration);
			List<Note> notesInMeasure = new LinkedList<>(prevMeasureNotes);
			prevMeasureNotes.clear();
			
			for(Note n : song) {
				if(!(n.getStartTime() < lastFrameOfMeasure - 2)) {
					lastMeasure = false;
					break;
				}
				notesInMeasure.add(n);
			}
			song.removeNotes(notesInMeasure);
			
			int highestFrameInMeasure = -1;
			for(Note n : notesInMeasure) {
				if(n.getEndTime() <= lastFrameOfMeasure + 3 && n.getEndTime() > highestFrameInMeasure) {
					highestFrameInMeasure = n.getEndTime();
				}
			}
			
			if(Math.abs(highestFrameInMeasure - lastFrameOfMeasure) <= 3) {
				lastFrameOfMeasure = highestFrameInMeasure;
			}
			
			for(Note n : notesInMeasure) {				
				if(n.getEndTime() > lastFrameOfMeasure) {
					if(!lastMeasure) {
						prevMeasureNotes.add(splitNotes(n, lastFrameOfMeasure));
					} else {
						n.setEndTime(lastFrameOfMeasure);
					}
				}
			}
			
			for(Note n : notesInMeasure) {
				List<Note> foundNotes = new LinkedList<>();
				determineType(n, ts.getBeatUnit(), null, foundNotes);
				measure.addElements(foundNotes, foundNotes.get(0).isLeftHand());
			}
			
			addChordsAndRests(measure.getRightElements(), false, firstFrameOfMeasure, lastFrameOfMeasure);
			if(song.isTwoHands()) {
				addChordsAndRests(measure.getLeftElements(), true, firstFrameOfMeasure, lastFrameOfMeasure);
			}
			
			song.addMeasure(measure);
			measureCounter++;
			firstFrameOfMeasure = lastFrameOfMeasure + 1;
		} while(!lastMeasure);
	}
	
	/**
	 * Prepolavlja poslanu notu na dvije po danom broju slike. Danu notu (prvu) uređuje i vraća novu notu.
	 * 
	 * @param n nota koja se želi prepoloviti
	 * @param splitFrame broj slike po kojemu se želi prepoloviti nota
	 * @return nova nota (druga po redu) koja je nastala propolavljanjem dane
	 */
	private static Note splitNotes(Note n, int splitFrame) {		
		Note newNote = new Note(n.isRest(), n.getPitch(), n.getAccidental(), n.isChord(), n.isLeftHand(), splitFrame + 1, n.getEndTime());
		newNote.setTiedStop();
		
		if(n.isTiedStart()) {
			newNote.setTiedStart(true);
		}
		
		n.setTiedStart(true);
		n.setEndTime(splitFrame);
		
		return newNote;
	}
	
	/**
	 * Metoda određuje tip note po trajanju rekurzivnim algoritmom i sprema dobivenu notu (ili note ako se sastoji od više tipova) u danu listu.
	 * 
	 * @param note nota za koju se želi odrediti tip
	 * @param type trenutno pretpostavljeni tip note
	 * @param prevSmaller je li se u prošlom pozivu tražila manja nota ili veća (<code>null</code> ako je prvi poziv)
	 * @param foundNotes lista nota u koju se trebaju pohraniti dobivene
	 */
	private static void determineType(Note note, NoteType type, Boolean prevSmaller, List<Note> foundNotes) {
		if(realDurations.get(type) - note.getLength() > Constants.NOTE_LEN_ERROR && type != NoteType.N32TH) {			
			if(prevSmaller == null || prevSmaller) {
				determineType(note, getSmaller(type), true, foundNotes);
				return;
			}
			
			Note newNote = splitNotes(note, (int) (note.getStartTime() + realDurations.get(getSmaller(type))));
			addNote(foundNotes, note, getSmaller(type));
			determineType(newNote, getSmaller(type), null, foundNotes);
			
		} else if(note.getLength() - realDurations.get(type) > Constants.NOTE_LEN_ERROR && type != NoteType.WHOLE) {
			if(prevSmaller == null || !prevSmaller) {
				determineType(note, getBigger(type), false, foundNotes);
				return;
			}
			
			Note newNote = splitNotes(note, (int) (note.getStartTime() + realDurations.get(type)));
			addNote(foundNotes, note, type);
			determineType(newNote, getSmaller(type), null, foundNotes);
			
		} else {
			addNote(foundNotes, note, type);
		}
	}
	
	/**
	 * Vraća tip note s duplo dužim trajanjem od danog.
	 * 
	 * @param n tip note za koji se traži duža nota
	 * @return tip note s duplo dužim trajanjem od danog
	 */
	private static NoteType getBigger(NoteType n) {
		int index = types.indexOf(n);
		return types.get(Integer.max(index - 1, 0));
	}
	
	/**
	 * Vraća tip note s duplo kraćim trajanjem od danog.
	 * 
	 * @param n tip note za koji se traži kraća nota
	 * @return tip note s duplo kraćim trajanjem od danog
	 */
	private static NoteType getSmaller(NoteType n) {
		int index = types.indexOf(n);
		return types.get(Integer.min(index + 1, types.size() - 1));
	}
	
	/**
	 * Danoj noti postavlja tip na dani i trajanje s obzirom na tip i dodaje ju u danu listu.
	 * 
	 * @param foundNotes list nota u koju se želi dodati poslana
	 * @param note nota koja se želi pohraniti
	 * @param type tip note kojeg treba postaviti
	 */
	private static void addNote(List<Note> foundNotes, Note note, NoteType type) {
		note.setNoteType(type);
		note.setDuration(relativeDurations.get(type));
		foundNotes.add(note);
	}
	
	/**
	 * Dodaje pauze i stvara akorde gdje je potrebno.
	 * 
	 * @param notes note takta u koje se dodaje pauza/akordi
	 * @param leftHand pripadaju li note lijevoj ruci takta ili ne
	 * @param firstFrameOfMeasure broj prve slike takta
	 * @param lastFrameOfMeasure broj zadnje slike takta
	 */
	private static void addChordsAndRests(Set<Note> notes, boolean leftHand, int firstFrameOfMeasure, int lastFrameOfMeasure) {
		int biggestEndFrame = firstFrameOfMeasure;
		Set<Note> notesToAdd = new HashSet<>();
		Set<Note> chords = new TreeSet<>();
		
		for(Note n : notes) {
			if(biggestEndFrame - n.getStartTime() > Constants.CHORD_ERROR) {
				Set<Note> oldChords = new TreeSet<>();
				Set<Note> newChords = new TreeSet<>();
				for(Note ch : chords) {
					if(ch.getEndTime() - n.getStartTime() >  Constants.CHORD_ERROR) {
						if(Math.abs(ch.getStartTime() - n.getStartTime()) > Constants.CHORD_ERROR) {
							Note newNote = splitNotes(ch, n.getStartTime() - 1);
							
							List<Note> foundNotes = new LinkedList<>();
							determineType(ch, ch.getNoteType(), null, foundNotes);
							oldChords.addAll(foundNotes);
							
							foundNotes.clear();
							determineType(newNote, ch.getNoteType(), null, foundNotes);
							newChords.addAll(foundNotes);
						} else {
							newChords.add(ch);
						}
					} else {
						oldChords.add(ch);
					}
				}
				
				setChords(oldChords, notesToAdd);

				chords.clear();
				chords.addAll(newChords);
				chords.add(n);
			} else {
				setChords(chords, notesToAdd);
				
				chords.clear();
				chords.add(n);
			}
			
			if(n.getStartTime() - biggestEndFrame > Constants.REST_ERROR) {
				List<Note> foundRests = new LinkedList<>();
				Note rest = new Note(true, null, -2, false, leftHand, biggestEndFrame, n.getStartTime());
				determineType(rest, NoteType.QUARTER, null, foundRests);
				notesToAdd.addAll(foundRests);
			}
			
			biggestEndFrame = Math.max(biggestEndFrame, n.getEndTime());
		}
		
		setChords(chords, notesToAdd);
		
		if(Math.abs(biggestEndFrame - lastFrameOfMeasure) > Constants.REST_ERROR) {
			Set<NoteType> types = getTypes(Math.abs(biggestEndFrame - lastFrameOfMeasure));
			types.forEach(t -> notesToAdd.add(new Note(leftHand, relativeDurations.get(t), t)));
		}
		
		notes.addAll(notesToAdd);
	}
	
	/**
	 * Stvara akorde iz dani nota koje tvore jedan akord.
	 * 
	 * @param chords note koje tvore akord
	 * @param notesToAdd kolekcija u koju treba dodati note koje čine akord
	 */
	private static void setChords(Set<Note> chords, Set<Note> notesToAdd) {
		if(chords.size() > 1) {
			boolean first = true;
			int firstFrame = -1;
			for(Note c : chords) {
				if(!first && Math.abs(firstFrame - c.getStartTime()) <= Constants.CHORD_ERROR) {
					c.setChord();
				} else {
					firstFrame = c.getStartTime();
					first = false;
				}
			}
		}
		
		notesToAdd.addAll(chords);
	}
	
	/**
	 * Metoda pronalazi sve tipove nota po trajanju od kojih se sastoji nota sa poslanim relativnim trajanjem.
	 * 
	 * @param relativeDuration relativno trajanje note.
	 * @return svi tipovi nota od kojih se sastoji nota sa poslanim relativnim trajanjem
	 */
	private static Set<NoteType> getTypes(int relativeDuration) {
		Set<NoteType> types = new HashSet<>();
		
		for(NoteType type : types) {
			if(relativeDuration == relativeDurations.get(type)) {
				types.add(type);
				relativeDuration %= relativeDurations.get(type);
			}
		}
		
		return types;
	}
}
