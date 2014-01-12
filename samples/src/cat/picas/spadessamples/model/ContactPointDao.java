package cat.picas.spadessamples.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

public class ContactPointDao extends Dao<ContactPoint> {

	public static final Table TABLE = new TableBuilder("contact_point", ContactPoint.class)
			.columnId("id")
			.columnAuto("person_id").notNull().foreignKey(PersonDao.ID).end()
			.columnAuto("name").notNull().end()
			.columnAuto("email").end()
			.columnAuto("phone").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();

	public static final Column PERSON_ID = TABLE.getColumn("person_id");
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
