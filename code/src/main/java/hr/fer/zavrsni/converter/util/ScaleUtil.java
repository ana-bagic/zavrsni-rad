package hr.fer.zavrsni.converter.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import hr.fer.zavrsni.converter.model.note.Note;
import hr.fer.zavrsni.converter.model.note.NoteAlphabet;
import hr.fer.zavrsni.converter.model.note.Pitch;
import hr.fer.zavrsni.converter.model.song.Scale;
import hr.fer.zavrsni.converter.model.song.Song;

/**
 * Pomoćni razred za određivanje tonaliteta pjesme i prilagođavanje nota tonalitetu.
 * 
 * @author Ana Bagić
 *
 */
public class ScaleUtil {
	
	/** Svi mogući tonovi poredani po visini. */
	private static List<NoteAlphabet> alphabet = new LinkedList<>();
	/** Sve moguće povisilice poredane po pojavljivanju u kvintnom krugu. */
	private static List<NoteAlphabet> sharps = new LinkedList<>();
	/** Sve moguće snizilice poredane po pojavljivanju u kvartnom krugu.*/
	private static List<NoteAlphabet> flats = new LinkedList<>();
	/** Svi tonaliteti poredani po poziciji u kvartnom/kvintnom krugu (od 7. pozicije u kvartnomm krugu do 7. pozicije u kvintnom krugu). */
	private static Map<Integer, NoteAlphabet> scales = new TreeMap<>((s1, s2) -> {
		int byPosition = Integer.compare(Math.abs(s2), Math.abs(s1));
		if(byPosition == 0) {
			return s1.compareTo(s2);
		}
		return byPosition;
	});
	
	static {
		alphabet.add(NoteAlphabet.C);
		alphabet.add(NoteAlphabet.CSH);
		alphabet.add(NoteAlphabet.D);
		alphabet.add(NoteAlphabet.DSH);
		alphabet.add(NoteAlphabet.E);
		alphabet.add(NoteAlphabet.F);
		alphabet.add(NoteAlphabet.FSH);
		alphabet.add(NoteAlphabet.G);
		alphabet.add(NoteAlphabet.GSH);
		alphabet.add(NoteAlphabet.A);
		alphabet.add(NoteAlphabet.ASH);
		alphabet.add(NoteAlphabet.H);
		
		sharps.add(NoteAlphabet.FSH);
		sharps.add(NoteAlphabet.CSH);
		sharps.add(NoteAlphabet.GSH);
		sharps.add(NoteAlphabet.DSH);
		sharps.add(NoteAlphabet.ASH);
		sharps.add(NoteAlphabet.F);
		sharps.add(NoteAlphabet.C);
		
		flats.add(NoteAlphabet.ASH);
		flats.add(NoteAlphabet.DSH);
		flats.add(NoteAlphabet.GSH);
		flats.add(NoteAlphabet.CSH);
		flats.add(NoteAlphabet.FSH);
		flats.add(NoteAlphabet.H);
		flats.add(NoteAlphabet.E);
		
		scales.put(-7, NoteAlphabet.H);
		scales.put(-6, NoteAlphabet.FSH);
		scales.put(-5, NoteAlphabet.CSH);
		scales.put(-4, NoteAlphabet.GSH);
		scales.put(-3, NoteAlphabet.DSH);
		scales.put(-2, NoteAlphabet.ASH);
		scales.put(-1, NoteAlphabet.F);
		scales.put(0, NoteAlphabet.C);
		scales.put(1, NoteAlphabet.G);
		scales.put(2, NoteAlphabet.D);
		scales.put(3, NoteAlphabet.A);
		scales.put(4, NoteAlphabet.E);
		scales.put(5, NoteAlphabet.H);
		scales.put(6, NoteAlphabet.FSH);
		scales.put(7, NoteAlphabet.CSH);
	}
	
