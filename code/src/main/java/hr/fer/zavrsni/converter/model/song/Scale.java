package hr.fer.zavrsni.converter.model.song;

import hr.fer.zavrsni.converter.model.note.NoteAlphabet;

/**
 * Razred modelira tonalitet pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class Scale {

	/** Redni broj tonaliteta u kvintnom krugu, odnosno negativni redni broj tonaliteta u kvartnom krugu. */
	private int fifths;
	/** Je li tonalitet u duru ili molu. */
	private boolean major;
	/** Početni ton ljestvice ovog tonaliteta u duru. */
	private NoteAlphabet alphabet;
	
	/**
	 * Konstruktor stvara novi tonalitet koristeći redni broj tonaliteta, je li tonalitet u duru, i početnu notu tonaliteta.
	 * 
	 * @param fifths redni broj tonaliteta u kvintnom krugu ili negativni redni broj tonaliteta u kvartnom krugu
	 * @param major je li tonalitet u duru ili molu
	 * @param alphabet početni ton ljestvice ovog tonaliteta u duru
	 */
	public Scale(int fifths, boolean major, NoteAlphabet alphabet) {
		this.fifths = fifths;
		this.major = major;
		this.alphabet = alphabet;
	}
	
	/**
	 * Ako je tonalitet u kvintnom krugu, vratiti će redni broj tonaliteta u kvintnom krugu.
	 * Ako je tonalitet u kvartnom krugu, vratiti će negativni redni broj tonaliteta u kvartnom krugu.
	 * 
	 * @return redni broj tonaliteta u kvintnom krugu, odnosno negativni redni broj tonaliteta u kvartnom krugu
	 */
	public int getFifths() {
		return fifths;
	}
	
	/**
	 * Vraća je li tonalitet u duru ili molu.
	 * 
	 * @return <code>true</code> ako je tonalitet u duru, <code>false</code> ako je tonalitet u molu
	 */
	public boolean isMajor() {
		return major;
	}
	
	/**
	 * Vraća početni ton ljestvice ovog tonaliteta u duru.
	 * 
	 * @return početni ton ljestvice ovog tonaliteta u duru
	 */
	public NoteAlphabet getStartAlphabet() {
		return alphabet;
	}
}
