package cat.ppicas.spades.text;

import static android.text.TextUtils.join;

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
	 * Generate a list of possible entity field names from the words of a table
	 * column name.
	 *
	 * <p>
	 * For example if we have a column name {@code two_words} then this method
	 * will receive the following words array: {@code two} and {@code words}.
	 * Then with the previous array it will generate the following list:
	 * <ul>
	 * <li>mTwoWords</li>
	 * <li>twoWords</li>
	 * <li>TwoWords</li>
	 * <li>twowords</li>
	 * <li>two_words</li>
	 * <li>Two_Words</li>
	 * <li>TWO_WORDS</li>
	 * </ul>
	 * </p>
	 *
	 * @param words An array of words.
	 * @return An array of strings with the possible names.
	 */
	public static Set<String> generateFieldNames(String[] words) {
		Set<String> names = new HashSet<String>();

		String[] capitalizedWords = capitalize(words);
		String capitalized = concat(capitalizedWords);

		String[] lowerCasedWords = lowerCase(words);
		String lowerCased = concat(lowerCasedWords);

		names.add("m" + capitalized);
		names.add(firstCharToLowerCase(capitalized));
		names.add(capitalized);
		names.add(lowerCased);
		names.add(join("_", lowerCasedWords));
		names.add(join("_", capitalizedWords));
		names.add(join("_", lowerCasedWords).toUpperCase(Locale.US));

		return names;
	}


	private static String concat(String[] words) {
		StringBuffer buffer = new StringBuffer();
		for (String word : words) {
			buffer.append(word);
		}
		return buffer.toString();
	}

	private static String[] capitalize(String... words) {
		String[] capitalized = new String[words.length];
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			capitalized[i] = capitalize(word);
		}
		return capitalized;
	}

	private static String capitalize(String word) {
		return word.substring(0, 1).toUpperCase(Locale.US)
				+ word.substring(1).toLowerCase(Locale.US);
	}

	private static String[] lowerCase(String[] words) {
		String[] lowerCased = new String[words.length];
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			lowerCased[i] = word.toLowerCase(Locale.US);
		}
		return lowerCased;
	}

	private static String firstCharToLowerCase(String text) {
		return text.substring(0, 1).toLowerCase(Locale.US) + text.substring(1);
	}

}
