package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class StringMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			String string = (String) field.get(object);
			if (string != null || (string == null && !notNull)) {
				values.put(key, (String) string);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			field.set(object, cursor.isNull(index) ? null : cursor.getString(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
