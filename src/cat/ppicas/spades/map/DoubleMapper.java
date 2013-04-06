package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class DoubleMapper implements ValueMapper {

	private Field mField;
	private boolean mPrimitive;

	public DoubleMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Double.TYPE && type != Double.class) {
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
				values.put(key, (Double) mField.get(object));
			} else {
				Double value = (Double) mField.get(object);
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
				mField.setDouble(object, cursor.getDouble(index));
			} else {
				mField.set(object, cursor.isNull(index) ? null : cursor.getDouble(index));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
