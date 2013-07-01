package cat.ppicas.spades;

import java.lang.reflect.Field;

public class ReflectionUtils {

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
	 * For example with the table column named {@code underscored_words} this
	 * will return the following list:
	 * <ul>
	 * <li>camleCase</li>
	 * <li>mCamleCase</li>
	 * </ul>
	 * </p>
	 *
	 * @param columnName The name of a table column.
	 * @return An array of strings with the possible names.
	 */
	public static String[] generateFieldNamesFromColumnName(String columnName) {
		return null;
	}

}
