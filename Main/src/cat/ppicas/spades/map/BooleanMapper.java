package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class BooleanMapper implements ValueMapper {

	private Field mField;
	private boolean mPrimitive;

	public BooleanMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Boolean.TYPE && type != Boolean.class) {
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
				values.put(key, (Boolean) mField.get(object));
			} else {
				Boolean value = (Boolean) mField.get(object);
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
				mField.setBoolean(object, cursor.getShort(index) == 1);
			} else {
				mField.set(object, cursor.isNull(index) ? null : cursor.getShort(index) == 1);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
