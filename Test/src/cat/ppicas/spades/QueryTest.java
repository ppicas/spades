package cat.ppicas.spades;

import java.util.ArrayList;

import junit.framework.TestCase;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.models.CompanyDao;

public class QueryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAutoIds() throws Exception {
		Query query = new Query(CompanyDao.TABLE);
		ArrayList<ColumnId> list = query.getAutoColumnIdList();
		assertTrue(list.isEmpty());
	}

	public void testAutoIdsSkipRedundatFirstField() throws Exception {
		Query query = new Query(CompanyDao.TABLE);
		query.select(CompanyDao.NAME);
		ArrayList<ColumnId> list = query.getAutoColumnIdList();
		assertTrue(list.isEmpty());
	}

}
