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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import cat.ppicas.spades.models.Building;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.Company.CompanySize;
import cat.ppicas.spades.models.OpenHelper;
import cat.ppicas.spades.query.Query;

public abstract class IntegrationBaseTest extends AndroidTestCase {

	private SQLiteDatabase mDb;

	private Dao<Company> mCompanyDao;
	private Dao<Building> mBuildingDao;
	private TablesHolder mTables;

	private Company mCompany;
	private long mCompanyId;
	private Building mBuildingA;
	private long mBuildingAId;
	private Building mBuildingB;
	private long mBuildingBId;

	@Override
	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception {
		super.setUp();

		OpenHelper helper = new OpenHelper(getContext());
		mDb = helper.getWritableDatabase();

		mCompanyDao = (Dao<Company>) newCompanyDao(mDb);
		mBuildingDao = (Dao<Building>) newBuildingDao(mDb);
		mTables = getTablesHolder();

		mCompany = newCompany();
		mCompany.setName("Google");
		mCompany.setFundationYear(1998);
		mCompany.setRegistration(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime());
		mCompany.setSize(CompanySize.LARGE);

		mCompanyId = mCompanyDao.insert(mCompany);

		mBuildingA = newBuilding();
		mBuildingA.setMain(false);
		mBuildingA.getCompany().setKey(mCompany.getEntityId());
		mBuildingA.setAddress("76 Ninth Avenue, New York, NY 10011");
		mBuildingA.setPhone("+1 212-565-0000");
		mBuildingA.setFloors(4);
		mBuildingA.setSurface(256.64);

		mBuildingAId = mBuildingDao.insert(mBuildingA);

		mBuildingB = newBuilding();
		mBuildingB.setMain(true);
		mBuildingB.getCompany().setKey(mCompany.getEntityId());
		mBuildingB.setAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043");
		mBuildingB.setPhone("+1 650-253-0000");
		mBuildingB.setFloors(8);
		mBuildingB.setSurface(1024.512);

		mBuildingBId = mBuildingDao.insert(mBuildingB);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mDb.close();
	}

	public void test__Insert_return_and_sets_entity_id() throws Exception {
		long id = mCompanyDao.insert(mCompany);
		assertTrue(id > 0);
		assertEquals(id,  mCompany.getEntityId());
	}

	public void test__Get_by_id() throws Exception {
		Company company = mCompanyDao.get(mCompanyId);

		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
		assertEquals(CompanySize.LARGE, company.getSize());
	}

	public void test__Simple_query() throws Exception {
		Query query = new Query(mTables.companyTable).where(mTables.companyName, "=?").params("Google");
		List<Company> companies = mCompanyDao.fetchAll(query);

		assertEquals(1, companies.size());
	}

	public void test__Simple_query_cursor() throws Exception {
		Query query = new Query(mTables.companyTable).where(mTables.companyName, "=?").params("Google");
		Cursor cursor = query.execute(mDb);
		CursorInfo cursorInfo = query.getCursorInfo();
		List<Company> companies = mCompanyDao.fetchAll(cursor, cursorInfo);
		cursor.close();

		assertEquals(1, companies.size());
		Company company = companies.get(0);
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Query_with_column_selected_and_auto_entities_id() throws Exception {
		Company company = mCompanyDao.fetchFirst(new Query(mTables.companyTable)
				.select(mTables.companyFundationYear));
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Query_with_column_selected_and_no_auto_entities_id() throws Exception {
		Company company = mCompanyDao.fetchFirst(new Query(mTables.companyTable)
				.setAutoEntitiesId(false)
				.select(mTables.companyFundationYear));
		assertEquals(0, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Many_to_one_related_fetched_manually() throws Exception {
		Query query = new Query(mTables.buildingTable)
				.leftJoin(mTables.companyTable, "%s = %s", mTables.buildingCompanyId, mTables.companyId)
				.where(mTables.buildingId, "=" + mBuildingAId);

		Cursor cursor = query.execute(mDb);
		CursorInfo cursorInfo = query.getCursorInfo();
		List<Building> buildings = mBuildingDao.fetchAll(cursor, cursorInfo);
		List<Company> companies = mCompanyDao.fetchAll(cursor, cursorInfo);
		cursor.close();

		assertEquals(1, buildings.size());
		assertEquals(1, companies.size());
	}

	public void test__Many_to_one_related_fetched_automatically() throws Exception {
		Query query = new Query(mTables.buildingTable)
				.leftJoin(mTables.companyTable, "%s = %s", mTables.buildingCompanyId, mTables.companyId)
				.where(mTables.buildingId, "=" + mBuildingAId);
		List<Building> buildings = mBuildingDao.fetchAll(query);

		assertEquals(1, buildings.size());
		Company company = buildings.get(0).getCompany().get();
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Many_to_one_related_fetched_from_entity() throws Exception {
		Company company = mBuildingA.getCompany().fetch(mDb);

		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Many_to_one_related_fetched_from_inverse_entity() throws Exception {
		Company company = mCompanyDao.get(mCompanyId);
		Building building = company.getMainBuilding().fetch(mDb);

		assertEquals(mBuildingBId, building.getEntityId());
		assertEquals(mCompanyId, (long) building.getCompany().getKey());
		assertEquals("1600 Amphitheatre Parkway, Mountain View, CA 94043", building.getAddress());
		assertEquals("+1 650-253-0000", building.getPhone());
		assertEquals(8, building.getFloors());
		assertEquals(1024.512, building.getSurface());
		assertTrue(building.isMain());
	}

	protected abstract Dao<? extends Company> newCompanyDao(SQLiteDatabase db);

	protected abstract Dao<? extends Building> newBuildingDao(SQLiteDatabase db);

	protected abstract Company newCompany();

	protected abstract Building newBuilding();

	protected abstract TablesHolder getTablesHolder();

	protected static class TablesHolder {

		public Table buildingTable;

		public Column buildingId;
		public Column buildingCompanyId;
		public Column buildingAddress;
		public Column buildingPhone;
		public Column buildingFloors;
		public Column buildingSurface;
		public Column buildingIsMain;

		public Table companyTable;

		public Column companyId;
		public Column companyName;
		public Column companyFundationYear;
		public Column companyRegistration;

	}

}
