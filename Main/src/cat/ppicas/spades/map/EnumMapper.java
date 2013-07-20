package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class EnumMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Enum<?> enumValue = (Enum<?>) field.get(object);
			if (enumValue != null || (enumValue == null && !notNull)) {
				values.put(key, (String) enumValue.name());
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			if (cursor.isNull(index)) {
				field.set(object, null);
			} else {
				String name = cursor.getString(index);
				@SuppressWarnings("unchecked")
				Enum<?> value = Enum.valueOf(field.getType().asSubclass(Enum.class), name);
				field.set(object, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
