package cat.ppicas.spades.models.manual;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;

public class CompanyDao extends Dao<Company> {

	public static final Table<Company> TABLE = new Table<Company>("companies_manual", Company.class);

	public static final Column ID = TABLE.columnId();
	public static final Column NAME = TABLE.column().text("name").notNull().end();
	public static final Column FUNDATION_YEAR = TABLE.column().integer("fundation_year").notNull().end();
	public static final Column REGISTRATION = TABLE.column().integer("registration").end();

	public static final EntityMapper<Company> MAPPER = new EntityMapper<Company>(TABLE) {

		@Override
		protected Company newInstance(Cursor cursor, int[] mappings) {
			return new Company();
		}

		@Override
		protected void mapCursorValues(Company company, Cursor cursor, int[] maps) {
			int index;
			index = maps[NAME.index];
			if (index != -1) {
				company.setName(cursor.getString(index));
			}
			index = maps[FUNDATION_YEAR.index];
			if (index != -1) {
				company.setFundationYear(cursor.getInt(index));
			}
			index = maps[REGISTRATION.index];
			if (index != -1) {
				company.setRegistration(cursor.isNull(index) ? null
						: new Date(cursor.getLong(index)));
			}

			company.getMainBuilding().setKey(company.getEntityId());
		}

		@Override
		protected void mapContentValues(Company company, ContentValues values) {
			values.put(NAME.name, company.getName());
			values.put(FUNDATION_YEAR.name, company.getFundationYear());
			Date date = company.getRegistration();
			values.put(REGISTRATION.name, date != null ? date.getTime() : null);
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
