package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import cat.ppicas.spades.Related;

import android.content.ContentValues;
import android.database.Cursor;

public class RelatedMapper implements ValueMapper {

	private Field mRelatedField;
	private Field mField;

	public RelatedMapper(Field field) {
		Class<?> type = field.getType();
		if (type != Related.class) {
			throw new IllegalArgumentException("Invalid Field type");
		}
		mRelatedField = field;
		mRelatedField.setAccessible(true);
		try {
			mField = Related.class.getDeclaredField("mValue");
			mField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public Field getRelatedField() {
		return mRelatedField;
	}

	@Override
	public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
		try {
			Long value = (Long) mField.get(mRelatedField.get(object));
			if (value != null || (value == null && !notNull)) {
				values.put(key, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			mField.set(mRelatedField.get(object),
					cursor.isNull(index) ? null : cursor.getLong(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
