package cat.ppicas.spades;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import cat.ppicas.spades.models.OpenHelper;
import cat.ppicas.spades.models.auto.BuildingAuto;
import cat.ppicas.spades.models.auto.BuildingDao;
import cat.ppicas.spades.models.auto.CompanyAuto;
import cat.ppicas.spades.models.auto.CompanyDao;
import cat.ppicas.spades.query.Query;

public class AutoModelsIntegrationTest extends AndroidTestCase {

	private SQLiteDatabase mDb;
	private CompanyDao mCompanyDao;
	private BuildingDao mBuildingDao;
	private CompanyAuto mCompany;
	private long mCompanyId;
	private BuildingAuto mBuildingA;
	private long mBuildingAId;
	private BuildingAuto mBuildingB;
	private long mBuildingBId;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		OpenHelper helper = new OpenHelper(getContext());
		mDb = helper.getWritableDatabase();
		mCompanyDao = new CompanyDao(mDb);
		mBuildingDao = new BuildingDao(mDb);

		mCompany = new CompanyAuto();
		mCompany.setName("Google");
		mCompany.setFundationYear(1998);
		mCompany.setRegistration(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime());

		mCompanyId = mCompanyDao.insert(mCompany);

		mBuildingA = new BuildingAuto();
		mBuildingA.setMain(false);
		mBuildingA.getCompany().setKey(mCompany.getEntityId());
		mBuildingA.setAddress("76 Ninth Avenue, New York, NY 10011");
		mBuildingA.setPhone("+1 212-565-0000");
		mBuildingA.setFloors(4);
		mBuildingA.setSurface(256.64);

		mBuildingAId = mBuildingDao.insert(mBuildingA);

		mBuildingB = new BuildingAuto();
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
		CompanyAuto company = mCompanyDao.get(mCompanyId);

		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Simple_query() throws Exception {
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		List<CompanyAuto> companies = mCompanyDao.fetchAll(query);

		assertEquals(1, companies.size());
	}

	public void test__Simple_query_cursor() throws Exception {
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		Cursor cursor = query.execute(mDb);
		int[][] mappings = query.getMappings();
		List<CompanyAuto> companies = mCompanyDao.fetchAll(cursor, mappings);
		cursor.close();

		assertEquals(1, companies.size());
		CompanyAuto company = companies.get(0);
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Query_with_column_selected_and_auto_entities_id() throws Exception {
		CompanyAuto company = mCompanyDao.fetchFirst(new Query(CompanyDao.TABLE)
				.select(CompanyDao.FUNDATION_YEAR));
		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Query_with_column_selected_and_no_auto_entities_id() throws Exception {
		CompanyAuto company = mCompanyDao.fetchFirst(new Query(CompanyDao.TABLE)
				.setAutoEntitiesId(false)
				.select(CompanyDao.FUNDATION_YEAR));
		assertEquals(0, company.getEntityId());
		assertEquals("", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(null, company.getRegistration());
	}

	public void test__Many_to_one_related_fetched_manually() throws Exception {
		Query query = new Query(BuildingDao.TABLE)
				.leftJoin(CompanyDao.TABLE, "%s = %s", BuildingDao.COMPANY_ID, CompanyDao.ID)
				.where(BuildingDao.ID, "=" + mBuildingAId);

		Cursor cursor = query.execute(mDb);
		int[][] mappings = query.getMappings();
		List<BuildingAuto> buildings = mBuildingDao.fetchAll(cursor, mappings);
		List<CompanyAuto> companies = mCompanyDao.fetchAll(cursor, mappings);
		cursor.close();

		assertEquals(1, buildings.size());
		assertEquals(1, companies.size());
	}

	public void test__Many_to_one_related_fetched_automatically() throws Exception {
		Query query = new Query(BuildingDao.TABLE)
				.leftJoin(CompanyDao.TABLE, "%s = %s", BuildingDao.COMPANY_ID, CompanyDao.ID)
				.where(BuildingDao.ID, "=" + mBuildingAId);
		List<BuildingAuto> buildings = mBuildingDao.fetchAll(query);

		assertEquals(1, buildings.size());
		CompanyAuto company = buildings.get(0).getCompany().get();
		assertEquals(mCompanyId, company.getEntityId());
	}

	public void test__Many_to_one_related_fetched_from_entity() throws Exception {
		CompanyAuto company = mBuildingA.getCompany().fetch(mDb);

		assertEquals(mCompanyId, company.getEntityId());
		assertEquals("Google", company.getName());
		assertEquals(1998, company.getFundationYear());
		assertEquals(new GregorianCalendar(1998, Calendar.SEPTEMBER, 4).getTime(),
				company.getRegistration());
	}

	public void test__Many_to_one_related_fetched_from_inverse_entity() throws Exception {
		CompanyAuto company = mCompanyDao.get(mCompanyId);
		BuildingAuto building = company.getMainBuilding().fetch(mDb);

		assertEquals(mBuildingBId, building.getEntityId());
		assertEquals(mCompanyId, (long) building.getCompany().getKey());
		assertEquals("1600 Amphitheatre Parkway, Mountain View, CA 94043", building.getAddress());
		assertEquals("+1 650-253-0000", building.getPhone());
		assertEquals(8, building.getFloors());
		assertEquals(1024.512, building.getSurface());
		assertTrue(building.isMain());
	}

}
