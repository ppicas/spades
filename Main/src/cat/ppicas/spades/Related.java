package cat.ppicas.spades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.query.Query;

public class Related<T extends Entity> {

	private Column mRelatedColumn;
	private Table mRelatedTable;
	private EntityMapper<T> mMapper;
	private String mExtraWhere;

	private boolean mFetched;
	private Long mValue;
	private T mEntity;

	public Related(Column relatedColumn, EntityMapper<T> mapper) {
		mRelatedColumn = relatedColumn;
		mRelatedTable = mRelatedColumn.table;
		mMapper = mapper;
	}

	public Related(Column relatedColumn, EntityMapper<T> mapper, String extraWhere) {
		this(relatedColumn, mapper);
		mExtraWhere = extraWhere;
	}

	public boolean isFetched() {
		return mFetched;
	}

	public void reset() {
		mFetched = false;
		mEntity = null;
	}

	public T fetch(SQLiteDatabase db) {
		if (!mFetched && mValue != null) {
			Query query = new Query(mRelatedTable).where(mRelatedColumn, "=" + mValue).limit(1);
			if (mExtraWhere != null) {
				query.where(mExtraWhere);
			}
			Cursor cursor = query.execute(db);
			if (cursor.moveToFirst()) {
				mEntity = mMapper.createFromCursor(cursor, query.getMappings());
			} else {
				mEntity = null;
			}
			cursor.close();
			mFetched = true;
		}

		return mEntity;
	}

	public T fetch(Cursor cursor, int[][] mappings) {
		if (!mFetched) {
			// Obtain the cursor index of the related column.
			int colIndex = -1;
			if (mRelatedTable.index < mappings.length && mappings[mRelatedTable.index] != null
					&& mappings[mRelatedTable.index][mRelatedColumn.index] != -1) {
				colIndex = mappings[mRelatedTable.index][mRelatedColumn.index];
			}

			if (colIndex != -1 && !cursor.isNull(colIndex)) {
				if (mValue != null && mValue != cursor.getLong(colIndex)) {
					// Protection against incorrect entity assignments.
					return null;
				} else if (mValue == null) {
					// Automatic assignment of mValue.
					mValue = cursor.getLong(colIndex);
				}
			}

			mEntity = mMapper.createFromCursor(cursor, mappings);
			mFetched = true;
		}

		return mEntity;
	}

	public T get() {
		return mEntity;
	}

	public void set(T entity) {
		mEntity = entity;
		mFetched = true;
	}

	public Long getKey() {
		return mValue;
	}

	public void setKey(Long value) {
		mValue = value;
	}

}
