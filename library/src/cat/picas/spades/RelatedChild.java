/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cat.picas.spades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.picas.spades.query.Query;

public class RelatedChild<T extends Entity> {

	private Entity mParent;
	private Column mChildColumn;
	private Table mChildTable;
	private EntityMapper<T> mChildMapper;
	private String mExtraWhere;

	private boolean mFetched;
	private T mChild;

	public RelatedChild(Entity parent, Column childColumn, EntityMapper<T> childMapper) {
		mParent = parent;
		mChildColumn = childColumn;
		mChildTable = mChildColumn.getTable();
		mChildMapper = childMapper;
	}

	public RelatedChild(Entity parent, Column childColumn, EntityMapper<T> childMapper,
			String extraWhere) {
		this(parent, childColumn, childMapper);
		mExtraWhere = extraWhere;
	}

	public boolean isFetched() {
		return mFetched;
	}

	public void reset() {
		mChild = null;
		mFetched = false;
	}

	public T fetch(SQLiteDatabase db) {
		if (!mFetched && mParent.getEntityId() != null) {
			Query query = createFetchQuery();
			Cursor cursor = query.execute(db);
			if (cursor.moveToFirst()) {
				mChild = mChildMapper.createFromCursor(cursor, query.getCursorInfo());
			} else {
				mChild = null;
			}
			cursor.close();
			mFetched = true;
		}

		return mChild;
	}

	public T fetch(Cursor cursor, CursorInfo cursorInfo) {
		if (!mFetched) {
			// Check if the cursor contains data for this entity.
			if (!cursorInfo.hasTable(mChildTable)) {
				return null;
			}

			// If we have enough information check if the child entity ID is
			// equal to the parent ID, if they are different then we discard
			// this child.
			int childColIndex = cursorInfo.getColumnIndex(mChildColumn);
			if (childColIndex != CursorInfo.INVALID_INDEX
					&& !cursor.isNull(childColIndex) && mParent.getEntityId() != null
					&& !mParent.getEntityId().equals(cursor.getLong(childColIndex))) {
				return null;
			}

			mChild = mChildMapper.createFromCursor(cursor, cursorInfo);
			mFetched = true;
		}

		return mChild;
	}

	public T get() {
		return mChild;
	}

	public void set(T entity) {
		mChild = entity;
		mFetched = true;
	}

	protected Query createFetchQuery() {
		Query query = new Query(mChildTable).where(mChildColumn, "=" + mParent.getEntityId()).limit(1);
		if (mExtraWhere != null) {
			query.where(mExtraWhere);
		}

		return query;
	}

}
