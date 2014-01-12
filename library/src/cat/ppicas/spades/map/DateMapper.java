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
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class DateMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Date date = (Date) field.get(object);
			if (date != null || (date == null && !notNull)) {
				values.put(key, (Long) (date == null ? null : date.getTime()));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			field.set(object, cursor.isNull(index) ? null : new Date(cursor.getLong(index)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
