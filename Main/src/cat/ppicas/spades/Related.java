package cat.ppicas.spades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.query.Query;

public class Related<T extends Entity> {

	private boolean mFetched;
	private Entity mEntity;
	private T mRelated;

	private EntityMapper<T> mMapper;
	private Column mRelatedColumn;
	private Column mOwnColumn;

	public Related(Entity entity) {
		mEntity = entity;
	}

	public boolean isFetched() {
		return mFetched;
	}

	public T fetch(SQLiteDatabase db) {
		if (!mFetched) {
			Query query = new Query(mOwnColumn.table)
					.select(mRelatedColumn.table)
					.leftJoin(mRelatedColumn.table, "%s = %s", mOwnColumn, mRelatedColumn)
					.where(mOwnColumn.table.getColumnId(), "=" + mEntity.getEntityId());
			int[][] tableMaps = query.getMappings();
			int[] maps = tableMaps[mRelatedColumn.table.index];
			Cursor cursor = query.execute(db);
			if (maps != null && cursor.moveToFirst()) {
				mRelated = mMapper.createFromCursor(cursor, maps);
			} else {
				mRelated = null;
			}
			cursor.close();
			mFetched = true;
		}

		return mRelated;
	}

	public T get() {
		return mRelated;
	}

	public void set(T entity) {
		mRelated = entity;
		mFetched = true;
	}

}
