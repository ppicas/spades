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

import android.content.ContentValues;
import android.database.Cursor;

public abstract class EntityMapper<T extends Entity> {

	private Table mTable;

	public EntityMapper(Table table) {
		mTable = table;
		if (mTable.getColumnsSize() == 0) {
			throw new IllegalArgumentException("Columns list is empty");
		}
		if (mTable.getColumnId() == null) {
			throw new IllegalArgumentException("ColumnId not defined");
		}
	}

	public Table getTable() {
		return mTable;
	}

	public CursorInfo getDefaultCursorInfo() {
		return new CursorInfoBuilder().add(mTable).build();
	}

	public void putContentValues(T entity, ContentValues values) {
		mapContentValues(entity, values);
	}

	public T createFromCursor(Cursor cursor, CursorInfo cursorInfo) {
		if (!cursorInfo.hasTable(mTable)) {
			return null;
		}

		T entity = newInstance(cursor, cursorInfo);

		// Set the entity ID.
		int colIdIndex = cursorInfo.getColumnIndex(mTable.getColumnId());
		if (colIdIndex != -1) {
			if (cursor.isNull(colIdIndex)) {
				// As the entity ID is selected (the cursor has the column) and the value is null,
				// we should return null as this row doesn't contain any entity instance.
				return null;
			} else {
				entity.setEntityId(cursor.getLong(colIdIndex));
			}
		}

		// Manual population of the entity implemented by user.
		mapCursorValues(entity, cursor, cursorInfo);

		return entity;
	}

	protected abstract T newInstance(Cursor cursor, CursorInfo cursorInfo);

	protected abstract void mapContentValues(T entity, ContentValues values);

	protected abstract void mapCursorValues(T entity, Cursor cursor, CursorInfo cursorInfo);

}
