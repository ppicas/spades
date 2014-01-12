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

package cat.picas.spades.map;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class EnumMapper implements ValueMapper {

	@Override
	public void putContetValue(Field field, Object object, ContentValues values, String key, boolean notNull) {
		try {
			Enum<?> enumValue = (Enum<?>) field.get(object);
			if (enumValue != null || (enumValue == null && !notNull)) {
				values.put(key, (String) enumValue.name());
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFieldValue(Field field, Object object, Cursor cursor, int index) {
		try {
			if (cursor.isNull(index)) {
				field.set(object, null);
			} else {
				String name = cursor.getString(index);
				@SuppressWarnings("unchecked")
				Enum<?> value = Enum.valueOf(field.getType().asSubclass(Enum.class), name);
				field.set(object, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
