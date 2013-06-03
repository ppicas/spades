package cat.ppicas.spades.models.auto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;

public class CompanyDao extends Dao<CompanyAuto> {

	public static final Table<CompanyAuto> TABLE = new Table<CompanyAuto>("companies_auto", CompanyAuto.class);

	public static final Column ID = TABLE.columnId();
	public static final Column NAME = TABLE.column().auto("mName").notNull().end();
	public static final Column FUNDATION_YEAR = TABLE.column().auto("mFundationYear").notNull().end();
	public static final Column REGISTRATION = TABLE.column().auto("mRegistration").end();

	public static final EntityMapper<CompanyAuto> MAPPER = new EntityMapper<CompanyAuto>(TABLE) {

		@Override
		protected CompanyAuto newInstance(Cursor cursor, int[] mappings) {
			return new CompanyAuto();
		}

		@Override
		protected void mapCursorValues(CompanyAuto entity, Cursor cursor, int[] mappings) {
			entity.getMainBuilding().setKey(entity.getEntityId());
		}

		@Override
		protected void mapContentValues(CompanyAuto entity, ContentValues values) {
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
