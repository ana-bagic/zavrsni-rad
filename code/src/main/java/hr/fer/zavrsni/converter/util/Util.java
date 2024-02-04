package hr.fer.zavrsni.converter.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Razred s pomoćnim funkcijama za ovu aplikaciju.
 * 
 * @author Ana Bagić
 *
 */
public class Util {
	
	/**
	 * Metoda pronalazi frekvencije pojavljivanja elemenata koji se mogu dobiti iz objekata iz poslanog {@link Iterable} skupa.
	 * 
	 * @param <T> elementi čije se frekvencije pojavljivanja traže
	 * @param <V> elementi iz kojih se mogu dobiti elementi &lt;T&gt;
	 * @param iterable skupni objekt iz čijih se elemenata mogu dobit elementi za traženje frekvencije
	 * @param mapper preslikava element skupnog objekta u element za koje se traži frekvencija
	 * @return mapu frekvencija pojavljivanja elemenata koji se mogu dobiti iz objekata iz poslanog {@link Iterable} skupa
	 */
	public static <T, V> Map<T, Integer> getFrequencies(Iterable<V> iterable, Function<V, T> mapper) {
		Map<T, Integer> frequencies = new HashMap<>();
		
		for(V it : iterable) {
			T element = mapper.apply(it);
			Integer f = frequencies.get(element);
			frequencies.put(element, f == null ? 1 : f + 1);
		}
		
		return frequencies;
	}

	/**
	 * Vraća listu ključeva dane mape sortiranu po vrijednostima dane mape.
	 * 
	 * @param <T> razred ključa mape
	 * @param original originalna mapa koju se želi sortirati
	 * @return novu listu ključeva mape sortiranu po vrijednostima
	 */
	public static <T> List<T> getListSortedByValues(Map<T, Integer> original) {
		TreeMap<T, Integer> sorted = new TreeMap<>((v1, v2) -> {
	        if (original.get(v1) >= original.get(v2))
	            return -1;
	        return 1;
		});
		
		sorted.putAll(original);
		return new LinkedList<>(sorted.keySet());
	}
}
