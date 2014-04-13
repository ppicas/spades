package cat.picas.spadessamples.model.inheritance;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.TableBuilder;

public class RestaurantDao extends Dao<Restaurant> {

	public static final Table TABLE = new TableBuilder("restaurant", Restaurant.class)
			.columnId("id")
			.columnAuto("micheline_stars", "mMichelineStars").end()
			.build();

	public static final EntityMapper<Restaurant> MAPPER = new EntityMapper<Restaurant>(TABLE) {
		@Override
		public Restaurant newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new Restaurant();
		}

		@Override
		public void mapContentValues(Restaurant entity, ContentValues values) {
			PlaceDao.MAPPER.mapContentValues(entity, values);
			super.mapContentValues(entity, values);
		}

		@Override
		public void mapCursorValues(Restaurant entity, Cursor cursor, CursorInfo cursorInfo) {
			PlaceDao.MAPPER.mapCursorValues(entity, cursor, cursorInfo);
			super.mapCursorValues(entity, cursor, cursorInfo);
		}
	};
	private final Dao<Place> mPlaceDao;

	public RestaurantDao(SQLiteDatabase db, Table table, EntityMapper<Restaurant> mapper) {
		super(db, table, mapper);

		mPlaceDao = new Dao<Place>(db, PlaceDao.TABLE, PlaceDao.MAPPER);
	}

}
