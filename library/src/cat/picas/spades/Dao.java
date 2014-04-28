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

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.picas.spades.fetch.ArrayListFetchStrategy;
import cat.picas.spades.fetch.FetchStrategy;
import cat.picas.spades.query.Query;

/**
 * This class is used to do CRUD operations with {@link Entity} objects. With an instance of
 * this class you can do operations with Entity objects, or in other words: read, create, update
 * or delete rows of a table.
 * <p>
 * To use this class you will need a {@link SQLiteDatabase} connection, a {@link Table} definition
 * and a {@link EntityMapper} to read or write the {@code Entity} fields during table row
 * operations.
 * <p>
 * A common pattern is to extend this class, and define the related {@code Table} and
 * {@code EntityMapper} as static final fields. For example, given an {@code Entity} called
 * {@code Example} the following code will define a {@code Dao} class to operate with this
 * {@code Entity}.
 * <p>
 * <pre><code>
 * public class Example implements Entity {
 *
 *     private Long mId;
 *
 *     private String mName;
 *
 *     {@literal @Override}
 *     public Long getEntityId() {
 *         return mId;
 *     }
 *
 *     {@literal @Override}
 *     public void setEntityId(Long id) {
 *         mId = id;
 *     }
 *
 *     public String getName() {
 *         return mName;
 *     }
 *
 *     public void setName(String name) {
 *         mName = name;
 *     }
 *
 * }
 *
 * public class ExampleDao extends Dao{@literal <Example>} {
 *
 *     // First declare a Table that defines the table for the entity. Every table
 *     // must declared only once, that is that must exist only Table instance
 *     // per database table.
 *     //
 *     // "examples" is the table name in the SQLite database.
 *     public static final Table TABLE = new TableBuilder("examples", Example.class)
 *             // Every table must have a column ID. "id" is the name of the column in the
 *             // table database.
 *             .columnId("id")
 *
 *             // Defines the column "name" in the database table. In this case this
 *             // column is defined with the type 'auto', this type requires the name
 *             // of the field in the Entity class ("mName") in order to use reflection
 *             // and resolve the type at runtime.
 *             .columnAuto("name", "mName").end()
 *
 *             // Create the Table
 *             .build();
 *
 *     // The next step is define a... TODO
 *
 * }
 * </code></pre>
 *
 * @see Table
 * @see EntityMapper
 */
public class Dao<T extends Entity> {

	public interface EntityConsumer<T extends Entity> {
		public void accept(Cursor cursor, CursorInfo cursorInfo, T entity);
	}

	private SQLiteDatabase mDb;
	private Table mTable;
	private EntityMapper<T> mMapper;
	private FetchStrategy<T> mFetchStrategy;

	public Dao(SQLiteDatabase db, Table table, EntityMapper<T> mapper) {
		this(db, table, mapper, new ArrayListFetchStrategy<T>(table, mapper));
	}

	public Dao(SQLiteDatabase db, Table table, EntityMapper<T> mapper, FetchStrategy<T> fetchStrategy) {
		mDb = db;
		mTable = table;
		mMapper = mapper;
		mFetchStrategy = fetchStrategy;
	}

	public long insert(T entity) {
		ContentValues values = new ContentValues();
		mMapper.putContentValues(entity, values);
		values.putNull(mTable.getColumnId().name);

		long newId = mDb.insertOrThrow(mTable.name, null, values);
		if (newId != -1) {
			entity.setEntityId(newId);
		}

		return newId;
	}

	public int update(T entity) {
		ContentValues values = new ContentValues();
		mMapper.putContentValues(entity, values);
		String colId = mTable.getColumnId().name;
		if (values.containsKey(colId)) {
			values.remove(colId);
		}

		return mDb.update(mTable.name, values, colId + "=" + entity.getEntityId(), null);
	}

	public long save(T entity) {
		if (entity.getEntityId() == null) {
			return insert(entity);
		} else {
			return update(entity);
		}
	}

	public int delete(long id) {
		return mDb.delete(mTable.name, mTable.getColumnId().name + "=" + id, null);
	}

	public T get(long id) {
		Cursor cursor = mDb.query(mTable.name, new String[] { "*" },
				mTable.getColumnId().name + "=" + id, null, null, null, null, "1");
		try {
			return fetchFirst(cursor, mMapper.getDefaultCursorInfo());
		} finally {
			cursor.close();
		}
	}

	public List<T> getAll() {
		Cursor cursor = mDb.query(mTable.name, new String[] { "*" }, null, null, null, null, null);
		try {
			return fetchAll(cursor, mMapper.getDefaultCursorInfo());
		} finally {
			cursor.close();
		}
	}

	public T fetchFirst(Cursor cursor, CursorInfo cursorInfo) {
		if (!cursor.moveToFirst()) {
			return null;
		}

		T entity = mMapper.createFromCursor(cursor, cursorInfo);

		return entity;
	}

	public T fetchFirst(Query query) {
		Cursor cursor = query.execute(mDb);
		try {
			return fetchFirst(cursor, query.getCursorInfo());
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Query query) {
		Cursor cursor = query.execute(mDb);
		try {
			return mFetchStrategy.fetchAll(cursor, query.getCursorInfo(), null);
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Query query, EntityConsumer<T> consumer) {
		Cursor cursor = query.execute(mDb);
		try {
			return mFetchStrategy.fetchAll(cursor, query.getCursorInfo(), consumer);
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Query query, FetchStrategy<T> strategy, EntityConsumer<T> consumer) {
		Cursor cursor = query.execute(mDb);
		try {
			return strategy.fetchAll(cursor, query.getCursorInfo(), consumer);
		} finally {
			cursor.close();
		}
	}

	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo) {
		return mFetchStrategy.fetchAll(cursor, cursorInfo, null);
	}

	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer) {
		return mFetchStrategy.fetchAll(cursor, cursorInfo, consumer);
	}

	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, FetchStrategy<T> strategy,
			EntityConsumer<T> consumer) {
		return strategy.fetchAll(cursor, cursorInfo, consumer);
	}

	public FetchStrategy<T> getFetchStrategy() {
		return mFetchStrategy;
	}

	public void setFetchStrategy(FetchStrategy<T> fetchStrategy) {
		mFetchStrategy = fetchStrategy;
	}

	protected SQLiteDatabase getDb() {
		return mDb;
	}

}
