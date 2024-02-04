package hr.fer.zavrsni.converter.model.song;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Razred modelira metapodatke pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class SongMetadata {

	/** Ime pjesme. */
	private String songName;
	/** Ime autora pjesme. */
	private String artistName;
	/** Datum stvaranja .musicxml datoteke. */
	private Date date = new Date();
	
	/**
	 * Konstruktor stvara novi objekt koristeći ime i autora pjesme.
	 * 
	 * @param songName ime pjesme
	 * @param artistName ime autora pjesme
	 */
	public SongMetadata(String songName, String artistName) {
		this.songName = songName;
		this.artistName = artistName;
	}

	/**
	 * Vraća ime pjesme.
	 * 
	 * @return ime pjesme
	 */
	public String getSongName() {
		return songName;
	}
	
	/**
	 * Vraća ime autora pjesme.
	 * 
	 * @return ime autora pjesme
	 */
	public String getArtistName() {
		return artistName;
	}
	
	/**
	 * Vraća datum stvaranja .musicxml datoteke u formatu yyyy-MM-dd.
	 * 
	 * @return datum stvaranja .musicxml datoteke
	 */
	public String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}
