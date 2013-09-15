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

package cat.ppicas.spades;

import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.models.Building;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.auto.BuildingAuto;
import cat.ppicas.spades.models.auto.BuildingDao;
import cat.ppicas.spades.models.auto.CompanyAuto;
import cat.ppicas.spades.models.auto.CompanyDao;

public class AutoModelsIntegrationTest extends IntegrationBaseTest {

	@Override
	protected Dao<? extends Company> newCompanyDao(SQLiteDatabase db) {
		return new CompanyDao(db);
	}

	@Override
	protected Dao<? extends Building> newBuildingDao(SQLiteDatabase db) {
		return new BuildingDao(db);
	}

	@Override
	protected Company newCompany() {
		return new CompanyAuto();
	}

	@Override
	protected Building newBuilding() {
		return new BuildingAuto();
	}

	@Override
	protected TablesHolder getTablesHolder() {
		TablesHolder holder = new TablesHolder();

		holder.buildingTable = BuildingDao.TABLE;

		holder.buildingId = BuildingDao.ID;
		holder.buildingCompanyId = BuildingDao.COMPANY_ID;
		holder.buildingAddress = BuildingDao.ADDRESS;
		holder.buildingPhone = BuildingDao.PHONE;
		holder.buildingFloors = BuildingDao.FLOORS;
		holder.buildingSurface = BuildingDao.SURFACE;
		holder.buildingIsMain = BuildingDao.IS_MAIN;

		holder.companyTable = CompanyDao.TABLE;

		holder.companyId = CompanyDao.ID;
		holder.companyName = CompanyDao.NAME;
		holder.companyFundationYear = CompanyDao.FUNDATION_YEAR;
		holder.companyRegistration = CompanyDao.REGISTRATION;

		return holder;
	}

}
