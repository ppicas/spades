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

package cat.picas.spadessamples.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;
import cat.picas.spades.fetch.HashMapFetchStrategy;
import cat.picas.spades.query.Query;
import cat.picas.spadessamples.model.Person.Gender;

public class PersonDao extends Dao<Person> {

	public static final Table TABLE = new TableBuilder("person", Person.class)
			.columnId("id")
			.columnAuto("spouse_id", "spouse").foreignKey("id").end()
			.columnAuto("name", "mName").notNull().end()
			.columnAuto("birth_date", "mBirthDate").end()
			.columnAuto("gender", "mGender").notNull().defaultValue(Gender.MALE).end()
			.columnAuto("height", "mHeight").indexed(false, true).end()
			.columnAuto("weight", "mWeight").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();

	public static final Column SPOUSE_ID = TABLE.getColumn("spouse_id");
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column BIRTH_DATE = TABLE.getColumn("birth_date");
	public static final Column GENDER = TABLE.getColumn("gender");
	public static final Column HEIGHT = TABLE.getColumn("height");
	public static final Column WEIGHT = TABLE.getColumn("weight");

	public static final EntityMapper<Person> MAPPER = new EntityMapper<Person>(TABLE) {
		@Override
		public Person newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new Person();
		}
	};

	private HashMapFetchStrategy<Person> mHashMapFetchStrategy;

	public PersonDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
		mHashMapFetchStrategy = new HashMapFetchStrategy<Person>(TABLE, MAPPER);
	}

	public List<Person> fetchAllWithRelated(Query query) {
		return fetchAll(query, mHashMapFetchStrategy, mFetchRelatedConsumer);
	}

	public List<Person> fetchAllWithRelated(Cursor cursor, CursorInfo cursorInfo) {
		return fetchAll(cursor, cursorInfo, mHashMapFetchStrategy, mFetchRelatedConsumer);
	}

	private final EntityConsumer<Person> mFetchRelatedConsumer = new EntityConsumer<Person>() {
		@Override
		public void accept(Cursor cursor, CursorInfo cursorInfo, Person person) {
			person.contactPoints.fetchAndAddOne(cursor, cursorInfo);
		}
	};

	public static class SpouseDao {

		public static final Table TABLE = PersonDao.TABLE.alias();

		public static final ColumnId ID = TABLE.getColumnId();

		public static final Column NAME = TABLE.getColumn(PersonDao.NAME.name);

		public static final EntityMapper<Person> MAPPER = new EntityMapper<Person>(TABLE) {
			@Override
			public Person newInstance(Cursor cursor, CursorInfo cursorInfo) {
				return new Person();
			}
		};
	}

}
