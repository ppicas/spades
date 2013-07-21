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
