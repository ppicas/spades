package cat.ppicas.spades.models.auto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;

public class CompanyDao extends Dao<CompanyAuto> {

	public static final Table TABLE = new TableBuilder("companies_auto", CompanyAuto.class)
			.columnId("id")
			.columnAuto("name").notNull().end()
			.columnAuto("fundation_year").notNull().end()
			.columnAuto("registration").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column FUNDATION_YEAR = TABLE.getColumn("fundation_year");
	public static final Column REGISTRATION = TABLE.getColumn("registration");

	public static final EntityMapper<CompanyAuto> MAPPER = new EntityMapper<CompanyAuto>(TABLE) {

		@Override
		protected CompanyAuto newInstance(Cursor cursor, int[][] mappings) {
			return new CompanyAuto();
		}

		@Override
		protected void mapCursorValues(CompanyAuto company, Cursor cursor, int[][] mappings,
				int tableIndex) {
			company.getMainBuilding().setKey(company.getEntityId());

			int[] buildingMappings = mappings[BuildingDao.TABLE.index];
			if (buildingMappings != null) {
				int isMainIndex = buildingMappings[BuildingDao.IS_MAIN.index];
				if (isMainIndex != -1 && cursor.getInt(isMainIndex) == 1) {
					company.getMainBuilding().fetch(cursor, mappings);
				}
			}
		}

		@Override
		protected void mapContentValues(CompanyAuto company, ContentValues values) {
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
