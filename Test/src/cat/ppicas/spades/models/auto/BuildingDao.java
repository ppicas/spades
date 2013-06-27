package cat.ppicas.spades.models.auto;

import static cat.ppicas.spades.ColumnBuilder.DefaultValue.EMTPY;
import static cat.ppicas.spades.ColumnBuilder.DefaultValue.FALSE;
import static cat.ppicas.spades.ColumnBuilder.DefaultValue.ZERO;
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
			.columnAuto("company").notNull().foreignKey(CompanyDao.ID).end()
			.columnAuto("address").notNull(EMTPY).end()
			.columnAuto("phone").end()
			.columnAuto("floors").notNull(ZERO).end()
			.columnAuto("surface").notNull(ZERO).end()
			.columnAuto("is_main").notNull(FALSE).end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column COMPANY_ID = TABLE.getColumn("company");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column PHONE = TABLE.getColumn("phone");
	public static final Column FLOORS = TABLE.getColumn("floors");
	public static final Column SURFACE = TABLE.getColumn("surface");
	public static final Column IS_MAIN = TABLE.getColumn("is_main");

	/*public static final Table<BuildingAuto> TABLE = new Table<BuildingAuto>("buildings_auto", BuildingAuto.class);

	public static final Column ID = TABLE.columnId();

	public static final Column COMPANY_ID = TABLE.column().auto("mCompany").notNull().foreignKey(CompanyDao.ID).end();
	public static final Column ADDRESS = TABLE.column().auto("mAddress").notNull(DEFAULT_EMTPY).end();
	public static final Column PHONE = TABLE.column().auto("mPhone").end();
	public static final Column FLOORS = TABLE.column().auto("mFloors").notNull(DEFAULT_ZERO).end();
	public static final Column SURFACE = TABLE.column().auto("mSurface").notNull(DEFAULT_ZERO).end();
	public static final Column IS_MAIN = TABLE.column().auto("mMain").notNull(DEFAULT_FALSE).end();*/

	public static final EntityMapper<BuildingAuto> MAPPER = new EntityMapper<BuildingAuto>(TABLE) {

		@Override
		protected BuildingAuto newInstance(Cursor cursor, int[] mappings) {
			return new BuildingAuto();
		}

		@Override
		protected void mapCursorValues(BuildingAuto building, Cursor cursor, int[] maps) {
		}

		@Override
		protected void mapContentValues(BuildingAuto building, ContentValues values) {
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
