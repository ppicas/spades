package cat.ppicas.spades.map;

import java.lang.reflect.Field;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class DateMapper implements ValueMapper {

	private Field mField;

	public DateMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Date.class) {
			throw new IllegalArgumentException("Invalid Field type");
		}
		field.setAccessible(true);
		mField = field;
	}

	@Override
	public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
		try {
			Date date = (Date) mField.get(object);
			if (date != null || (date == null && !notNull)) {
				values.put(key, (Long) (date == null ? null : date.getTime()));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			mField.set(object, cursor.isNull(index) ? null : new Date(cursor.getLong(index)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
