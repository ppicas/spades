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

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;
import cat.picas.spades.fetch.HashMapFetchStrategy;
import cat.picas.spades.query.Query;
import cat.picas.spadessamples.model.Person.Gender;

public class PersonDao extends Dao<Person> {

	public static final Table TABLE = Tables.newTable("person", Person.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column SPOUSE_ID = TABLE
			.newColumnAuto("spouse_id", "spouse")
			.foreignKey(ID)
			.end();

	public static final Column NAME = TABLE
			.newColumnAuto("name", "mName")
			.notNull()
			.end();

	public static final Column BIRTH_DATE = TABLE
			.newColumnAuto("birth_date", "mBirthDate")
			.end();

	public static final Column GENDER = TABLE
			.newColumnAuto("gender", "mGender")
			.notNull()
			.defaultValue(Gender.MALE)
			.end();

	public static final Column HEIGHT = TABLE
			.newColumnAuto("height", "mHeight")
			.indexed(false, true)
			.end();

	public static final Column WEIGHT = TABLE
			.newColumnAuto("weight", "mWeight")
			.end();

	public static final AutoEntityMapper<Person> MAPPER = new AutoEntityMapper<Person>(TABLE);

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

}
