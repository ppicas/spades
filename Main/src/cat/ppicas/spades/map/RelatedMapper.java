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
		field.setAccessible(true);
		mRelatedField = field;
		try {
			mField = Related.class.getField("mValue");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
		try {
			values.put(key, (Integer) mField.get(mRelatedField.get(object)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Object object, Cursor cursor, int index) {
		try {
			mField.setInt(mRelatedField.get(object), cursor.getInt(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
