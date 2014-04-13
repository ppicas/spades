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
import java.util.List;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Entity;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;

public class ArrayListFetchStrategy<T extends Entity> implements FetchStrategy<T> {
	private Table mTable;
	private EntityMapper<T> mMapper;

	public ArrayListFetchStrategy(Table table, EntityMapper<T> mapper) {
		mTable = table;
		mMapper = mapper;
	}

	@Override
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer) {
		if (!cursorInfo.hasTable(mTable)) {
			Collections.emptyList();
		}

		ArrayList<T> list = new ArrayList<T>();

		int oldPosition = cursor.getPosition();

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			T entity = mMapper.createFromCursor(cursor, cursorInfo);
			if (entity != null) {
				list.add(entity);
				if (consumer != null) {
					consumer.accept(cursor, cursorInfo, entity);
				}
			}
		}

		cursor.moveToPosition(oldPosition);

		return list;
	}
}
