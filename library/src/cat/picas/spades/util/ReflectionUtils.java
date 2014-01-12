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

package cat.picas.spades.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class ReflectionUtils {

	public static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
		Field field = findField(cls, fieldName);
		if (field == null) {
			throw new NoSuchFieldException(fieldName);
		}

		return field;
	}

	public static Field findField(Class<?> cls, String fieldName) {
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}
			cls = cls.getSuperclass();
		}

		return null;
	}

	public static Field findField(Class<?> cls, Collection<String> fieldNames) {
		for (String fieldName : fieldNames) {
			Field field = findField(cls, fieldName);
			if (field != null) {
				return field;
			}
		}

		return null;
	}

}
