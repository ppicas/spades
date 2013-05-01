package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.query.Query;

public abstract class Dao<T extends Entity> {

	public static interface EntityConsumer<T extends Entity> {

		public void accept(T entity, Cursor cursor);

	}

	protected SQLiteDatabase mDb;
	private Table<T> mTable;
	private EntityMapper<T> mMapper;

	public Dao(SQLiteDatabase db, Table<T> table, EntityMapper<T> mapper) {
		mDb = db;
		mTable = table;
		mMapper = mapper;
	}

	public long insert(T entity) {
		ContentValues values = new ContentValues();
		mMapper.putContentValues(entity, values);
		values.putNull(mTable.getColumnId().name);

		long newId = mDb.insert(mTable.getName(), null, values);
		if (newId != -1) {
			entity.setEntityId(newId);
		}

		return newId;
	}

	public int update(T entity) {
		ContentValues values = new ContentValues();
		mMapper.putContentValues(entity, values);
		String colId = mTable.getColumnId().name;
		if (values.containsKey(colId)) {
			values.remove(colId);
		}

		return mDb.update(mTable.getName(), values, colId + "=" + entity.getEntityId(), null);
	}

	public boolean save(T entity) {
		if (entity.getEntityId() > 0) {
			return (update(entity) > 0);
		} else {
			return (insert(entity) != -1);
		}
	}

	public int delete(long id) {
		return mDb.delete(mTable.getName(), mTable.getColumnId().name + "=" + id, null);
	}

	public T get(long id) {
		Cursor cursor = mDb.query(mTable.getName(), new String[] { "*" },
				mTable.getColumnId().name + "=" + id, null, null, null, null, "1");
		return fetchFirst(cursor, true, mMapper.getDefaultMappings());
	}

	public List<T> getAll() {
		Cursor cursor = mDb.query(mTable.getName(), new String[] { "*" }, null, null, null, null, null);
		return fetchAll(cursor, true, mMapper.getDefaultMappings());
	}

	public T fetchFirst(Cursor cursor, boolean close, int[][] mappings) {
		try {
			if (!cursor.moveToFirst() || mTable.index >= mappings.length  || mappings[mTable.index] == null) {
				return null;
			}

			T object = mMapper.createFromCursor(cursor, mappings[mTable.index]);

			return object;
		} finally {
			if (close) {
				cursor.close();
			}
		}
	}

	public T fetchFirst(Query query) {
		return fetchFirst(query.execute(mDb), true, query.getMappings());
	}

	public List<T> fetchAll(Cursor cursor, boolean close, int[][] mappings) {
		return fetchAll(cursor, close, mappings, null);
	}

	public List<T> fetchAll(Cursor cursor, boolean close, int[][] mappings,
			EntityConsumer<T> consumer) {
		ArrayList<T> list = new ArrayList<T>();

		if (mappings.length > mTable.index && mappings[mTable.index] != null) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				T entity = mMapper.createFromCursor(cursor, mappings[mTable.index]);
				if (consumer != null) {
					consumer.accept(entity, cursor);
				}
				list.add(entity);
			}
		}
		if (close) {
			cursor.close();
		}

		return list;
	}

	public List<T> fetchAll(Query query) {
		return fetchAll(query.execute(mDb), true, query.getMappings());
	}

}
