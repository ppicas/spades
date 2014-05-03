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

import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;

public class ContactPointDao extends Dao<ContactPoint> {

	public static final Table TABLE = Tables.newTable("contact_point", ContactPoint.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column PERSON_ID = TABLE
			.newColumnAuto("person_id", "person")
			.notNull()
			.foreignKey(PersonDao.ID)
			.end();

	public static final Column NAME = TABLE
			.newColumnAuto("name", "mName")
			.notNull()
			.end();

	public static final Column EMAIL = TABLE
			.newColumnAuto("email", "mEmail")
			.end();

	public static final Column PHONE = TABLE
			.newColumnAuto("phone", "mPhone")
			.end();

	public static final EntityMapper<ContactPoint> MAPPER = new AutoEntityMapper<ContactPoint>(TABLE);

	public ContactPointDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
