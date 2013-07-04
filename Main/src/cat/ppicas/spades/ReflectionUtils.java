package cat.ppicas.spades;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionUtils {

	private static Pattern sIsPattern = Pattern.compile("^(is[A-Z]|IS_|is_)");
	private static Pattern sIdPattern = Pattern.compile("([a-z]Id|_ID|_id)$");

	public static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}
			cls = cls.getSuperclass();
		}

		throw new NoSuchFieldException(fieldName);
	}

	public static Field findFields(Class<?> cls, String[] fieldNames) {
		return null;
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
	 * @param columnName The name of a table column.
	 * @return An array of strings with the possible names.
	 */
	public static Set<String> generateFieldNamesFromColumnName(String columnName) {
		Set<String> names = new HashSet<String>();

		names.add(columnName);
		names.add("m" + firstCharToUpperCase(columnName));

		names.add(columnName.toLowerCase(Locale.US));
		names.add("m" + firstCharToUpperCase(columnName.toLowerCase(Locale.US)));

		String camelCase = toCamelCase(columnName);
		names.add(firstCharToLowerCase(camelCase));
		names.add("m" + camelCase);

		Matcher matcher = sIsPattern.matcher(columnName);
		if (matcher.find()) {
			names.addAll(generateFieldNamesFromColumnName(matcher.replaceFirst("")));
		}

		matcher = sIdPattern.matcher(columnName);
		if (matcher.find()) {
			names.addAll(generateFieldNamesFromColumnName(matcher.replaceFirst("")));
		}

		return names;
	}

	private static String toCamelCase(String text) {
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
	}

}
