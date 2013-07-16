package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public interface ValueMapper {

	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull);

	public void setFieldValue(Field field, Object object, Cursor cursor, int index);

}
