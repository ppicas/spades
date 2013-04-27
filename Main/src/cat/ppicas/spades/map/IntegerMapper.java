package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class IntegerMapper implements ValueMapper {

	private Field mField;
	private boolean mPrimitive;

	public IntegerMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Integer.TYPE && type != Integer.class) {
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
				values.put(key, (Integer) mField.get(object));
			} else {
				Integer value = (Integer) mField.get(object);
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
				mField.setInt(object, cursor.getInt(index));
			} else {
				mField.set(object, cursor.isNull(index) ? null : cursor.getInt(index));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
