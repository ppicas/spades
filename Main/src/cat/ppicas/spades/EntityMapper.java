package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class EntityMapper<T extends Entity> {

	private Table<T> mTable;
	private List<Column> mMappedColumns = new ArrayList<Column>();

	public EntityMapper(Table<T> table) {
		mTable = table;
		if (mTable.getColumns().isEmpty()) {
			throw new IllegalStateException("Columns list is empty");
		}
		if (mTable.getColumnId() == null) {
			throw new IllegalStateException("ColumnId not defined");
		}
		for (Column column : table.getColumns()) {
			if (column.valueMapper != null) {
				mMappedColumns.add(column);
			}
		}
	}

	public int[][] getDefaultMappings() {
		return new MappingsBuilder().add(mTable).build();
	}

	public void putContentValues(T entity, ContentValues values) {
		for (Column column : mMappedColumns) {
			column.valueMapper.putContetValue(entity, values, column.name, column.notNull);
		}
		mapContentValues(entity, values);
	}

	public T createFromCursor(Cursor cursor, int[] mappings) {
		int colIdIndex = mappings[mTable.getColumnId().index];
		if (colIdIndex != -1 && cursor.isNull(colIdIndex)) {
			return null;
		}

		T entity = newInstance(cursor, mappings);
		if (colIdIndex != -1) {
			entity.setEntityId(cursor.getLong(colIdIndex));
		}
		for (Column column : mMappedColumns) {
			int index = mappings[column.index];
			if (index != -1) {
				column.valueMapper.setFieldValue(entity, cursor, index);
			}
		}
		mapCursorValues(entity, cursor, mappings);

		return entity;
	}

	protected abstract T newInstance(Cursor cursor, int[] mappings);

	protected abstract void mapContentValues(T entity, ContentValues values);

	protected abstract void mapCursorValues(T entity, Cursor cursor, int[] mappings);

}
