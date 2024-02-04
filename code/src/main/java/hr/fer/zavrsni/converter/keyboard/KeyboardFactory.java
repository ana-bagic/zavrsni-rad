package hr.fer.zavrsni.converter.keyboard;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zavrsni.converter.util.Constants;

/**
 * Pomoćni razred za dohvat svih dostupnih klavijatura. 
 * 
 * @author Ana Bagić
 *
 */
@SuppressWarnings("unchecked")
public class KeyboardFactory {
	
	/** Lista svih trenutno podržanih klavijatura. */
	private static final List<Keyboard> keyboards = new LinkedList<>();
	
	static {
		try {
			List<String> keyboardFQCN = Files.lines(Paths.get(Constants.PATH_TO_KEYBOARDS)).collect(Collectors.toList());
			
			for(String k : keyboardFQCN) {
				Class<Keyboard> keyboard = (Class<Keyboard>)Class.forName(k);
				keyboards.add(keyboard.getConstructor().newInstance());
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Vraća listu svih trenutno podržanih klavijatura.
	 * 
	 * @return lista svih trenutno podržanih klavijatura
	 */
	public static List<Keyboard> getKeyboards() {
		return keyboards;
	}
}
