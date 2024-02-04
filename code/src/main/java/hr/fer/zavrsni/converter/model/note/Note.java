package hr.fer.zavrsni.converter.model.note;

/**
 * Razred modelira jednu notu pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class Note implements Comparable<Note> {

	/** Je li nota pauza. */
	private boolean rest;
	
	/** Svira li se nota lijevom rukom. */
	private boolean leftHand;
	/** Broj slike u kojoj se nota prvi put pojavila. */
	private int startTime;
	/** Broj slike u kojoj se nota zadnji put pojavila. */
	private int endTime;
	
	/** Ton note. */
	private Pitch pitch;
	/** Ako nota ne spada u ljestvicu, znak koji se treba nalaziti ispred nje: 1 povisilica, 0 razješilica, -1 snizilica, inače -2. */
	private int accidental;
	
	/** Relativno trajanje note. */
	private int duration;
	/** Vrsta note po trajanju. */
	private NoteType type;
	/** Počinje li na noti luk. */
	private boolean tiedStart = false;
	/** Završava li na noti luk. */
	private boolean tiedStop = false;
	
	/** Je li nota dio akorda. */
	private boolean chord = false;
	
	/**
	 * Konstruktor stvara novu notu koristeći dane parametre.
	 * 
	 * @param pitch ton note
	 * @param leftHand svira li se nota lijevom rukom
	 * @param startTime broj slike u kojoj se nota prvi put pojavljuje
	 */
	public Note(Pitch pitch, boolean leftHand, int startTime) {
		this(false, pitch, -2, false, leftHand, startTime, -1);
	}
	
	/**
	 * Konstruktor stvara novu notu/pauzu koristeći dane parametre.
	 * 
	 * @param rest je li nota pauza
	 * @param pitch ton note
	 * @param accidental znak koji se treba nalaziti ispred note ili -2
	 * @param chord je li nota dio akorda
	 * @param leftHand svira li se nota lijevom rukom
	 * @param startTime broj slike u kojoj se nota prvi put pojavljuje
	 * @param endTime broj slike u kojoj se nota zadnji put pojavljuje
	 */
	public Note(boolean rest, Pitch pitch, int accidental, boolean chord, boolean leftHand, int startTime, int endTime) {
		this.rest = rest;
		this.pitch = pitch;
		this.accidental = accidental;
		this.chord = chord;
		this.leftHand = leftHand;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Konstruktor stvara novu pauzu koristeći dane parametre.
	 * 
	 * @param leftHand nalazi li se pauza u lijevoj ruci
	 * @param duration relativno trajanje pauze
	 * @param type vrsta pauze po trajanju
	 */
	public Note(boolean leftHand, int duration, NoteType type) {
		this.rest = true;
		this.leftHand = leftHand;
		this.duration = duration;
		this.type = type;
	}
	
	/**
	 * Vraća je li nota pauza ili ne.
	 * 
	 * @return <code>true</code> ako je nota pauza, <code>false</code> inače
	 */
	public boolean isRest() {
		return rest;
	}
	
	/**
	 * Vraća svira li se nota lijevom rukom.
	 * 
	 * @return <code>true</code> ako se nota svira lijevom rukom, <code>false</code> inače
	 */
	public boolean isLeftHand() {
		return leftHand;
	}
	
	/**
	 * Postavlja s kojom se rukom svira nota.
	 * 
	 * @param leftHand svira li se nota s lijevom rukom
	 */
	public void setLeftHand(boolean leftHand) {
		this.leftHand = leftHand;
	}
	
	/**
	 * Vraća broj slike u kojoj se nota prvi put pojavila.
	 * 
	 * @return broj slike u kojoj se nota prvi put pojavila
	 */
	public int getStartTime() {
		return startTime;
	}
	
	/**
	 * Vraća broj slike u kojoj se nota zadnji put pojavila.
	 * 
	 * @return broj slike u kojoj se nota zadnji put pojavila
	 */
	public int getEndTime() {
		return endTime;
	}
	
	/**
	 * Postavlja broj slike u kojoj se nota zadnji put pojavila.
	 * 
	 * @param endTime broj slike u kojoj se nota zadnji put pojavila
	 */
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Vraća broj slika koliko je nota trajala.
	 * 
	 * @return broj slika koliko je nota trajala
	 */
	public int getLength() {
		return endTime - startTime;
	}
	
	/**
	 * Vraća ton note.
	 * 
	 * @return ton note
	 */
	public Pitch getPitch() {
		return pitch;
	}
	
	/**
	 * Vraća znak koji se treba nalaziti ispred note ako ona ne spada u ljestvicu.
	 * Povisilica je 1, razrješilica je 0, snizilica je -1, a ako je nota u ljestvici vraća se -2.
	 * 
	 * @return znak koji se treba nalaziti ispred note ako ona ne spada u ljestvicu
	 */
	public int getAccidental() {
		return accidental;
	}
	
	/**
	 * Postavlja znak koji se treba nalaziti ispred note na zadani.
	 * Povisilica je 1, razrješilica je 0, snizilica je -1, a ako je nota u ljestvici je -2.
	 * 
	 * @param accidental znak koji se treba nalaziti ispred note na zadani
	 */
	public void setAccidental(int accidental) {
		this.accidental = accidental;
	}
	
	/**
	 * Vraća relativno trajanje note - trajanje u odnosu na zadano trajanje takta.
	 * 
	 * @return relativno trajanje note
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Postavlja relativno trajanje note - trajanje u odnosu na zadano trajanje takta.
	 * 
	 * @param duration relativno trajanje note
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Vraća vrstu note po trajanju.
	 * 
	 * @return vrsta note po trajanju
	 */
	public NoteType getNoteType() {
		return type;
	}
	
	/**
	 * Postavlja vrstu note po trajanju.
	 * 
	 * @param type vrsta note po trajanju
	 */
	public void setNoteType(NoteType type) {
		this.type = type;
	}
	
	/**
	 * Vraća počinje li na noti luk.
	 * 
	 * @return počinje li na noti luk
	 */
	public boolean isTiedStart() {
		return tiedStart;
	}
	
	/**
	 * Postavlja počinje li na noti luk.
	 * 
	 * @param tiedStart počinje li na noti luk
	 */
	public void setTiedStart(boolean tiedStart) {
		this.tiedStart = tiedStart;
	}
	
	/**
	 * Vraća završava li na noti luk.
	 * 
	 * @return završava li na noti luk
	 */
	public boolean isTiedStop() {
		return tiedStop;
	}
	
	/**
	 * Postavlja kraj luka na notu.
	 */
	public void setTiedStop() {
		tiedStop = true;
	}
	
	/**
	 * Vraća je li nota dio akorda.
	 * 
	 * @return je li nota dio akorda
	 */
	public boolean isChord() {
		return chord;
	}
	
	/**
	 * Postavlja notu kao dio akorda.
	 */
	public void setChord() {
		chord = true;
	}
	
	@Override
	public String toString() {
		return pitch + " start: " + startTime + " end: " + endTime + " len: " + getLength();
	}

	@Override
	public int compareTo(Note o) {
		int byStart = Integer.compare(startTime, o.startTime);
		if(byStart == 0 && pitch != null && o.pitch != null) {
			return pitch.compareTo(o.pitch);
		}
		
		return byStart;
	}
	
}
