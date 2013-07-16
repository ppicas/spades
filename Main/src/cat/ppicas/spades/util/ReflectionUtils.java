package cat.ppicas.spades.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class ReflectionUtils {

	public static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
		Field field = findField(cls, fieldName);
		if (field == null) {
			throw new NoSuchFieldException(fieldName);
		}

		return field;
	}

	public static Field findField(Class<?> cls, String fieldName) {
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}
			cls = cls.getSuperclass();
		}

		return null;
	}

	public static Field findField(Class<?> cls, Collection<String> fieldNames) {
		for (String fieldName : fieldNames) {
			Field field = findField(cls, fieldName);
			if (field != null) {
				return field;
			}
		}

		return null;
	}

}
