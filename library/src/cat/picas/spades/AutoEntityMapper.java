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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import cat.picas.spades.map.MappedField;

public class AutoEntityMapper<T extends Entity> extends EntityMapper<T> {

	private List<Column> mMappedColumns = new ArrayList<Column>();

	public AutoEntityMapper(Table table) {
		super(table);

		for (Column column : table.getColumns()) {
			if (column.getMappedField() != null) {
				mMappedColumns.add(column);
			}
		}
	}

	@Override
	public T newInstance(Cursor cursor, CursorInfo cursorInfo) {
		Class<? extends Entity> clazz = getTable().getEntity();
		try {
			Constructor<T> constructor = (Constructor<T>) clazz.getConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (NoSuchMethodException e) {
			String error = String.format("The class %s must provide a no arguments constructor, " +
					"or override the AutoEntityMapper::newInstance()", clazz.getSimpleName());
			throw new RuntimeException(error, e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void mapContentValues(T entity, ContentValues values) {
		for (Column column : mMappedColumns) {
			column.getMappedField().putContetValue(entity, values, column.name, column.isNotNull());
		}
	}

	@Override
	public void mapCursorValues(T entity, Cursor cursor, CursorInfo cursorInfo) {
		// Automatic population of the entity fields via reflection.
		for (Column column : mMappedColumns) {
			int index = cursorInfo.getColumnIndex(column);
			if (index != CursorInfo.INVALID_INDEX) {
				MappedField mappedField = column.getMappedField();
				mappedField.setFieldValue(entity, cursor, index);
				autoFetchRelatedParentField(cursor, cursorInfo, entity, mappedField);
			}
		}
	}

	public AutoEntityMapper<T> alias() {
		return new AutoEntityMapper<T>(getTable().alias());
	}

	public AutoEntityMapper<T> alias(int aliasId) {
		return new AutoEntityMapper<T>(getTable().alias(1));
	}

	/**
	 * Auto fetch RelatedParent mapped fields.
	 *
	 * @param cursor a {@code Cursor} object with the current query
	 * @param cursorInfo a {@code CursorInfo} object holding the {@code Cursor} info
	 * @param entity an {@code Entity} instance that holds the {@link RelatedParent} field
	 * @param mappedField the {@link MappedField} of the {@code RelatedParent} field
	 */
	private void autoFetchRelatedParentField(Cursor cursor, CursorInfo cursorInfo, T entity,
			MappedField mappedField) {

		Field field = mappedField.getField();

		// Check that the class of the Field is RelatedParent, and that its parameterized type hasn't
		// the same type as the Entity. The latest check it's to avoid the RelatedParent fields that
		// point to the same Entity.
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) field.getGenericType();

			if (type.getRawType() != RelatedParent.class
					|| type.getActualTypeArguments().length != 1
					|| type.getActualTypeArguments()[0] == entity.getClass()) {
				return;
			}
		} else {
			return;
		}

		try {
			RelatedParent<?> relatedParentField = (RelatedParent<?>) field.get(entity);
			relatedParentField.fetch(cursor, cursorInfo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
