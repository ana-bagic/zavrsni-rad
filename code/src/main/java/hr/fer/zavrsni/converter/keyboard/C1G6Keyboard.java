package hr.fer.zavrsni.converter.keyboard;

/**
 * Klavijatura u rasponu C1 - G6.
 * 
 * @author Ana Bagić
 *
 */
public class C1G6Keyboard extends Keyboard {

	@Override
	public int getWhiteKeys() {
		return 40;
	}

	@Override
	public double getOffset() {
		return 0;
	}

	@Override
	public int getRealOctave(int octave) {
		return octave + 1;
	}

	@Override
	public String getRange() {
		return "C1 - G6";
	}

}
