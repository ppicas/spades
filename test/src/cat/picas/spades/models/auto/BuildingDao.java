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

package cat.picas.spades.models.auto;

import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

public class BuildingDao extends Dao<BuildingAuto> {

	public static final Table TABLE = new TableBuilder("buildings_auto", BuildingAuto.class)
			.columnId("id")
			.columnAuto("company_id", "mCompany").notNull().foreignKey(CompanyDao.ID).end()
			.columnAuto("address", "mAddress").notNull().defaultValue("").end()
			.columnAuto("phone", "mPhone").end()
			.columnAuto("floors", "mFloors").notNull().defaultValue(0).end()
			.columnAuto("surface", "mSurface").notNull().defaultValue(0).end()
			.columnAuto("is_main", "mMain").notNull().defaultValue(false).end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column COMPANY_ID = TABLE.getColumn("company_id");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column PHONE = TABLE.getColumn("phone");
	public static final Column FLOORS = TABLE.getColumn("floors");
	public static final Column SURFACE = TABLE.getColumn("surface");
	public static final Column IS_MAIN = TABLE.getColumn("is_main");

	public static final EntityMapper<BuildingAuto> MAPPER = new AutoEntityMapper<BuildingAuto>(TABLE);

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
