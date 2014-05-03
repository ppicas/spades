package cat.picas.spadessamples.model.inheritance;

import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;

import static cat.picas.spades.Column.ColumnId;

public class RestaurantDao extends Dao<Restaurant> {

	public static final Table TABLE = Tables.newTable("restaurants", Restaurant.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column PLACE_ID = TABLE.newColumnAuto("place_id", "place").notNull().foreignKey(PlaceDao.ID).end();
	public static final Column MICHELINE_STARS = TABLE.newColumnAuto("micheline_stars", "mMichelineStars").end();

	public static final EntityMapper<Restaurant> MAPPER = new AutoEntityMapper<Restaurant>(TABLE);

	public RestaurantDao(SQLiteDatabase db, Table table, EntityMapper<Restaurant> mapper) {
		super(db, TABLE, MAPPER);
	}

}
