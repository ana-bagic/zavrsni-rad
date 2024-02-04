package hr.fer.zavrsni.converter.keyboard;

/**
 * Razred predstavlja potpunu klavijaturu, odnosno klavijaturu u rasponu A0-C8.
 * 
 * @author Ana Bagić
 *
 */
public class FullKeyboard extends Keyboard {
	
	@Override
	public int getWhiteKeys() {
		return 52;
	}

	@Override
	public double getOffset() {
		return 5*getKeyWidth();
	}

	@Override
	public int getRealOctave(int octave) {
		return octave;
	}

	@Override
	public String getRange() {
		return "A0 - C8";
	}

}