	/**
	 * Na temelju dane pjesme pronalazi njen najvjerojatniji tonalitet i postavlja ga.
	 * 
	 * @param song pjesma za koju se želi pronaći tonalitet
	 */
	public static void findScale(Song song) {
		Map<NoteAlphabet, Integer> pitchFreq = Util.getFrequencies(song, n -> n.getPitch().getAlphabet());
		List<NoteAlphabet> sortedFreq = Util.getListSortedByValues(pitchFreq);
		List<NoteAlphabet> mostFreq10 = new LinkedList<>(sortedFreq.size() > 10 ? sortedFreq.subList(0, 10) : sortedFreq);
		
		Scale finalScale = null;
		if(sortedFreq.size() >= 5) {
			List<NoteAlphabet> mostFreq5 = mostFreq10.subList(0, 5);
			
			for(var scale : scales.entrySet()) {
				finalScale = checkIsScale(scale.getKey(), scale.getValue(), true, mostFreq5, mostFreq10);
				if(finalScale != null) break;
				
				finalScale = checkIsScale(scale.getKey(), scale.getValue(), false, mostFreq5, mostFreq10);
				if(finalScale != null) break;
			}
		}
		
		song.setScale(finalScale == null ? new Scale(0, true, NoteAlphabet.C) : finalScale);
	}
	
	/**
	 * Provjerava je li pjesma s danih najčešćih 5 i 10 tonova danog tonaliteta.
	 * 
	 * @param fifths redni broj tonaliteta u kvintnom/kvartnom krugu
	 * @param firstNote prva nota dur ljestvice
	 * @param isMajor je li tonalitet u duru ili molu
	 * @param mostFreq5 5 najčešćih tonova pjesme
	 * @param mostFreq10 10 najčešćih tonova pjesme
	 * @return novu ljestvicu ako je pjesma danog tonaliteta, <code>null</code> inače
	 */
	private static Scale checkIsScale(int fifths, NoteAlphabet firstNote, boolean isMajor, List<NoteAlphabet> mostFreq5, List<NoteAlphabet> mostFreq10) {
		Set<NoteAlphabet> chord = isMajor ? getMajorChord(firstNote) : getMinorChord(getMinor(firstNote));
		if(mostFreq5.containsAll(chord)) {
			Set<NoteAlphabet> alters = fifths < 0 ? getFlats(Math.abs(fifths)) : getSharps(fifths);
			
			if(mostFreq10.containsAll(alters)) {
				return new Scale(fifths, isMajor, firstNote);
			}
		}
		
		return null;
	}
	
	/**
	 * Note dane pjesme prilagođava tonalitetu pjesme. Dodaje povisilice/snizilice/razrješilce.
	 * 
	 * @param song pjesma koju se želi prilagoditi tonalitetu
	 */
	public static void fitToScale(Song song) {
		int fifths = song.getScale().getFifths();
		
		Set<NoteAlphabet> notesInScale = getMajorScale(song.getScale().getStartAlphabet());
		Set<NoteAlphabet> alters = fifths < 0 ? getFlats(-fifths) : getSharps(fifths);
		
		for(Note n : song) {
			Pitch p = n.getPitch();
			NoteAlphabet prevAl = p.getAlphabet();
			
			if(alters.contains(prevAl) || (!notesInScale.contains(prevAl) && sharps.subList(0, 5).contains(prevAl))) {
				if(fifths < 0) {
					if(prevAl == NoteAlphabet.H) {
						p.increaseOctave();
					}
					
					p.setAlter(-1);
					p.setAlphabet(getSharp(prevAl));
				} else {
					if(prevAl == NoteAlphabet.C) {
						p.decreaseOctave();
					}
					
					p.setAlter(1);
					p.setAlphabet(getFlat(prevAl));
				}
			}
			
			if(!notesInScale.contains(prevAl)) {
				n.setAccidental(p.getAlter());
			}
		}
	}
	
	/**
	 * Vraća povišeni ton u odnosu na dani.
	 * 
	 * @param n ton za koji se traži povišeni
	 * @return povišeni ton u odnosu na dani
	 */
	private static NoteAlphabet getSharp(NoteAlphabet n) {
		return getStep(n, 1);
	}
	
