package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class DoubleMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			if (field.getType().isPrimitive()) {
				values.put(key, (Double) field.get(object));
			} else {
				Double value = (Double) field.get(object);
				if (value != null || (value == null && !notNull)) {
					values.put(key, (value == null ? null : value));
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			if (field.getType().isPrimitive()) {
				field.setDouble(object, cursor.getDouble(index));
			} else {
				field.set(object, cursor.isNull(index) ? null : cursor.getDouble(index));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
