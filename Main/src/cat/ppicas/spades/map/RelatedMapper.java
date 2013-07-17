package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import cat.ppicas.spades.Related;

import android.content.ContentValues;
import android.database.Cursor;

public class RelatedMapper implements ValueMapper {

	@Override
	public void putContetValue(Field relatedField, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Related<?> related = (Related<?>) relatedField.get(object);
			Long value = related.getKey();
			if (value != null || (value == null && !notNull)) {
				values.put(key, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field relatedField, Object object, Cursor cursor, int index) {
		try {
			Related<?> related = (Related<?>) relatedField.get(object);
			related.setKey(cursor.isNull(index) ? null : cursor.getLong(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
