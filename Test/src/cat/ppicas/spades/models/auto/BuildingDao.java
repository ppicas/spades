package cat.ppicas.spades.models.auto;

import static cat.ppicas.spades.ColumnBuilder.DEFAULT_EMTPY;
import static cat.ppicas.spades.ColumnBuilder.DEFAULT_ZERO;
import static cat.ppicas.spades.ColumnBuilder.DEFAULT_FALSE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;

public class BuildingDao extends Dao<Building> {

	public static final Table<Building> TABLE = new Table<Building>("buildings", Building.class);

	public static final Column ID = TABLE.columnId();

	public static final Column COMPANY_ID = TABLE.column().auto("mCompany").notNull().foreignKey(CompanyDao.ID).end();
	public static final Column ADDRESS = TABLE.column().auto("mAddress").notNull(DEFAULT_EMTPY).end();
	public static final Column PHONE = TABLE.column().auto("mPhone").end();
	public static final Column FLOORS = TABLE.column().auto("mFloors").notNull(DEFAULT_ZERO).end();
	public static final Column SURFACE = TABLE.column().auto("mSurface").notNull(DEFAULT_ZERO).end();
	public static final Column IS_MAIN = TABLE.column().auto("mMain").notNull(DEFAULT_FALSE).end();

	public static final EntityMapper<Building> MAPPER = new EntityMapper<Building>(TABLE) {

		@Override
		protected Building newInstance(Cursor cursor, int[] mappings) {
			return new Building();
		}

		@Override
		protected void mapCursorValues(Building building, Cursor cursor, int[] maps) {
		}

		@Override
		protected void mapContentValues(Building building, ContentValues values) {
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
