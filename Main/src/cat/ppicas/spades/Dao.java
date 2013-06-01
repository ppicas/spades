package cat.ppicas.spades;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.map.RelatedMapper;
import cat.ppicas.spades.query.Query;

public abstract class Dao<T extends Entity> {

	public static interface EntityConsumer<T extends Entity> {

		public void accept(T entity, Cursor cursor);

	}

	protected SQLiteDatabase mDb;
	private Table<T> mTable;
	private EntityMapper<T> mMapper;
	private List<Field> mRelatedFields = new ArrayList<Field>();

	public Dao(SQLiteDatabase db, Table<T> table, EntityMapper<T> mapper) {
		mDb = db;
		mTable = table;
		mMapper = mapper;

		for (Column column : table.getColumns()) {
			if (column.valueMapper instanceof RelatedMapper) {
				mRelatedFields.add(((RelatedMapper) column.valueMapper).getRelatedField());
			}
		}
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
		try {
			return fetchFirst(cursor, mMapper.getDefaultMappings());
		} finally {
			cursor.close();
		}
	}

	public List<T> getAll() {
		Cursor cursor = mDb.query(mTable.getName(), new String[] { "*" }, null, null, null, null, null);
		try {
			return fetchAll(cursor, mMapper.getDefaultMappings());
		} finally {
			cursor.close();
		}
	}

	public T fetchFirst(Cursor cursor, int[][] mappings) {
		if (!cursor.moveToFirst() || mTable.index >= mappings.length
				|| mappings[mTable.index] == null) {
			return null;
		}

		T entity = mMapper.createFromCursor(cursor, mappings[mTable.index]);
		fetchRelatedFields(cursor, mappings, entity);

		return entity;
	}

	public T fetchFirst(Query query) {
		Cursor cursor = query.execute(mDb);
		try {
			return fetchFirst(cursor, query.getMappings());
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Cursor cursor, int[][] mappings) {
		return fetchAll(cursor, mappings, null);
	}

	public List<T> fetchAll(Cursor cursor, int[][] mappings, EntityConsumer<T> consumer) {
		ArrayList<T> list = new ArrayList<T>();

		if (mTable.index < mappings.length && mappings[mTable.index] != null) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				T entity = mMapper.createFromCursor(cursor, mappings[mTable.index]);
				fetchRelatedFields(cursor, mappings, entity);
				if (consumer != null && entity != null) {
					consumer.accept(entity, cursor);
				}
				list.add(entity);
			}
		}

		return list;
	}

	public List<T> fetchAll(Query query) {
		Cursor cursor = query.execute(mDb);
		try {
			return fetchAll(cursor, query.getMappings(), null);
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Query query, EntityConsumer<T> consumer) {
		Cursor cursor = query.execute(mDb);
		try {
			return fetchAll(cursor, query.getMappings(), consumer);
		} finally {
			cursor.close();
		}
	}

	protected void fetchRelatedFields(Cursor cursor, int[][] mappings, T entity) {
		for (Field relatedField : mRelatedFields) {
			try {
				@SuppressWarnings("unchecked")
				Related<T> related = (Related<T>) relatedField.get(entity);
				related.fetch(cursor, mappings);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
