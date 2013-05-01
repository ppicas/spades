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

	public void test__Should_get_selected_columns__When_invalid_column_selected() throws Exception {
		try {
			// Given one table added.
			mSelector.addTable(CompanyDao.TABLE);
			// When one column from other table is selected.
			mSelector.selectColumn(BuildingDao.SURFACE);
			fail();
		} catch (IllegalArgumentException e) {
			// Then should throw an exception.
		}
	}

	public void test__Should_get_custom_columns__When_only_one_custom() throws Exception {
		// Given one table added.
		mSelector.addTable(BuildingDao.TABLE);
		// And add a custom column.
		mSelector.selectColumn("%s + 1 AS custom", BuildingDao.FLOORS);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then the two selected columns are returned.
		assertEquals(1, selected.size());
		assertTrue(selected.get(0).isCustom());
		assertEquals("%s + 1 AS custom", selected.get(0).getCustomExpression());
		assertEquals(1, selected.get(0).getCustomColumns().length);
	}

	public void test__Should_get_custom_columns__When_combined_custom_and_normal() throws Exception {
		// Given one table added.
		mSelector.addTable(BuildingDao.TABLE);
		// And one column from first table is selected.
		mSelector.selectColumn(BuildingDao.PHONE);
		// And add a custom column.
		mSelector.selectColumn("%s + 1 AS custom", BuildingDao.FLOORS);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then the two selected columns are returned.
		assertEquals(2, selected.size());
		assertTrue(selected.get(1).isCustom());
		assertEquals("%s + 1 AS custom", selected.get(1).getCustomExpression());
		assertEquals(1, selected.get(1).getCustomColumns().length);
	}

	public void test__Should_auto_add_entities_ids__When_one_is_necessary() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And one no ID column selected from first table.
		mSelector.selectColumn(CompanyDao.NAME);
		// And ID column selected from second table.
		mSelector.selectColumn(BuildingDao.ID);
		// And auto entities ID enabled.
		mSelector.setAutoEntitiesId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then 3 columns are returned.
		assertEquals(3, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
		assertEquals(BuildingDao.ID, selected.get(2).getColumn());
	}

	public void test__Should_auto_add_entities_ids__When_has_custom_columns() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And add a custom column.
		mSelector.selectColumn("%s + 1 AS custom", BuildingDao.FLOORS);
		// And auto entities ID enabled.
		mSelector.setAutoEntitiesId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then 3 columns are returned.
		assertEquals(3, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(BuildingDao.ID, selected.get(1).getColumn());
		assertTrue(selected.get(2).isCustom());
	}

	public void test__Should_auto_add_entities_ids__When_is_not_necessary() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And one no ID column selected from first table.
		mSelector.selectColumn(CompanyDao.NAME);
		mSelector.selectColumn(CompanyDao.ID);
		// And ID column selected from second table.
		mSelector.selectColumn(BuildingDao.ADDRESS);
		mSelector.selectColumn(BuildingDao.ID);
		// And auto entities ID enabled.
		mSelector.setAutoEntitiesId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then 3 columns are returned.
		assertEquals(4, selected.size());
		assertEquals(CompanyDao.NAME, selected.get(0).getColumn());
		assertEquals(CompanyDao.ID, selected.get(1).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(2).getColumn());
		assertEquals(BuildingDao.ID, selected.get(3).getColumn());
	}

	public void test__Should_auto_add_rows_id__When_first_column_not_row_id() throws Exception {
		// Given one table added.
		mSelector.addTable(CompanyDao.TABLE);
		// And first column is not row id.
		mSelector.selectColumn(CompanyDao.NAME);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then extra columns ID will be added.
		assertEquals(2, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
	}

	public void test__Should_auto_add_rows_id__When_first_column_id_but_not_row_id() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And first column is an ID but first table ID.
		mSelector.selectColumn(BuildingDao.ID);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then extra columns ID will be added.
		assertEquals(2, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(BuildingDao.ID, selected.get(1).getColumn());
	}

	public void test__Should_auto_add_rows_id__When_is_not_necessary() throws Exception {
		// Given two tables added.
		// And first column is row id because no columns are selected.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then no extra columns will be added.
		assertEquals(10, selected.size());
	}

	public void test__Should_auto_add_rows_id__When_auto_entity_id_was_added() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And is selected one no ID column from first table.
		mSelector.selectColumn(CompanyDao.NAME);
		// And is selected one no ID column from second table.
		mSelector.selectColumn(CompanyDao.NAME);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// And auto entities ID enabled.
		mSelector.setAutoEntitiesId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then only extra columns of auto add entities will be added.
		assertEquals(4, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(BuildingDao.ID, selected.get(1).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(2).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(3).getColumn());
	}

}
