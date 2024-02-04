package hr.fer.zavrsni.converter.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.fer.zavrsni.converter.model.note.Note;
import hr.fer.zavrsni.converter.model.note.NoteAlphabet;
import hr.fer.zavrsni.converter.model.note.NoteType;
import hr.fer.zavrsni.converter.model.song.Measure;
import hr.fer.zavrsni.converter.model.song.Song;

/**
 * Pomoćni razred za generiranje musicxml datoteka.
 * 
 * @author Ana Bagić
 *
 */
public class XMLUtil {

	/** Predložak generalnog dokumenta. */
	private static String mainTemp;
	/** Oznaka koja označuje kraj zadnjeg takta u pjesmi. */
	private static String barlineTemp;
	/** Oznaka koja označuje bas ključ. */
	private static String clefFTemp;
	/** Predložak prvog takta u pjesmi. */
	private static String measure1Temp;
	/** Predložak taktova u pjesmi. */
	private static String measure2Temp;
	/** Predložak note. */
	private static String noteTemp;
	/** Predložak pauze.  */
	private static String restTemp;
	
	/** Mapa dodatnih oznaka za notu. */
	private static Map<String, String> extras = new HashMap<>();
	
	static {
		try {
			mainTemp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "main.xml"));
			barlineTemp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "barline.xml"));
			clefFTemp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "clefF.xml"));
			measure1Temp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "measure1.xml"));
			measure2Temp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "measure2.xml"));
			noteTemp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "note.xml"));
			restTemp = Files.readString(Paths.get(Constants.PATH_TO_XML_TEMPLATES + "rest.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		extras.put("acc", "<accidental>#ACC#</accidental>");
		extras.put("chord", "<chord />");
		extras.put("tieStart", "<tie type=\"start\" />");
		extras.put("tieStop", "<tie type=\"stop\" />");
		extras.put("notStart", "<notations><tied type=\"start\" /></notations>");
		extras.put("notStop", "<notations><tied type=\"stop\" /></notations>");
		extras.put("notStartStop", "<notations><tied type=\"start\" /><tied type=\"stop\" /></notations>");
	}
	
	/**
	 * Generira {@link String} MusicXML dokumenta i vraća ga.
	 * 
	 * @param song pjesma iz koje se generira dokument
	 * @return MusciXML datoteka u {@link String} formatu
	 */
	public static String generate(Song song) {
		String document = new String(mainTemp);
		
		document = document.replaceAll("#D#", song.getMetadata().getDate());
		document = document.replaceAll("#T#", song.getMetadata().getSongName());
		document = document.replaceAll("#A#", song.getMetadata().getArtistName());
		
		List<String> measures = new LinkedList<>();
		int i = 1;
		for(Measure m : song.getMeasures()) {
			if(i == 1) {
				measures.add(getFirstMeasure(song, m));
			} else {
				measures.add(getMeasure(song, m));
			}
			i++;
		}
		
		document = document.replaceFirst("#M#", measures.stream().collect(Collectors.joining("\n")));
		
		return document;
	}

	/**
	 * Metoda stvara ispis prvog takta pjesme.
	 * 
	 * @param song pjesma iz koje se generira dokument
	 * @param m prvi takt pjesme
	 * @return {@link String} xml reprezentacija prvog takta pjesme
	 */
	private static String getFirstMeasure(Song song, Measure m) {
		String measure = new String(measure1Temp);
		
		measure = measure.replaceFirst("#BU#", getNoteTypeString(song.getTimeSignature().getBeatUnit()));
		measure = measure.replaceAll("#BPM#", String.valueOf(song.getBeatsPerMinute()));
		measure = measure.replaceFirst("#DIV#", String.valueOf(song.getMeasureDuration() / song.getTimeSignature().getBeats()));
		
		measure = measure.replaceFirst("#FIF#", String.valueOf(song.getScale().getFifths()));
		measure = measure.replaceFirst("#MOD#", song.getScale().isMajor() ? "major" : "minor");
		
		measure = measure.replaceFirst("#BTS#", String.valueOf(song.getTimeSignature().getBeats()));
		measure = measure.replaceFirst("#BT#", switch(song.getTimeSignature().getBeatUnit()) {
		case HALF -> "2";
		case EIGHTH -> "8";
		default -> "4";
		});
		
		measure = measure.replaceFirst("#ST#", song.isTwoHands() ? "2" : "1");
		measure = measure.replaceFirst("#CLF#", song.isTwoHands() ? new String(clefFTemp) : "");
		
		measure = fillWithNotes(song, m, measure);
		
		return measure;
	}
	
	/**
	 * Metoda stvara ispis takta pjesme.
	 * 
	 * @param song pjesma iz koje se generira dokument
	 * @param m takt pjesme
	 * @return {@link String} xml reprezentacija takta pjesme
	 */
	private static String getMeasure(Song song, Measure m) {
		String measure = new String(measure2Temp);
		
		measure = measure.replaceFirst("#NR#", String.valueOf(m.getNumber()));
		measure = fillWithNotes(song, m, measure);
		measure = measure.replaceFirst("#BL#", m.getNumber() == song.getMeasures().size() ? new String(barlineTemp) : "");
		
		return measure;
	}
	
	/**
	 * Metoda puni dani {@link String} sa notama danog takta i postavlja mu trajanje.
	 * 
	 * @param song pjesma iz koje se generira dokument
	 * @param m takt čije se note žele dodati u {@link String} takta
	 * @param measure {@link String} koji se želi napuniti sa notama takta
	 * @return novi {@link String} takta
	 */
	private static String fillWithNotes(Song song, Measure m, String measure) {
		measure = measure.replaceFirst("#DUR#", String.valueOf(song.getMeasureDuration()));
		
		StringBuilder noteRight = new StringBuilder();
		for(Note n : m.getRightElements()) {
			noteRight.append(n.isRest() ? getRest(n) : getNote(n)).append("\n");
		}
		measure = measure.replaceFirst("#NOTR#", noteRight.toString());
		
		if(song.isTwoHands()) {
			StringBuilder noteLeft = new StringBuilder();
			for(Note n : m.getLeftElements()) {
				noteLeft.append(n.isRest() ? getRest(n) : getNote(n)).append("\n");
			}
			measure = measure.replaceFirst("#NOTL#", noteLeft.toString());
		} else {
			measure = measure.replaceFirst("#NOTL#", "");
		}
		
		return measure;
	}
	
	/**
	 * Metoda stvara ispis jedne note pjesme.
	 * 
	 * @param n nota pjesme
	 * @return {@link String} xml reprezentacija note pjesme
	 */
	private static String getNote(Note n) {
		String note = new String(noteTemp);
		
		NoteAlphabet a = n.getPitch().getAlphabet();
		note = note.replaceFirst("#S#", a == NoteAlphabet.H ? "B" : a.toString());
		note = note.replaceFirst("#O#", String.valueOf(n.getPitch().getOctave()));
		note = note.replaceFirst("#AL#", n.getPitch().getAlter() != 0
				? "<alter>" + String.valueOf(n.getPitch().getAlter()) + "</alter>"
				: "");
		
		note = note.replaceFirst("#ND#", String.valueOf(n.getDuration()));
		note = note.replaceFirst("#NT#", getNoteTypeString(n.getNoteType()));
		note = note.replaceFirst("#HD#", n.isLeftHand() ? String.valueOf(2) : String.valueOf(1));
		
		StringBuilder extrasString = new StringBuilder();
		if(n.getAccidental() != -2) {
			String acc = extras.get("acc");
			acc = acc.replaceFirst("#ACC#", switch(n.getAccidental()) {
			case -1 -> "flat";
			case 1 -> "sharp";
			default -> "natural";
			});
			
			extrasString.append(acc).append("\n");
		}
		
		if(n.isChord()) {
			extrasString.append(extras.get("chord")).append("\n");
		}
		
		if(n.isTiedStart()) {
			extrasString.append(extras.get("tieStart")).append("\n");
			if(n.isTiedStop()) {
				extrasString.append(extras.get("tieStop")).append("\n");
				extrasString.append(extras.get("notStartStop")).append("\n");
			} else {
				extrasString.append(extras.get("notStart")).append("\n");
			}
		} else if(n.isTiedStop()) {
			extrasString.append(extras.get("tieStop")).append("\n");
			extrasString.append(extras.get("notStop")).append("\n");
		}
		
		note = note.replaceFirst("#EXT#", extrasString.toString());
		return note;
	}
	
	/**
	 * Metoda stvara ispis jedne pauze pjesme.
	 * 
	 * @param n pauza pjesme
	 * @return {@link String} xml reprezentacija pauze pjesme
	 */
	private static String getRest(Note n) {
		String rest = new String(restTemp);
		
		rest = rest.replaceFirst("#ND#", String.valueOf(n.getDuration()));
		rest = rest.replaceFirst("#NT#", getNoteTypeString(n.getNoteType()));
		rest = rest.replaceFirst("#HD#", n.isLeftHand() ? String.valueOf(2) : String.valueOf(1));
		
		return rest;
	}
	
	/**
	 * Vraća {@link String} reprezentaciju tipa note u MusicXML datoteci.
	 * 
	 * @param type tip note
	 * @return {@link String} reprezentacija tipa note
	 */
	private static String getNoteTypeString(NoteType type) {
		return switch (type) {
		case WHOLE -> "whole";
		case HALF -> "half";
		case QUARTER -> "quarter";
		case EIGHTH -> "eighth";
		case N16TH -> "16th";
		case N32TH -> "32nd";
		};
	}
	
}
