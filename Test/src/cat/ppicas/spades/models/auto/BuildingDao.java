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

public class BuildingDao extends Dao<BuildingAuto> {

	public static final Table TABLE = new TableBuilder("buildings_auto", BuildingAuto.class)
			.columnId("id")
			.columnAuto("company_id").notNull().foreignKey(CompanyDao.ID).end()
			.columnAuto("address").notNull().defaultValue("").end()
			.columnAuto("phone").end()
			.columnAuto("floors").notNull().defaultValue(0).end()
			.columnAuto("surface").notNull().defaultValue(0).end()
			.columnAuto("is_main").notNull().defaultValue(false).end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column COMPANY_ID = TABLE.getColumn("company_id");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column PHONE = TABLE.getColumn("phone");
	public static final Column FLOORS = TABLE.getColumn("floors");
	public static final Column SURFACE = TABLE.getColumn("surface");
	public static final Column IS_MAIN = TABLE.getColumn("is_main");

	public static final EntityMapper<BuildingAuto> MAPPER = new EntityMapper<BuildingAuto>(TABLE) {

		@Override
		protected BuildingAuto newInstance(Cursor cursor, int[][] mappings) {
			return new BuildingAuto();
		}

		@Override
		protected void mapCursorValues(BuildingAuto building, Cursor cursor, int[][] mappings,
				int tableIndex) {
			building.getCompany().fetch(cursor, mappings);
		}

		@Override
		protected void mapContentValues(BuildingAuto building, ContentValues values) {
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
