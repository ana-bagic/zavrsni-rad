package hr.fer.zavrsni.converter.model.song;

import hr.fer.zavrsni.converter.model.note.NoteType;

/**
 * Razred modelira mjeru pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class TimeSignature {

	/** Broj doba unutar jednog takta. */
	private int beats;
	/** Jedinica mjere takta. */
	private NoteType beatUnit;
	
	/**
	 * Konstruktor stvara novu mjeru koristeći broj doba i jedinicu mjere takta.
	 * 
	 * @param beats broj doba unutar jednog takta
	 * @param beatUnit jedinica mjere takta
	 */
	public TimeSignature(int beats, NoteType beatUnit) {
		this.beats = beats;
		this.beatUnit = beatUnit;
	}

	/**
	 * Vraća broj doba unutar jednog takta.
	 * 
	 * @return broj doba unutar jednog takta
	 */
	public int getBeats() {
		return beats;
	}

	/**
	 * Vraća jedinicu mjere takta.
	 * 
	 * @return jedinicu mjere takta
	 */
	public NoteType getBeatUnit() {
		return beatUnit;
	}
	
	@Override
	public String toString() {
		return beats + "/" + switch (beatUnit) {
		case WHOLE -> "1";
		case HALF -> "2";
		case QUARTER -> "4";
		case EIGHTH -> "8";
		case N16TH -> "16";
		case N32TH -> "32";
		};
	}
	
}
