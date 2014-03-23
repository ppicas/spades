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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import cat.picas.spades.map.MappedField;

public abstract class EntityMapper<T extends Entity> {

	private Table mTable;
	private List<Column> mMappedColumns = new ArrayList<Column>();

	public EntityMapper(Table table) {
		mTable = table;
		if (mTable.getColumnsSize() == 0) {
			throw new IllegalStateException("Columns list is empty");
		}
		if (mTable.getColumnId() == null) {
			throw new IllegalStateException("ColumnId not defined");
		}
		for (Column column : table.getColumns()) {
			if (column.getMappedField() != null) {
				mMappedColumns.add(column);
			}
		}
	}

	public Table getTable() {
		return mTable;
	}

	public CursorInfo getDefaultCursorInfo() {
		return new CursorInfoBuilder().add(mTable).build();
	}

	public void putContentValues(T entity, ContentValues values) {
		for (Column column : mMappedColumns) {
			column.getMappedField().putContetValue(entity, values, column.name, column.isNotNull());
		}
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
			entity.setEntityId(cursor.getLong(colIdIndex));
		}

		// Automatic population of the entity fields via reflection.
		for (Column column : mMappedColumns) {
			int index = cursorInfo.getColumnIndex(column);
			if (index != -1) {
                MappedField mappedField = column.getMappedField();
                mappedField.setFieldValue(entity, cursor, index);

                // Auto fetch Related mapped fields.
                if (mappedField.getField().getType() == Related.class) {
                    try {
                        Related<?> relatedField = (Related<?>) mappedField.getField().get(entity);
                        relatedField.fetch(cursor, cursorInfo);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
		}

		// Manual population of the entity implemented by user.
		mapCursorValues(entity, cursor, cursorInfo);

		return entity;
	}

	protected abstract T newInstance(Cursor cursor, CursorInfo cursorInfo);

	protected void mapContentValues(T entity, ContentValues values) {
    }

	protected void mapCursorValues(T entity, Cursor cursor, CursorInfo cursorInfo) {
    }

}
