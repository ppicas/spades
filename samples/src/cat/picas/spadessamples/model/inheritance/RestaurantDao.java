package cat.picas.spadessamples.model.inheritance;

import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

import static cat.picas.spades.Column.ColumnId;

public class RestaurantDao extends Dao<Restaurant> {

	public static final Table TABLE = new TableBuilder("restaurants", Restaurant.class)
			.columnId("id")
			.columnAuto("place_id", "place").notNull().foreignKey(PlaceDao.ID).end()
			.columnAuto("micheline_stars", "mMichelineStars").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column PLACE_ID = TABLE.getColumn("place_id");
	public static final Column MICHELINE_STARS = TABLE.getColumn("micheline_stars");

	public static final EntityMapper<Restaurant> MAPPER = new AutoEntityMapper<Restaurant>(TABLE);

	public RestaurantDao(SQLiteDatabase db, Table table, EntityMapper<Restaurant> mapper) {
		super(db, TABLE, MAPPER);
	}

}
