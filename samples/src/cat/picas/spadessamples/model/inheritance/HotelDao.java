package cat.picas.spadessamples.model.inheritance;

import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;

import static cat.picas.spades.Column.ColumnId;

public class HotelDao extends Dao<Hotel> {

	public static final Table TABLE = Tables.newTable("restaurants", Hotel.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column PLACE_ID = TABLE.newColumnAuto("place_id", "place").notNull().foreignKey(PlaceDao.ID).end();
	public static final Column TOTAL_ROOMS = TABLE.newColumnAuto("total_rooms", "mTotalRooms").end();
	public static final Column AVAILABLE_ROOMS = TABLE.newColumnAuto("available_rooms", "mAvailableRooms").end();

	public static final EntityMapper<Hotel> MAPPER = new AutoEntityMapper<Hotel>(TABLE);

	public HotelDao(SQLiteDatabase db, Table table, EntityMapper<Restaurant> mapper) {
		super(db, TABLE, MAPPER);
	}

}
