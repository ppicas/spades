package cat.ppicas.spadessamples.model;

import java.util.List;

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
import cat.ppicas.spades.fetch.HashMapFetchStrategy;
import cat.ppicas.spades.query.Query;
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

	private HashMapFetchStrategy<Person> mHashMapFetchStrategy;

	public PersonDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
		mHashMapFetchStrategy = new HashMapFetchStrategy<Person>(TABLE, MAPPER);
	}

	public List<Person> fetchAllWithRelated(Query query) {
		return fetchAll(query, mHashMapFetchStrategy, mFetchRelatedConsumer);
	}

	public List<Person> fetchAllWithRelated(Cursor cursor, CursorInfo cursorInfo) {
		return fetchAll(cursor, cursorInfo, mHashMapFetchStrategy, mFetchRelatedConsumer);
	}

	private final EntityConsumer<Person> mFetchRelatedConsumer = new EntityConsumer<Person>() {
		@Override
		public void accept(Cursor cursor, CursorInfo cursorInfo, Person person) {
			person.getContactPoints().fetchAndAddOne(cursor, cursorInfo);
		}
	};

}