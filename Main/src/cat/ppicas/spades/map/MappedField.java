package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import cat.ppicas.spades.ColumnBuilder.ColumnType;
import android.content.ContentValues;
import android.database.Cursor;

public interface MappedField {

	public void putContetValue(Object object, ContentValues values, String key, boolean notNull);

	public void setFieldValue(Object object, Cursor cursor, int index);

	public Field getField();

	public ColumnType getColumnType();

}
