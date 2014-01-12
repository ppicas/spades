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

package cat.ppicas.spades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.query.Query;

public class Related<T extends Entity> {

	private ColumnId mParentColumn;
	private Table mParentTable;
	private EntityMapper<T> mParentMapper;
	private String mExtraWhere;

	private boolean mFetched;
	private Long mRawValue;
	private T mParent;

	public Related(ColumnId parentColumn, EntityMapper<T> parentMapper) {
		mParentColumn = parentColumn;
		mParentTable = mParentColumn.getTable();
		mParentMapper = parentMapper;
	}

	public Related(ColumnId parentColumn, EntityMapper<T> parentMapper, String extraWhere) {
		this(parentColumn, parentMapper);
		mExtraWhere = extraWhere;
	}

	public boolean isFetched() {
		return mFetched;
	}

	public void reset() {
		mParent = null;
		mFetched = false;
	}

	public T fetch(SQLiteDatabase db) {
		if (!mFetched && mRawValue != null) {
			Query query = createFetchQuery();
			Cursor cursor = query.execute(db);
			if (cursor.moveToFirst()) {
				mParent = mParentMapper.createFromCursor(cursor, query.getCursorInfo());
			} else {
				mParent = null;
			}
			cursor.close();
			mFetched = true;
		}

		return mParent;
	}

	public T fetch(Cursor cursor, CursorInfo cursorInfo) {
		if (!mFetched) {
			// Check if the cursor contains data for this entity.
			if (!cursorInfo.hasTable(mParentTable)) {
				return null;
			}

			// Obtain the cursor index of the related column.
			int parentColIndex = cursorInfo.getColumnIndex(mParentColumn);
			if (parentColIndex != -1 && !cursor.isNull(parentColIndex)) {
				if (mRawValue != null && !mRawValue.equals(cursor.getLong(parentColIndex))) {
					// Protection against incorrect entity assignments (ID != Foreign key).
					return null;
				} else if (mRawValue == null) {
					// Automatic assignment of raw value.
					mRawValue = cursor.getLong(parentColIndex);
				}
			}

			mParent = mParentMapper.createFromCursor(cursor, cursorInfo);
			mFetched = true;
		}

		return mParent;
	}

	public T get() {
		return mParent;
	}

	public void set(T entity) {
		mParent = entity;
		mRawValue = entity.getEntityId();
		mFetched = true;
	}

	public Long getRawValue() {
		return mRawValue;
	}

	public void setRawValue(Long value) {
		reset();
		mRawValue = value;
	}

	public boolean isNull() {
		return mRawValue == null;
	}

	protected Query createFetchQuery() {
		Query query = new Query(mParentTable).where(mParentColumn, "=" + mRawValue).limit(1);
		if (mExtraWhere != null) {
			query.where(mExtraWhere);
		}

		return query;
	}

}
