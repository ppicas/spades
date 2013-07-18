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
		if (!mFetched) {
			Query query = new Query(mRelatedTable).where(mRelatedColumn, "=" + getKey()).limit(1);
			if (mExtraWhere != null) {
				query.where(mExtraWhere);
			}
			Cursor cursor = query.execute(db);
			if (cursor.moveToFirst()) {
				int[][] mappings = query.getMappings();
				mEntity = mMapper.createFromCursor(cursor, mappings);
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
