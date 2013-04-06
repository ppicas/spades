package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class LongMapper implements ValueMapper {

	private Field mField;
	private boolean mPrimitive;

	public LongMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Long.TYPE && type != Long.class) {
			throw new IllegalArgumentException("Invalid Field type");
		}
		field.setAccessible(true);
		mField = field;
		mPrimitive = field.getType().isPrimitive();
	}

	@Override
	public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
		try {
			if (mPrimitive) {
				values.put(key, (Long) mField.get(object));
			} else {
				Long value = (Long) mField.get(object);
				if (value != null || (value == null && !notNull)) {
					values.put(key, (value == null ? null : value));
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			if (mPrimitive) {
				mField.setLong(object, cursor.getLong(index));
			} else {
				mField.set(object, cursor.isNull(index) ? null : cursor.getLong(index));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
