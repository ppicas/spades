package cat.ppicas.spades.query;

import java.util.List;

import cat.ppicas.spades.models.BuildingDao;
import cat.ppicas.spades.models.CompanyDao;
import junit.framework.TestCase;

public class ColumnSelectorTest extends TestCase {

	private ColumnSelector mSelector;

	protected void setUp() throws Exception {
		super.setUp();

		mSelector = new ColumnSelector();
	}

	public void test__Should_get_selected_columns__When_two_tables_added() throws Exception {
		// Given two tables added.
		// And without any concrete column selected.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then all both table columns are returned in order of aggregation.
		assertEquals(10, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
		assertEquals(CompanyDao.FUNDATION_YEAR, selected.get(2).getColumn());
		assertEquals(CompanyDao.REGISTRATION, selected.get(3).getColumn());
		assertEquals(BuildingDao.ID, selected.get(4).getColumn());
		assertEquals(BuildingDao.COMPANY_ID, selected.get(5).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(6).getColumn());
	}

	public void test__Should_get_selected_columns__When_one_table_added() throws Exception {
		// Given one table added.
		// And without any concrete column selected.
		mSelector.addTable(BuildingDao.TABLE);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then all table columns are returned.
		assertEquals(6, selected.size());
		assertEquals(BuildingDao.ID, selected.get(0).getColumn());
		assertEquals(BuildingDao.COMPANY_ID, selected.get(1).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(2).getColumn());
		assertEquals(BuildingDao.PHONE, selected.get(3).getColumn());
		assertEquals(BuildingDao.FLOORS, selected.get(4).getColumn());
		assertEquals(BuildingDao.SURFACE, selected.get(5).getColumn());
	}

	public void test__Should_get_selected_columns__When_one_table_added_with_columns_selected() throws Exception {
		// Given one table added.
		mSelector.addTable(CompanyDao.TABLE);
		// And two table columns selected.
		mSelector.selectColumn(CompanyDao.NAME);
		mSelector.selectColumn(CompanyDao.REGISTRATION);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then the two selected columns are returned.
		assertEquals(2, selected.size());
		assertEquals(CompanyDao.NAME, selected.get(0).getColumn());
		assertEquals(CompanyDao.REGISTRATION, selected.get(1).getColumn());
	}

	public void test__Auto_add_table_ids() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And one no ID column selected from first table.
		mSelector.selectColumn(CompanyDao.NAME);
		// And ID column selected from second table.
		mSelector.selectColumn(BuildingDao.ID);
		// And auto tables ID enabled
		mSelector.setAutoTablesId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then the two selected columns are returned.
		assertEquals(3, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
		assertEquals(BuildingDao.ID, selected.get(2).getColumn());
	}

}
