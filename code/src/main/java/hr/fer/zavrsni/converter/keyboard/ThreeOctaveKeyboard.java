package hr.fer.zavrsni.converter.keyboard;

/**
 * Razred predstavlja klavijaturu s 3 oktave, odnosno klavijaturu u rasponu C3-G5.
 * 
 * @author Ana Bagić
 *
 */
public class ThreeOctaveKeyboard extends Keyboard {

	@Override
	public int getWhiteKeys() {
		return 19;
	}
	
	@Override
	public double getOffset() {
		return 0;
	}

	@Override
	public int getRealOctave(int octave) {
		return octave + 3;
	}

	@Override
	public String getRange() {
		return "C3 - G5";
	}

}