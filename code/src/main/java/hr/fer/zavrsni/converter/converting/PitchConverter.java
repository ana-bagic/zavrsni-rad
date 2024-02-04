package hr.fer.zavrsni.converter.converting;

import org.opencv.core.Rect;

import hr.fer.zavrsni.converter.keyboard.Keyboard;
import hr.fer.zavrsni.converter.model.note.NoteAlphabet;
import hr.fer.zavrsni.converter.model.note.Pitch;

/**
 * Razred služi za prepoznavanje tona note iz pravokutnika koji obrubljuje tu padajuću notu.
 * 
 * @author Ana Bagić
 *
 */
public class PitchConverter {

	/** Jedinstveni objekt ovog razreda. */
	private static PitchConverter pitchConverter = new PitchConverter();
	/** Klavijatura koja se koristi za prepozavanje tona. */
	private Keyboard keyboard;
	
	/**
	 * Defaultni privatni konstruktor.
	 */
	private PitchConverter() {}
	
	/**
	 * Vraća instancu {@link PitchConverter}-a (jedinstveni objekt).
	 * 
	 * @return instancu {@link PitchConverter}-a
	 */
	public static PitchConverter getInstance() {
		return pitchConverter;
	}
	
	/**
	 * Postavlja klavijaturu na danu.
	 * 
	 * @param keyboard klavijatura koja se koristi
	 */
	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}
	
	/**
	 * Postavlja širinu klavijature na danu.
	 * 
	 * @param keyboardWidth širina klavijature koja se koristi
	 */
	public void setKeyboardWidth(int keyboardWidth) {
		keyboard.setKeyboardWidth(keyboardWidth);
	}
	
	/**
	 * Pronalazi ton note koristeći pravokutnik koji obrubljuje tu notu.
	 * 
	 * @param rect pravokutnik koji obrubljuje tu notu
	 * @return odgovarajući ton note
	 */
	public Pitch findPitch(Rect rect) {
		double keyCenter = rect.x + rect.width/2.0;
		double ofsettedCenter = keyCenter + keyboard.getOffset();
		
		int octave = (int) (ofsettedCenter/keyboard.getOctaveWidth());
		double offsetInOctave = ofsettedCenter - octave*keyboard.getOctaveWidth();
		
		int tone = (int) (offsetInOctave/keyboard.getKeyWidth());
		int leftover = (int) (offsetInOctave%keyboard.getKeyWidth());
		
		boolean isSharp = keyboard.isSharp(leftover);
		boolean isFlat = keyboard.isFlat(leftover);
		
		NoteAlphabet alphabet = switch (tone) {
		case 0 -> isSharp ? NoteAlphabet.CSH : NoteAlphabet.C;
		case 1 -> isSharp ? NoteAlphabet.DSH : (isFlat ? NoteAlphabet.CSH : NoteAlphabet.D);
		case 2 -> isFlat ? NoteAlphabet.DSH : NoteAlphabet.E;
		case 3 -> isSharp ? NoteAlphabet.FSH : NoteAlphabet.F;
		case 4 -> isSharp ? NoteAlphabet.GSH : (isFlat ? NoteAlphabet.FSH : NoteAlphabet.G);
		case 5 -> isSharp ? NoteAlphabet.ASH : (isFlat ? NoteAlphabet.GSH : NoteAlphabet.A);
		default -> isFlat ? NoteAlphabet.ASH : NoteAlphabet.H;
		};
		
		return new Pitch(alphabet, keyboard.getRealOctave(octave));
	}

}