	/**
	 * Vraća sniženi ton u odnosu na dani.
	 * 
	 * @param n ton za koji se traži sniženi
	 * @return sniženi ton u odnosu na dani
	 */
	private static NoteAlphabet getFlat(NoteAlphabet n) {
		return getStep(n, 11);
	}
	
	/**
	 * Vraća početni ton mol ljestvice koja je komplementarna dur ljestvici s danim početnim tonom.
	 * 
	 * @param n ton za koji se želi dobiti početni ton komplementarne mol ljestvice
	 * @return početni ton mol ljestvice koja je komplementarna dur ljestvici s danim početnim tonom
	 */
	private static NoteAlphabet getMinor(NoteAlphabet n) {
		return getStep(n, 9);
	}
	
	/**
	 * Vraća ton udaljen od danog tona za dani broj stepeni.
	 * 
	 * @param n originalni ton
	 * @param step broj stepeni za koliko se želi pomaknuti dani ton 
	 * @return ton udaljen od danog tona za dani broj stepeni
	 */
	private static NoteAlphabet getStep(NoteAlphabet n, int step) {
		int index = alphabet.indexOf(n);
		index += step;
		return alphabet.get(index % alphabet.size());
	}
	
	/**
	 * Pronalazi durski akord početno s danim tonom.
	 * 
	 * @param n ton od kojega se želi izgraditi durski akord
	 * @return durski akord početno s danim tonom
	 */
	private static Set<NoteAlphabet> getMajorChord(NoteAlphabet n) {
		Set<NoteAlphabet> chord = new HashSet<>();
		
		chord.add(n);
		chord.add(getStep(n, 4));
		chord.add(getStep(n, 7));
		
		return chord;
	}
	
	/**
	 * Pronalazi molski akord početno s danim tonom.
	 * 
	 * @param n ton od kojega se želi izgraditi molski akord
	 * @return molski akord početno s danim tonom
	 */
	private static Set<NoteAlphabet> getMinorChord(NoteAlphabet n) {
		Set<NoteAlphabet> chord = new HashSet<>();
		
		chord.add(n);
		chord.add(getStep(n, 3));
		chord.add(getStep(n, 7));
		
		return chord;
	}
	
	/**
	 * Vraća sve povisilice ljestvice koja se u kvintnom krugu nalazi na danoj poziciji.
	 * 
	 * @param fifth redni broj ljestvice u kvintnom krugu
	 * @return sve povisilice dane ljestvice u kvintnom krugu
	 */
	private static Set<NoteAlphabet> getSharps(int fifth) {
		Set<NoteAlphabet> sharps = new HashSet<>();
		
		for(int i = 0; i < fifth; i++) {
			sharps.add(ScaleUtil.sharps.get(i));
		}
		
		return sharps;
	}
	
	/**
	 * Vraća sve snizilice ljestvice koja se u kvartnom krugu nalazi na danoj poziciji.
	 * 
	 * @param fifth redni broj ljestvice u kvartnom krugu
	 * @return sve snizilice dane ljestvice u kvartnom krugu
	 */
	private static Set<NoteAlphabet> getFlats(int fifth) {
		Set<NoteAlphabet> flats = new HashSet<>();
		
		for(int i = 0; i < fifth; i++) {
			flats.add(ScaleUtil.flats.get(i));
		}
		
		return flats;
	}

	/**
	 * Vraća sve tonove durske ljestvice početno s danim tonom.
	 * 
	 * @param n početni ton durske ljestvice
	 * @return sve tonove durske ljestvice početno s danim tonom
	 */
	private static Set<NoteAlphabet> getMajorScale(NoteAlphabet n) {
		Set<NoteAlphabet> scale = new HashSet<>();
		
		scale.add(n);
		scale.add(getStep(n, 2));
		scale.add(getStep(n, 4));
		scale.add(getStep(n, 5));
		scale.add(getStep(n, 7));
		scale.add(getStep(n, 9));
		scale.add(getStep(n, 11));
		
		return scale;
	}
	
}
