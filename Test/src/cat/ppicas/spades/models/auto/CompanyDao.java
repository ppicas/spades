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

package cat.ppicas.spades.models.auto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;

public class CompanyDao extends Dao<CompanyAuto> {

	public static final Table TABLE = new TableBuilder("companies_auto", CompanyAuto.class)
			.columnId("id")
			.columnAuto("name").notNull().end()
			.columnAuto("fundation_year").notNull().end()
			.columnAuto("registration").end()
			.columnAuto("size").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column FUNDATION_YEAR = TABLE.getColumn("fundation_year");
	public static final Column REGISTRATION = TABLE.getColumn("registration");
	public static final Column SIZE = TABLE.getColumn("size");

	public static final EntityMapper<CompanyAuto> MAPPER = new EntityMapper<CompanyAuto>(TABLE) {

		@Override
		protected CompanyAuto newInstance(Cursor cursor, int[][] mappings) {
			return new CompanyAuto();
		}

		@Override
		protected void mapCursorValues(CompanyAuto company, Cursor cursor, int[][] mappings,
				int tableIndex) {
			company.getMainBuilding().setKey(company.getEntityId());

			int[] buildingMappings = mappings[BuildingDao.TABLE.index];
			if (buildingMappings != null) {
				int isMainIndex = buildingMappings[BuildingDao.IS_MAIN.index];
				if (isMainIndex != -1 && cursor.getInt(isMainIndex) == 1) {
					company.getMainBuilding().fetch(cursor, mappings);
				}
			}
		}

		@Override
		protected void mapContentValues(CompanyAuto company, ContentValues values) {
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
