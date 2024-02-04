package hr.fer.zavrsni.converter.model.song;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zavrsni.converter.model.note.Note;

/**
 * Razred modelira pjesmu. Iterira po notama ove pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class Song implements Iterable<Note> {

	/** Metapodatci pjesme. */
	private SongMetadata metadata;
	/** Tonalitet pjesme. */
	private Scale scale;
	/** Mjera pjesme. */
	private TimeSignature timeSignature;
	/** Pretpostavljen tempo pjesme (beats per minute). */
	private int beatsPerMinute;
	/** Relativno trajanje jednog takta. */
	private int measureDuration;
	/** Svira li se pjesma s dvije ruke. */
	private boolean twoHands;
	/** Sve note pjesme pročitane iz videozapisa. */
	private Set<Note> notes = new TreeSet<>();
	/** Svi taktovi pjesme. */
	private List<Measure> measures = new LinkedList<>();

	/**
	 * Vraća metapodatke pjesme.
	 * 
	 * @return metapodatke pjesme
	 */
	public SongMetadata getMetadata() {
		return metadata;
	}
	
	/**
	 * Postavlja metapodatke pjesme na dane.
	 * 
	 * @param metadata metapodatci pjesme
	 */
	public void setMetadata(SongMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * Vraća tonalitet pjesme.
	 * 
	 * @return tonalitet pjesme
	 */
	public Scale getScale() {
		return scale;
	}
	
	/**
	 * Postavlja tonalitet pjesme.
	 * 
	 * @param scale tonalitet pjesme
	 */
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
	/**
	 * Vraća mjeru pjesme.
	 * 
	 * @return mjeru pjesme
	 */
	public TimeSignature getTimeSignature() {
		return timeSignature;
	}

	/**
	 * Postavlja mjeru pjesme.
	 * 
	 * @param timeSignature mjera pjesme
	 */
	public void setTimeSignature(TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
	}
	
	/**
	 * Vraća pretpostavljeni tempo pjesme (beats per minute).
	 * 
	 * @return pretpostavljeni tempo pjesme
	 */
	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}

	/**
	 * Postavlja pretpostavljeni tempo pjesme (beats per minute).
	 * 
	 * @param beatsPerMinute pretpostavljeni tempo pjesme
	 */
	public void setBeatsPerMinute(int beatsPerMinute) {
		this.beatsPerMinute = beatsPerMinute;
	}
	
	/**
	 * Vraća relativno trajanje jednog takta pjesme.
	 * 
	 * @return relativno trajanje jednog takta pjesme
	 */
	public int getMeasureDuration() {
		return measureDuration;
	}

	/**
	 * Postavlja relativno trajanje jednog takta pjesme.
	 * 
	 * @param measureDuration relativno trajanje jednog takta pjesme
	 */
	public void setMeasureDuration(int measureDuration) {
		this.measureDuration = measureDuration;
	}
	
	/**
	 * Vraća svira li se pjesma s dvije ruke ili jednom.
	 * 
	 * @return <code>true</code> ako se pjesma svira s dvije ruke, inače <code>false</code>
	 */
	public boolean isTwoHands() {
		return twoHands;
	}
	
	/**
	 * Postavlja svira li se pjesma s dvije ruke ili jednom.
	 * 
	 * @param twoHands <code>true</code> ako se pjesma svira s dvije ruke, inače <code>false</code>
	 */
	public void setTwoHands(boolean twoHands) {
		this.twoHands = twoHands;
	}
	
	/**
	 * Dodaje notu u kolekciju nota pročitanih iz videozapisa.
	 * 
	 * @param note nota pročitana iz videozapisa
	 */
	public void addNote(Note note) {
		notes.add(note);
	}
	
	/**
	 * Izbacuje sve note dane kolekcije iz kolekcije nota ove pjesme.
	 * 
	 * @param notes note koje se žele izbaciti iz pjesme
	 */
	public void removeNotes(Collection<Note> notes) {
		this.notes.removeAll(notes);
	}
	
	/**
	 * Vraća listu s taktovima ove pjesme.
	 * 
	 * @return listu s taktovima ove pjesme
	 */
	public List<Measure> getMeasures() {
		return measures;
	}
	
	/**
	 * Dodaje dani takt u listu taktova ove pjesme.
	 * 
	 * @param measure takt koji se želi dodati u pjesmu
	 */
	public void addMeasure(Measure measure) {
		measures.add(measure);
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}

}
