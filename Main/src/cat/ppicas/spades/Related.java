package cat.ppicas.spades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.query.Query;

public class Related<T extends Entity> {

	private Column mRelatedColumn;
	private Table<?> mRelatedTable;
	private EntityMapper<T> mMapper;
	private String mExtraWhere;

	private boolean mFetched;
	private long mValue;
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

	public T fetch(SQLiteDatabase db) {
		if (!mFetched) {
			Query query = new Query(mRelatedTable).where(mRelatedColumn, "=" + getKey()).limit(1);
			if (mExtraWhere != null) {
				query.where(mExtraWhere);
			}
			int[][] tableMaps = query.getMappings();
			int[] maps = tableMaps[mRelatedTable.index];
			Cursor cursor = query.execute(db);
			if (maps != null && cursor.moveToFirst()) {
				mEntity = mMapper.createFromCursor(cursor, maps);
			} else {
				mEntity = null;
			}
			cursor.close();
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

	public long getKey() {
		return mValue;
	}

	public void setKey(long value) {
		mValue = value;
	}

}
