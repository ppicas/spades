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
			values.put(key, (Long) mField.get(mRelatedField.get(object)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			mField.setLong(mRelatedField.get(object), cursor.getLong(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
