package cat.ppicas.spades;

import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.CompanyDao;
import cat.ppicas.spades.models.OpenHelper;
import cat.ppicas.spades.query.Query;

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

	public void testGetById() throws Exception {
		long id = mCompanyDao.insert(mCompany);
		Company company = mCompanyDao.get(id);
		assertEquals(id, company.getEntityId());
		assertEquals("Google", company.getName());
	}

	public void testSimpleQuery() throws Exception {
		mCompanyDao.insert(mCompany);
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		List<Company> companies = mCompanyDao.fetchAll(query);
		assertEquals(1, companies.size());
	}

	public void testSimpleQueryCursor() throws Exception {
		mCompanyDao.insert(mCompany);
		Query query = new Query(CompanyDao.TABLE).where(CompanyDao.NAME, "=?").params("Google");
		Cursor cursor = query.execute(mDb);
		System.out.println(Arrays.toString(cursor.getColumnNames()));
		int[][] mappings = query.getMappings();
		List<Company> companies = mCompanyDao.fetchAll(cursor, false, mappings);
		assertEquals(1, companies.size());
	}

}
