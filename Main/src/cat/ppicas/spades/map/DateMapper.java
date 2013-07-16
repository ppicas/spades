package cat.ppicas.spades.map;

import java.lang.reflect.Field;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class DateMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Date date = (Date) field.get(object);
			if (date != null || (date == null && !notNull)) {
				values.put(key, (Long) (date == null ? null : date.getTime()));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			field.set(object, cursor.isNull(index) ? null : new Date(cursor.getLong(index)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
