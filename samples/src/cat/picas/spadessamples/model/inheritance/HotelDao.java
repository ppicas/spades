package cat.picas.spadessamples.model.inheritance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.Column;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

import static cat.picas.spades.Column.ColumnId;

public class HotelDao extends Dao<Hotel> {

	public static final Table TABLE = new TableBuilder("restaurants", Hotel.class)
			.columnId("id")
			.columnAuto("place_id", "place").notNull().foreignKey(PlaceDao.ID).end()
			.columnAuto("total_rooms", "mTotalRooms").end()
			.columnAuto("available_rooms", "mAvailableRooms").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column PLACE_ID = TABLE.getColumn("place_id");
	public static final Column TOTAL_ROOMS = TABLE.getColumn("total_rooms");
	public static final Column AVAILABLE_ROOMS = TABLE.getColumn("available_rooms");

	public static final EntityMapper<Hotel> MAPPER = new EntityMapper<Hotel>(TABLE) {
		@Override
		public Hotel newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new Hotel();
		}
	};

	public HotelDao(SQLiteDatabase db, Table table, EntityMapper<Restaurant> mapper) {
		super(db, TABLE, MAPPER);
	}

}
