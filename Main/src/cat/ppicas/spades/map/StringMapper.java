package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class StringMapper implements ValueMapper {

	private Field mField;

	public StringMapper(Field field) {
		Class<?> type = field.getType();
		if (type != String.class) {
			throw new IllegalArgumentException("Invalid Field type");
		}
		field.setAccessible(true);
		mField = field;
	}

	@Override
	public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
		try {
			String string = (String) mField.get(object);
			if (string != null || (string == null && !notNull)) {
				values.put(key, (String) string);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			mField.set(object, cursor.isNull(index) ? null : cursor.getString(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
