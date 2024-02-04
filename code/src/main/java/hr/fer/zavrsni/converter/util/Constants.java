package hr.fer.zavrsni.converter.util;

/**
 * Razred sadrži konstante korištene u aplikaciji.
 * 
 * @author Ana Bagić
 *
 */
public class Constants {
	
	/** Putanja u aplikaciji do direktorija sa datotekama potrebnima za vizualizaciju. */
	public static final String PATH_TO_VISUALIZATION = "/hr/fer/zavrsni/converter/visualization/";
	/** Putanja u aplikaciji do datoteke s popisom svih klavijatura. */
	public static final String PATH_TO_KEYBOARDS = "config/keyboards.txt";
	/** Putanja u aplikaciji do direktorija koji sadrži datoteke sa predlošcima za generiranje MusicXML dokumenta.  */
	public static final String PATH_TO_XML_TEMPLATES = "musicxml/";
	
	/** Pretpostavljeno ime pjesme. */
	public static final String DEFAULT_SONG_NAME = "Pjesma";
	/** Pretpostavljen autor pjesme. */
	public static final String DEFAULT_ARTIST_NAME = "Autor";
	
	/** Širina prozora. */
	public static final int WIDTH = 1280;
	/** Visina prozora. */
    public static final int HEIGHT = 720;
	
    /** Koliki postotak širine bijele tipke prekriva polovica crne tipke. */
    public static final double BLACK_IN_WHITE_PERC = 0.2;
    
	/** Za koliko se piksela nalazi kontrolna linija za detekciju nota iznad dna slike.  */
	public static final double CONTROL_DISTANCE = 25;
	/** Minimalna širina pravokutnika potrebna da se smatra notom. */
	public static final int MIN_WIDTH = 12;
	/** Minimalna visina pravokutnika potrebna da se smatra notom. */
	public static final int MIN_HEIGHT = 10;
	/** Koliko slika se dopušta da se ne može detektirati pravokutnik prije nego što se to smatra greškom.  */
	public static final Integer EXISTENCE = 4;
	/** Dozvoljeno odstupanje boje pravokutnika od boje pravokutnika koji se sviraju određenom rukom. */
	public static final double HUE_ERROR = 15;
	
	/** Veličina jezgre oko piksela koji se obrađuje Gaussovim zamućivanjem. */
	public static final double GAUSS_BLUR_SIZE = 3;
	/** Standardna devijacija Gaussove funkcije za dobivanje Gaussovog zamućivanja. */
	public static final double GAUSS_BLUR_DEV = 5;
	/** Prag ispod kojega se piksel ne smatra rubom u Canny funkciji. */
	public static final double CANNY_MIN = 80;
	/** Prag iznad kojega se piksel smatra sigurnim rubom u Canny funkciji. */
	public static final double CANNY_MAX = 250;
	/** Veličina jezgre za Sobel operator u Canny funkciji. */
	public static final int CANNY_APERTURE = 3;
	
	/** Dozvoljeno odstupanje dužine note u slikama od traženog. */
	public static final int NOTE_LEN_ERROR = 3;
	/** Dozvoljeno odstupanje udaljenosti broja slika dvije note da bi se smatrale akordom. */
	public static final int CHORD_ERROR = 2;
	/** Minimalni potrebni razmak između nota da bi se smatrao pauzom. */
	public static final int REST_ERROR = 2;
}
