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

import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

public class ContactPointDao extends Dao<ContactPoint> {

	public static final Table TABLE = new TableBuilder("contact_point", ContactPoint.class)
			.columnId("id")
			.columnAuto("person_id").notNull().foreignKey(PersonDao.ID).end()
			.columnAuto("name").notNull().end()
			.columnAuto("email").end()
			.columnAuto("phone").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();

	public static final Column PERSON_ID = TABLE.getColumn("person_id");
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column EMAIL = TABLE.getColumn("email");
	public static final Column PHONE = TABLE.getColumn("phone");

	public static final EntityMapper<ContactPoint> MAPPER = new EntityMapper<ContactPoint>(TABLE) {
		@Override
		protected ContactPoint newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new ContactPoint();
		}
	};

	public ContactPointDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
