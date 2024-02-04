package hr.fer.zavrsni.converter.model.note;

/**
 * Razred predstavlja ton note.
 * 
 * @author Ana Bagić
 *
 */
public class Pitch implements Comparable<Pitch> {

	/** Ton note unutar oktave. */
	private NoteAlphabet alphabet;
	/** Oktava tona. */
	private int octave;
	/** Izmjena note: -1 za sniženu notu, 1 za povišenu, 0 inače. */
	private int alter;
	
	/**
	 * Stvara novi ton note na temelju notne abecede i oktave.
	 * 
	 * @param alphabet abeceda tona unutar oktave
	 * @param octave oktava tona
	 */
	public Pitch(NoteAlphabet alphabet, int octave) {
		this.alphabet = alphabet;
		this.octave = octave;
		this.alter = 0;
	}
	
	/**
	 * Vraća abecedu tona unutar oktave.
	 * 
	 * @return abecedu tona unutar oktave
	 */
	public NoteAlphabet getAlphabet() {
		return alphabet;
	}
	
	/**
	 * Postavlja abecedu tona na danu.
	 * 
	 * @param alphabet abeceda tona unutar oktave
	 */
	public void setAlphabet(NoteAlphabet alphabet) {
		this.alphabet = alphabet;
	}
	
	/**
	 * Vraća oktavu u kojoj se nalazi ton.
	 * 
	 * @return oktavu u kojoj se nalazi ton
	 */
	public int getOctave() {
		return octave;
	}
	
	/**
	 * Smanjuje oktavu za 1.
	 */
	public void decreaseOctave() {
		octave--;
	}
	
	/**
	 * Povećava oktavu za 1.
	 */
	public void increaseOctave() {
		octave++;
	}
	
	/**
	 * Vraća izmjenu tona. Izmjena: -1 za sniženu notu, 1 za povišenu, 0 inače.
	 * 
	 * @return izmjenu tona
	 */
	public int getAlter() {
		return alter;
	}
	
	/**
	 * Postavlja izmjenu tona na danu.
	 * 
	 * @param alter izmjena tona
	 */
	public void setAlter(int alter) {
		this.alter = alter;
	}
	
	@Override
	public String toString() {
		return "alphabet: " + alphabet.toString() + " octave: " + octave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alphabet == null) ? 0 : alphabet.hashCode());
		result = prime * result + octave;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pitch other = (Pitch) obj;
		if (alphabet != other.alphabet)
			return false;
		if (octave != other.octave)
			return false;
		return true;
	}

	@Override
	public int compareTo(Pitch o) {
		int byOctave = Integer.compare(octave, o.octave);
		if(byOctave == 0) {
			return alphabet.compareTo(o.alphabet);
		}
		
		return byOctave;
	}
	
}
