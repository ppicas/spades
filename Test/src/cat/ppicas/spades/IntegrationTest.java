package cat.ppicas.spades;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import cat.ppicas.spades.models.Building;
import cat.ppicas.spades.models.BuildingDao;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.CompanyDao;
import cat.ppicas.spades.models.OpenHelper;
import cat.ppicas.spades.query.Query;

public class IntegrationTest extends AndroidTestCase {

	private SQLiteDatabase mDb;
	private CompanyDao mCompanyDao;
	private BuildingDao mBuildingDao;
	private Company mCompany;
	private long mCompanyId;
	private Building mBuildingA;
	private long mBuildingAId;
	private Building mBuildingB;
	private long mBuildingBId;

	protected void setUp() throws Exception {
		super.setUp();

		OpenHelper helper = new OpenHelper(getContext());
		mDb = helper.getWritableDatabase();
		mCompanyDao = new CompanyDao(mDb);
		mBuildingDao = new BuildingDao(mDb);

		mCompany = new Company();
		mCompany.setName("Google");
		mCompany.setFundationYear(1998);
		mCompany.setRegistration(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime());

		mCompanyId = mCompanyDao.insert(mCompany);

		mBuildingA = new Building();
		mBuildingA.setMain(false);
		mBuildingA.getCompany().setKey(mCompany.getEntityId());
		mBuildingA.setAddress("76 Ninth Avenue, New York, NY 10011");
		mBuildingA.setPhone("+1 212-565-0000");
		mBuildingA.setFloors(4);
		mBuildingA.setSurface(256.64);

		mBuildingAId = mBuildingDao.insert(mBuildingA);

		mBuildingB = new Building();
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
	}

	public void test__Simple_query() throws Exception {
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		List<Company> companies = mCompanyDao.fetchAll(query);

		assertEquals(1, companies.size());
	}

	public void test__Simple_query_cursor() throws Exception {
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		Cursor cursor = query.execute(mDb);
		int[][] mappings = query.getMappings();
		List<Company> companies = mCompanyDao.fetchAll(cursor, false, mappings);

		assertEquals(1, companies.size());
		Company company = companies.get(0);
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Query_with_column_selected_and_auto_entities_id() throws Exception {
		Company company = mCompanyDao.fetchFirst(new Query(CompanyDao.TABLE)
				.select(CompanyDao.FUNDATION_YEAR));
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Query_with_column_selected_and_no_auto_entities_id() throws Exception {
		Company company = mCompanyDao.fetchFirst(new Query(CompanyDao.TABLE)
				.setAutoEntitiesId(false)
				.select(CompanyDao.FUNDATION_YEAR));
		assertEquals(0, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Query_with_left_join() throws Exception {
		Query query = new Query(BuildingDao.TABLE)
				.leftJoin(CompanyDao.TABLE, "%s = %s", BuildingDao.COMPANY_ID, CompanyDao.ID)
				.where(BuildingDao.ID, "=" + mBuildingAId);

		Cursor cursor = query.execute(mDb);
		int[][] mappings = query.getMappings();
		List<Building> buildings = mBuildingDao.fetchAll(cursor, false, mappings);
		List<Company> companies = mCompanyDao.fetchAll(cursor, false, mappings);
		cursor.close();

		assertEquals(1, buildings.size());
		assertEquals(1, companies.size());
	}

	public void test__Related_fetch_from_many_to_one() throws Exception {
		Company company = mBuildingA.getCompany().fetch(mDb);
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Related_fetch_from_inverse_side() throws Exception {
		Company company = mCompanyDao.get(mCompanyId);
		Building building = company.getBuilding().fetch(mDb);
		assertEquals(mBuildingBId, building.getEntityId());
		assertEquals(mCompanyId, building.getCompany().getKey());
		assertEquals("1600 Amphitheatre Parkway, Mountain View, CA 94043", building.getAddress());
		assertEquals("+1 650-253-0000", building.getPhone());
		assertEquals(8, building.getFloors());
		assertEquals(1024.512, building.getSurface());
		assertTrue(building.isMain());
	}
}
