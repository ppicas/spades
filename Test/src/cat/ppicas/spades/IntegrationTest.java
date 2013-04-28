package cat.ppicas.spades;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.CompanyDao;
import cat.ppicas.spades.models.OpenHelper;

public class IntegrationTest extends AndroidTestCase {

	private SQLiteDatabase mDb;
	private CompanyDao mCompanyDao;
	private Company mCompany;

	protected void setUp() throws Exception {
		super.setUp();

		OpenHelper helper = new OpenHelper(getContext());
		mDb = helper.getWritableDatabase();
		mCompanyDao = new CompanyDao(mDb);

		mCompany = new Company();
		mCompany.setName("Google");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mDb.close();
	}

	public void testInsertReturnAndSetsEntityId() throws Exception {
		long id = mCompanyDao.insert(mCompany);
		assertTrue(id > 0);
		assertEquals(id,  mCompany.getEntityId());
	}

	public void testGet() throws Exception {
		long id = mCompanyDao.insert(mCompany);
		Company company = mCompanyDao.get(id);
		assertEquals(id, company.getEntityId());
		assertEquals("Google", company.getName());
	}

}
