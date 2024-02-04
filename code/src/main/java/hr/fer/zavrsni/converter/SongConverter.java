package hr.fer.zavrsni.converter;

/**
 * Razred korišten za testiranje aplikacije bez korisničkog sučelja.
 * 
 * @author Ana Bagić
 *
 */
public class SongConverter {

	/*
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Converter.getInstance().setPathToVideo("C:\\Users\\anaba\\Documents\\FER\\ZAVRSNI\\lovely.mp4");
		PitchConverter.getInstance().setKeyboard(new FullKeyboard());
		PitchConverter.getInstance().setKeyboardWidth(1280);
		Converter.getInstance().setSongMetadata(new SongMetadata("Pjesma", "Artist"));
		
		Converter.getInstance().getSong().setTimeSignature(new TimeSignature(4, NoteType.QUARTER));
		Converter.getInstance().getSong().setBeatsPerMinute(132);
		
		Converter con = Converter.getInstance();
		con.extractNotes(new Point(0, 0), new Point(1280, 500));
		
		ScaleUtil.findScale(con.getSong());
		ScaleUtil.fitToScale(con.getSong());
		
		TempoUtil.findTempo(con.getSong());
		TempoUtil.createTimeline(con.getSong(), con.getFirstFrame());
		
		String document = XMLUtil.generate(con.getSong());
		try {
			Files.write(Paths.get("C:\\Users\\anaba\\Documents\\FER\\proba.musicxml"), document.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
