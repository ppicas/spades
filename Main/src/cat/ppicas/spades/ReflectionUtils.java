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

}
