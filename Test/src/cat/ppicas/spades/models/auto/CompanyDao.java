package cat.ppicas.spades.models.auto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;

public class CompanyDao extends Dao<Company> {

	public static final Table<Company> TABLE = new Table<Company>("companies_auto", Company.class);

	public static final Column ID = TABLE.columnId();
	public static final Column NAME = TABLE.column().auto("mName").notNull().end();
	public static final Column FUNDATION_YEAR = TABLE.column().auto("mFundationYear").notNull().end();
	public static final Column REGISTRATION = TABLE.column().auto("mRegistration").end();

	public static final EntityMapper<Company> MAPPER = new EntityMapper<Company>(TABLE) {

		@Override
		protected Company newInstance(Cursor cursor, int[] mappings) {
			return new Company();
		}

		@Override
		protected void mapCursorValues(Company entity, Cursor cursor, int[] mappings) {
			entity.getMainBuilding().setKey(entity.getEntityId());
		}

		@Override
		protected void mapContentValues(Company entity, ContentValues values) {
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
