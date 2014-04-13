package cat.picas.spadessamples.model.inheritance;

import android.content.ContentValues;
import android.database.Cursor;

import cat.picas.spades.Column;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

import static cat.picas.spades.Column.ColumnId;

public class PlaceDao {

	public static final Table TABLE = new TableBuilder("places", Place.class)
			.columnId("id")
			.columnAuto("name", "mName").notNull().end()
			.columnAuto("address", "mAddress").end()
			.columnAuto("type", "mType").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column TYPE = TABLE.getColumn("type");

	public static final EntityMapper<Place> MAPPER = new EntityMapper<Place>(TABLE) {
		@Override
		public Place newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return null;
		}
	};

}
