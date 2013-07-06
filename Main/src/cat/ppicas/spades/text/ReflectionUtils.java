package cat.ppicas.spades.text;

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

}