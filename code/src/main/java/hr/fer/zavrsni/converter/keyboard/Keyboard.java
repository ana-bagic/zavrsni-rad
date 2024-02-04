package hr.fer.zavrsni.converter.keyboard;

import hr.fer.zavrsni.converter.util.Constants;

/**
 * Razred predstavlja apstraktnu klavijaturu.
 * Konkretne klavijature se razlikuju po rasponu tipki.
 * 
 * @author Ana Bagić
 *
 */
public abstract class Keyboard {
	
	/** Širina klavijature tj. širina slike iz koje čitamo podatke. */
	private int keyboardWidth;
	
	/**
	 * Postavlja širinu klavijature na danu.
	 * 
	 * @param keyboardWidth širina klavijature
	 */
	public void setKeyboardWidth(int keyboardWidth) {
		this.keyboardWidth = keyboardWidth;
	}
	
	/**
	 * Računa širinu bijele tipke na klavijaturi.
	 * 
	 * @return širina bijele tipke u pikselima
	 */
	public double getKeyWidth() {
		return 1.0*keyboardWidth/getWhiteKeys();
	}
	
	/**
	 * Računa širinu jedne oktave na klavijaturi.
	 * 
	 * @return širina jedne oktave na klavijaturi u pikselima
	 */
	public double getOctaveWidth() {
		return 7*getKeyWidth();
	}

	/**
	 * Provjerava predstavlja li zadani odmak povišenu notu.
	 * 
	 * @param offset odmak od početka bijele tipke
	 * @return <code>true</code> ako odmak predstavlja povišenu notu, inače <code>false</code>
	 */
	public boolean isSharp(double offset) {
		 return offset > (1 - Constants.BLACK_IN_WHITE_PERC)*getKeyWidth();
	}

	/**
	 * Provjerava predstavlja li zadani odmak sniženu notu.
	 * 
	 * @param offset odmak od početka bijele tipke
	 * @return <code>true</code> ako odmak predstavlja sniženu notu, inače <code>false</code>
	 */
	public boolean isFlat(double offset) {
		return offset < Constants.BLACK_IN_WHITE_PERC*getKeyWidth();
	}
	
	/**
	 * Vraća broj bijelih tipki na klavijaturi.
	 * 
	 * @return broj bijelih tipki na klavijaturi
	 */
	public abstract int getWhiteKeys();
	
	/**
	 * U slučaju da klavijatura ne počinje tonom C, vraća broj piksela
	 * koji odgovara tipkama koje bi trebalo dodati na lijevu stranu klavijature da klavijatura počne tonom C.
	 * 
	 * @return odmak od početka tona C do prve bijele tipke na ovoj klavijaturi u pikselima
	 */
	public abstract double getOffset();
	
	/**
	 * Računa apsolutnu oktavu na temelju relativne.
	 * Relativne oktave počinju od 0, a apsolutne se nalaze u rasponu 0-8.
	 * 
	 * @param octave relativna oktava
	 * @return apsolutna oktava
	 */
	public abstract int getRealOctave(int octave);
	
	/**
	 * Vraća raspon klavijature u {@link String} obliku. Npr "A0 - C8".
	 * 
	 * @return raspon klavijature u {@link String} obliku
	 */
	public abstract String getRange();
	
}
