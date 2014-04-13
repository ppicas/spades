package cat.picas.spadessamples.model.inheritance;

import android.content.ContentValues;
import android.database.Cursor;

import cat.picas.spades.CursorInfo;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

public class PlaceDao {

	public static final Table TABLE = new TableBuilder("places", Place.class)
			.columnId("id")
			.columnAuto("name", "mName").notNull().end()
			.columnAuto("address", "mAddress").end()
			.build();

	public static final EntityMapper<Place> MAPPER = new EntityMapper<Place>(TABLE) {
		@Override
		public Place newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return null;
		}
	};

}
