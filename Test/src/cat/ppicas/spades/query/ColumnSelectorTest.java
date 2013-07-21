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

package cat.ppicas.spades.query;

import java.util.List;

import cat.ppicas.spades.models.manual.BuildingDao;
import cat.ppicas.spades.models.manual.CompanyDao;
import cat.ppicas.spades.query.SelectedColumn.CountColumn;
import cat.ppicas.spades.query.SelectedColumn.RowIdColumn;
import junit.framework.TestCase;

public class ColumnSelectorTest extends TestCase {

	private ColumnSelector mSelector;

	@Override
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
		assertEquals(11, selected.size());
		assertEquals(CompanyDao.ID, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
		assertEquals(CompanyDao.FUNDATION_YEAR, selected.get(2).getColumn());
		assertEquals(CompanyDao.REGISTRATION, selected.get(3).getColumn());
		assertEquals(BuildingDao.ID, selected.get(4).getColumn());
		assertEquals(BuildingDao.COMPANY_ID, selected.get(5).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(6).getColumn());
		assertEquals(BuildingDao.PHONE, selected.get(7).getColumn());
		assertEquals(BuildingDao.FLOORS, selected.get(8).getColumn());
		assertEquals(BuildingDao.SURFACE, selected.get(9).getColumn());
		assertEquals(BuildingDao.IS_MAIN, selected.get(10).getColumn());
	}

	public void test__Should_get_selected_columns__When_one_table_added() throws Exception {
		// Given one table added.
		// And without any concrete column selected.
		mSelector.addTable(BuildingDao.TABLE);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then all table columns are returned.
		assertEquals(7, selected.size());
		assertEquals(BuildingDao.ID, selected.get(0).getColumn());
		assertEquals(BuildingDao.COMPANY_ID, selected.get(1).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(2).getColumn());
		assertEquals(BuildingDao.PHONE, selected.get(3).getColumn());
		assertEquals(BuildingDao.FLOORS, selected.get(4).getColumn());
		assertEquals(BuildingDao.SURFACE, selected.get(5).getColumn());
		assertEquals(BuildingDao.IS_MAIN, selected.get(6).getColumn());
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
		// Then the custom column is returned.
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
		// And the last one is a custom our custom column.
		assertTrue(selected.get(1).isCustom());
		assertEquals("%s + 1 AS custom", selected.get(1).getCustomExpression());
		assertEquals(1, selected.get(1).getCustomColumns().length);
	}

	public void test__Should_get_columns_for_count() throws Exception {
		// Given one table added.
		mSelector.addTable(CompanyDao.TABLE);
		// And on table column selected.
		mSelector.selectColumn("length(%s)", CompanyDao.NAME);
		// And one custom column selected.
		mSelector.selectColumn("length(%s)", CompanyDao.NAME);
		// When get the selected columns for count.
		List<SelectedColumn> selected = mSelector.getSelectedColumnsForCount();
		// Then first column is count.
		assertTrue(selected.get(0) instanceof CountColumn);
		// And the next column is the selected custom column.
		assertTrue(selected.get(1).isCustom());
		assertEquals("length(%s)", selected.get(1).getCustomExpression());
		assertEquals(CompanyDao.NAME, selected.get(1).getCustomColumns()[0]);
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
		// Then returns the selected columns plus the ID columns from first table.
		assertEquals(3, selected.size());
		assertEquals(CompanyDao.NAME, selected.get(0).getColumn());
		assertEquals(BuildingDao.ID, selected.get(1).getColumn());
		assertEquals(CompanyDao.ID, selected.get(2).getColumn());
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
		// Then only returns the selected columns.
		assertEquals(4, selected.size());
		assertEquals(CompanyDao.NAME, selected.get(0).getColumn());
		assertEquals(CompanyDao.ID, selected.get(1).getColumn());
		assertEquals(BuildingDao.ADDRESS, selected.get(2).getColumn());
		assertEquals(BuildingDao.ID, selected.get(3).getColumn());
	}

	public void test__Should_auto_add_rows_id__When_all_selected() throws Exception {
		// Given two tables added.
		// And no columns are selected.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then will return all tables columns plus a RowId.
		assertEquals(12, selected.size());
		// And the last column will be a RowId of ID column from first table.
		assertEquals(CompanyDao.ID, selected.get(11).getCustomColumns()[0]);
		assertTrue(selected.get(11) instanceof RowIdColumn);
	}

	public void test__Should_auto_add_rows_id__When_columns_selected() throws Exception {
		// Given two tables added.
		mSelector.addTable(CompanyDao.TABLE);
		mSelector.addTable(BuildingDao.TABLE);
		// And various columns selected.
		mSelector.selectColumn(CompanyDao.NAME);
		mSelector.selectColumn(BuildingDao.ADDRESS);
		mSelector.selectColumn("%s > 10", BuildingDao.FLOORS);
		// And auto rows ID is enabled.
		mSelector.setAutoRowsId(true);
		// When get the selected columns.
		List<SelectedColumn> selected = mSelector.getSelectedColumns();
		// Then will return all selected columns plus a RowId.
		assertEquals(4, selected.size());
		// And the last column will be a RowId of ID column from first table.
		assertEquals(CompanyDao.ID, selected.get(3).getCustomColumns()[0]);
		assertTrue(selected.get(3) instanceof RowIdColumn);
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
		// Then will return 5 columns.
		assertEquals(5, selected.size());
		// And the first columns will be the expected.
		assertEquals(CompanyDao.NAME, selected.get(0).getColumn());
		assertEquals(CompanyDao.NAME, selected.get(1).getColumn());
		assertEquals(CompanyDao.ID, selected.get(2).getColumn());
		assertEquals(BuildingDao.ID, selected.get(3).getColumn());
		// And the last column will be a RowId of ID column from first table.
		assertEquals(CompanyDao.ID, selected.get(4).getCustomColumns()[0]);
		assertTrue(selected.get(4) instanceof RowIdColumn);
	}

}
