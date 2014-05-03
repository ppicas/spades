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

package cat.picas.spades.models.manual;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;
import cat.picas.spades.models.auto.CompanyDao;

public class BuildingDao extends Dao<BuildingManual> {

	public static final Table TABLE = Tables.newTable("buildings_manual", BuildingManual.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column COMPANY_ID = TABLE.newColumnInteger("company_id")
			.notNull().foreignKey(CompanyDao.ID).end();

	public static final Column ADDRESS = TABLE.newColumnText("address")
			.notNull().defaultValue("").end();

	public static final Column PHONE = TABLE.newColumnText("phone").end();

	public static final Column FLOORS = TABLE.newColumnInteger("floors")
			.notNull().defaultValue(0).end();

	public static final Column SURFACE = TABLE.newColumnReal("surface")
			.notNull().defaultValue(0).end();

	public static final Column IS_MAIN = TABLE.newColumnInteger("is_main")
			.notNull().defaultValue(false).end();

	public static final EntityMapper<BuildingManual> MAPPER = new EntityMapper<BuildingManual>(TABLE) {

		@Override
		public BuildingManual newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new BuildingManual();
		}

		@Override
		public void mapCursorValues(BuildingManual building, Cursor cursor, CursorInfo cursorInfo) {
			int index;

			index = cursorInfo.getColumnIndex(COMPANY_ID);
			if (index != CursorInfo.INVALID_INDEX) {
				building.getCompany().setValue(cursor.getLong(index));
			}
			index = cursorInfo.getColumnIndex(ADDRESS);
			if (index != CursorInfo.INVALID_INDEX) {
				building.setAddress(cursor.getString(index));
			}
			index = cursorInfo.getColumnIndex(PHONE);
			if (index != CursorInfo.INVALID_INDEX) {
				building.setPhone(cursor.isNull(index) ? null : cursor.getString(index));
			}
			index = cursorInfo.getColumnIndex(FLOORS);
			if (index != CursorInfo.INVALID_INDEX) {
				building.setFloors(cursor.getInt(index));
			}
			index = cursorInfo.getColumnIndex(SURFACE);
			if (index != CursorInfo.INVALID_INDEX) {
				building.setSurface(cursor.getDouble(index));
			}
			index = cursorInfo.getColumnIndex(IS_MAIN);
			if (index != CursorInfo.INVALID_INDEX) {
				building.setMain(cursor.getInt(index) == 1);
			}

			building.getCompany().fetch(cursor, cursorInfo);
		}

		@Override
		public void mapContentValues(BuildingManual building, ContentValues values) {
			values.put(COMPANY_ID.name, building.getCompany().getValue());
			if (building.getAddress() != null) {
				values.put(ADDRESS.name, building.getAddress());
			}
			values.put(PHONE.name, building.getPhone());
			values.put(FLOORS.name, building.getFloors());
			values.put(SURFACE.name, building.getSurface());
			values.put(IS_MAIN.name, building.isMain());
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
