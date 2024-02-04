package hr.fer.zavrsni.converter.model.song;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zavrsni.converter.model.note.Note;

/**
 * Razred modelira takt pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class Measure {
	
	/** Redni broj takta u pjesmi. */
	private int number;
	/** Note svirane lijevom rukom u ovom taktu. */
	private Set<Note> leftElements = new TreeSet<Note>();
	/** Note svirane desnom rukom u ovom taktu.  */
	private Set<Note> rightElements = new TreeSet<Note>();
	
	/**
	 * Konstruktor stvara novi takt koristeći dani redni broj.
	 * 
	 * @param number redni broj takta u pjesmi
	 */
	public Measure(int number) {
		this.number = number;
	}
	
	/**
	 * Vraća redni broj takta u pjesmi.
	 * 
	 * @return redni broj takta u pjesmi
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Vraća note svirane lijevom rukom.
	 * 
	 * @return note svirane lijevom rukom
	 */
	public Set<Note> getLeftElements() {
		return leftElements;
	}
	
	/**
	 * Vraća note svirane desnom rukom.
	 * 
	 * @return note svirane desnom rukom
	 */
	public Set<Note> getRightElements() {
		return rightElements;
	}
	
	/**
	 * Dodaje kolekciju nota u note svirane lijevom odnosno desnom rukom.
	 * 
	 * @param notes kolekcija nota koja se želi dodati
	 * @param isLeft <code>true</code> ako se dodaju note svirane lijevom rukom, <code>false</code> ako se dodaju note svirane desnom rukom
	 */
	public void addElements(Collection<Note> notes, boolean isLeft) {
		if(isLeft) {
			leftElements.addAll(notes);
		} else {
			rightElements.addAll(notes);
		}
	}
}
