package cat.ppicas.spades.text;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TextUtils {

	public static String[] splitIdentifierWords(String text) {
		String[] words;

		if (!text.contains("_")) {
			text = text.replaceAll("([a-z])([A-Z])", "$1_$2");
			text = text.replaceAll("([A-Z])([A-Z])([a-z])", "$1_$2$3");
		}

		words = text.split("_");
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].toLowerCase(Locale.US);
		}

		return words;
	}

	/**
	 * Generate a list of possible entity field names from a table column name.
	 *
	 * <p>
	 * For example with the table column named {@code two_words} this
	 * will return the following list:
	 * <ul>
	 * <li>two_words</li>
	 * <li>twoWords</li>
	 * <li>mTwoWords</li>
	 * </ul>
	 * </p>
	 *
	 * @param words The name of a table column.
	 * @return An array of strings with the possible names.
	 */
	public static Set<String> generateFieldNames(String[] words) {
		Set<String> names = new HashSet<String>();
		return names;
	}

	/*private static String toCamelCase(String text) {
		text = text.toLowerCase(Locale.US);
		StringBuilder result = new StringBuilder();
		for (String word : text.split("_")) {
			result.append(capitalize(word));
		}
		return result.toString();
	}

	private static String capitalize(String text) {
		return text.substring(0, 1).toUpperCase(Locale.US)
				+ text.substring(1).toLowerCase(Locale.US);
	}

	private static String firstCharToLowerCase(String text) {
		return text.substring(0, 1).toLowerCase(Locale.US) + text.substring(1);
	}

	private static String firstCharToUpperCase(String text) {
		return text.substring(0, 1).toUpperCase(Locale.US) + text.substring(1);
	}*/

}
