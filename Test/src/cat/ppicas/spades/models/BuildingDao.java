package cat.ppicas.spades.models;

import static cat.ppicas.spades.ColumnBuilder.DEFAULT_EMTPY;
import static cat.ppicas.spades.ColumnBuilder.DEFAULT_ZERO;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.query.Query;

public class BuildingDao extends Dao<Building> {

	public static final Table<Building> TABLE = new Table<Building>("buildings", Building.class);

	public static final Column ID = TABLE.columnId();

	public static final Column COMPANY_ID = TABLE.column().integer("company_id").notNull().foreignKey(CompanyDao.ID).end();
	public static final Column ADDRESS = TABLE.column().text("address").notNull(DEFAULT_EMTPY).end();
	public static final Column PHONE = TABLE.column().text("phone").end();
	public static final Column FLOORS = TABLE.column().integer("floors").notNull(DEFAULT_ZERO).end();
	public static final Column SURFACE = TABLE.column().real("surface").notNull(DEFAULT_ZERO).end();
	public static final Column IS_MAIN = TABLE.column().integer("is_main").notNull(DEFAULT_ZERO).end();

	public static final EntityMapper<Building> MAPPER = new EntityMapper<Building>(TABLE) {

		@Override
		protected Building newInstance(Cursor cursor, int[] mappings) {
			return new Building();
		}

		@Override
		protected void mapCursorValues(Building building, Cursor cursor, int[] maps) {
			int index;
			index = maps[COMPANY_ID.index];
			if (index != -1) {
				building.getCompany().setKey(cursor.getLong(index));
			}
			index = maps[ADDRESS.index];
			if (index != -1) {
				building.setAddress(cursor.getString(index));
			}
			index = maps[PHONE.index];
			if (index != -1) {
				building.setPhone(cursor.isNull(index) ? null : cursor.getString(index));
			}
			index = maps[FLOORS.index];
			if (index != -1) {
				building.setFloors(cursor.getInt(index));
			}
			index = maps[SURFACE.index];
			if (index != -1) {
				building.setSurface(cursor.getDouble(index));
			}
			index = maps[IS_MAIN.index];
			if (index != -1) {
				building.setMain(cursor.getInt(index) == 1);
			}
		}

		@Override
		protected void mapContentValues(Building building, ContentValues values) {
			values.put(COMPANY_ID.name, building.getCompany().getKey());
			if (building.getAddress() != null) {
				values.put(ADDRESS.name, building.getAddress());
			}
			values.put(PHONE.name, building.getPhone());
			values.put(FLOORS.name, building.getFloors());
			values.put(SURFACE.name, building.getSurface());
			values.put(IS_MAIN.name, building.isMain());
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

	public List<Building> fetchAllRelated(Query query) {
		final int[][] mappings = query.getMappings();
		return fetchAll(query, new EntityConsumer<Building>() {
			@Override
			public void accept(Building building, Cursor cursor) {
				building.getCompany().fetch(cursor, mappings);
			}
		});
	}

}
