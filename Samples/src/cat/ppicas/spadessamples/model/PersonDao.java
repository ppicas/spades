package cat.ppicas.spadessamples.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cat.ppicas.spadessamples.model.Person.Gender;

public class PersonDao extends Dao<Person> {

	public static final Table TABLE = new TableBuilder("person", Person.class)
			.columnId("id")
			.columnAuto("name").notNull().end()
			.columnAuto("birth_date").end()
			.columnAuto("gender").notNull().defaultValue(Gender.MALE).end()
			.columnAuto("spouse_id").end() // TODO Missing capacity to set self references.
			.columnAuto("height").indexed(false, true).end()
			.columnAuto("weight").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();

	public static final Column NAME = TABLE.getColumn("name");
	public static final Column BIRTH_DATE = TABLE.getColumn("birth_date");
	public static final Column GENDER = TABLE.getColumn("gender");
	public static final Column SPOUSE = TABLE.getColumn("spouse_id");
	public static final Column HEIGHT = TABLE.getColumn("height");
	public static final Column WEIGHT = TABLE.getColumn("weight");

	public static final EntityMapper<Person> MAPPER = new EntityMapper<Person>(TABLE) {
		@Override
		protected Person newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new Person();
		}

		@Override
		protected void mapContentValues(Person entity, ContentValues values) {
		}

		@Override
		protected void mapCursorValues(Person entity, Cursor cursor, CursorInfo cursorInfo) {
		}
	};

	public PersonDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

	public List<Person> fetchAllWithRelated(Cursor cursor, CursorInfo cursorInfo) {
		Map<Long, Person> map = new HashMap<Long, Person>();

		int idColIndex = cursorInfo.getColumnIndex(ID);
		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			Person person = map.get(cursor.getLong(idColIndex));
			if (person == null) {
				person = MAPPER.createFromCursor(cursor, cursorInfo);
				map.put(person.getEntityId(), person);
			}

			person.getContactPoints().fetchAndAddOne(cursor, cursorInfo);
		}

		return new ArrayList<Person>(map.values());
	}

}
