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

package cat.picas.spades.fetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.Entity;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;

public class HashMapFetchStrategy<T extends Entity> implements FetchStrategy<T> {
	private Table mTable;
	private EntityMapper<T> mMapper;

	public HashMapFetchStrategy(Table table, EntityMapper<T> mapper) {
		mTable = table;
		mMapper = mapper;
	}

	@Override
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer) {
		if (!cursorInfo.hasTable(mTable)) {
			Collections.emptyList();
		}

		Map<Long, T> map = new HashMap<Long, T>();

		int oldPosition = cursor.getPosition();
		int idColIndex = cursorInfo.getColumnIndex(mTable.getColumnId());

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			T entity = map.get(cursor.getLong(idColIndex));
			if (entity == null) {
				entity = mMapper.createFromCursor(cursor, cursorInfo);
				if (entity != null) {
					map.put(entity.getEntityId(), entity);
				}
			}
			if (consumer != null && entity != null) {
				consumer.accept(cursor, cursorInfo, entity);
			}
		}

		cursor.moveToPosition(oldPosition);

		return new ArrayList<T>(map.values());
	}
}
