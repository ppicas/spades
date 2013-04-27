package cat.ppicas.spades.map;

import android.content.ContentValues;
import android.database.Cursor;

public interface ValueMapper {

	public void putContetValue(Object object, ContentValues values, String key, boolean notNull);

	public void setFieldValue(Object object, Cursor cursor, int index);

}
