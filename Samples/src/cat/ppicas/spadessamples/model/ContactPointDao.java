package cat.ppicas.spadessamples.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;

public class ContactPointDao extends Dao<ContactPoint> {

	public static final Table TABLE = new TableBuilder("person", ContactPoint.class)
			.columnId("id")
			.columnAuto("person_id").notNull().foreignKey(PersonDao.ID).end()
			.columnAuto("name").notNull().end()
			.columnAuto("email").end()
			.columnAuto("phone").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();

	public static final Column PERSON_ID = TABLE.getColumn("phone_id");
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column EMAIL = TABLE.getColumn("email");
	public static final Column PHONE = TABLE.getColumn("phone");

	public static final EntityMapper<ContactPoint> MAPPER = new EntityMapper<ContactPoint>(TABLE) {
		@Override
		protected ContactPoint newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new ContactPoint();
		}

		@Override
		protected void mapContentValues(ContactPoint entity, ContentValues values) {
		}

		@Override
		protected void mapCursorValues(ContactPoint entity, Cursor cursor, CursorInfo cursorInfo) {
		}
	};

	public ContactPointDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
