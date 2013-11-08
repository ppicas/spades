package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.query.Query;

public class RelatedList<T extends Entity> {

	private Entity mParent;
	private Column mChildColumn;
	private Table mChildTable;
	private EntityMapper<T> mChildMapper;
	private String mExtraWhere;

	private boolean mFetched;
	private List<T> mChilds;

	public RelatedList(Entity parent, Column childColumn, EntityMapper<T> childMapper) {
		mParent = parent;
		mChildColumn = childColumn;
		mChildTable = mChildColumn.getTable();
		mChildMapper = childMapper;
	}

	public RelatedList(Entity parent, Column childColumn, EntityMapper<T> childMapper, String extraWhere) {
		this(parent, childColumn, childMapper);
		mExtraWhere = extraWhere;
	}

	public boolean isFetched() {
		return mFetched;
	}

	public void reset() {
		mChilds = null;
		mFetched = false;
	}

	public List<T> fetch(SQLiteDatabase db) {
		if (!mFetched && mParent.getEntityId() != null) {
			Query query = createFetchQuery();
			Cursor cursor = query.execute(db);

			mChilds = new ArrayList<T>();
			cursor.moveToPosition(-1);
			CursorInfo cursorInfo = query.getCursorInfo();
			while (cursor.moveToNext()) {
				T entity = mChildMapper.createFromCursor(cursor, cursorInfo);
				mChilds.add(entity);
			}

			cursor.close();
			mFetched = true;
		}

		return mChilds;
	}

	public List<T> fetch(Cursor cursor, CursorInfo cursorInfo) {
		if (!mFetched) {
			// Check if the cursor contains data for child entity.
			if (!cursorInfo.hasTable(mChildTable)) {
				return null;
			}

			mChilds = new ArrayList<T>();
			Long parentId = mParent.getEntityId();
			int childColIndex = cursorInfo.getColumnIndex(mChildColumn);

			int oldPosition = cursor.getPosition();
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				// If we have enough information check if the child entity ID is
				// equal to the parent ID, if they are different then we discard
				// this row.
				if (childColIndex != -1 && !cursor.isNull(childColIndex) && parentId != null
						&& !parentId.equals(cursor.getLong(childColIndex))) {
					continue;
				}

				T entity = mChildMapper.createFromCursor(cursor, cursorInfo);
				mChilds.add(entity);
			}

			cursor.moveToPosition(oldPosition);
			mFetched = true;
		}

		return mChilds;
	}

	public void fetchAndAddOne(Cursor cursor, CursorInfo cursorInfo) {
		// Check if the cursor contains data for this entity.
		if (!cursorInfo.hasTable(mChildTable)) {
			return;
		}

		// We only add the children that can be validated as a related entity of this parent.
		int childColIndex = cursorInfo.getColumnIndex(mChildColumn);
		if (childColIndex == -1 || cursor.isNull(childColIndex) || mParent.getEntityId() == null
				|| !mParent.getEntityId().equals(cursor.getLong(childColIndex))) {
			return;
		}

		if (mChilds == null) {
			mChilds = new ArrayList<T>();
		}
		T child = mChildMapper.createFromCursor(cursor, cursorInfo);
		mChilds.add(child);
		mFetched = true;
	}

	public List<T> getList() {
		return mChilds;
	}

	public void setList(List<T> entities) {
		mChilds = entities;
		mFetched = true;
	}

	public void add(T entity) {
		if (mChilds == null) {
			mChilds = new ArrayList<T>();
		}
		mChilds.add(entity);
		mFetched = true;
	}

	protected Query createFetchQuery() {
		Query query;
		if (mParent.getEntityId() != null) {
			query = new Query(mChildTable).where(mChildColumn, "=" + mParent.getEntityId());
		} else {
			query = new Query(mChildTable).where(mChildColumn, "ISNULL");
		}

		if (mExtraWhere != null) {
			query.where(mExtraWhere);
		}

		return query;
	}

}
