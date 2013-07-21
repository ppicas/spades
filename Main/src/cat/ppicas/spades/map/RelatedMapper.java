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

package cat.ppicas.spades.map;

import java.lang.reflect.Field;

import cat.ppicas.spades.Related;

import android.content.ContentValues;
import android.database.Cursor;

public class RelatedMapper implements ValueMapper {

	@Override
	public void putContetValue(Field relatedField, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Related<?> related = (Related<?>) relatedField.get(object);
			Long value = related.getKey();
			if (value != null || (value == null && !notNull)) {
				values.put(key, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field relatedField, Object object, Cursor cursor, int index) {
		try {
			Related<?> related = (Related<?>) relatedField.get(object);
			related.setKey(cursor.isNull(index) ? null : cursor.getLong(index));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
