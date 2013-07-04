package cat.ppicas.spades.models.manual;

import static cat.ppicas.spades.ColumnBuilder.DefaultValue.EMTPY;
import static cat.ppicas.spades.ColumnBuilder.DefaultValue.FALSE;
import static cat.ppicas.spades.ColumnBuilder.DefaultValue.ZERO;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;
import cat.ppicas.spades.models.auto.CompanyDao;

public class BuildingDao extends Dao<BuildingManual> {

	public static final Table TABLE = new TableBuilder("buildings_manual", BuildingManual.class)
			.columnId("id")
			.columnInteger("company_id").notNull().foreignKey(CompanyDao.ID).end()
			.columnText("address").notNull(EMTPY).end()
			.columnText("phone").end()
			.columnInteger("floors").notNull(ZERO).end()
			.columnReal("surface").notNull(ZERO).end()
			.columnInteger("is_main").notNull(FALSE).end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column COMPANY_ID = TABLE.getColumn("company_id");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column PHONE = TABLE.getColumn("phone");
	public static final Column FLOORS = TABLE.getColumn("floors");
	public static final Column SURFACE = TABLE.getColumn("surface");
	public static final Column IS_MAIN = TABLE.getColumn("is_main");

	/*public static final Table<BuildingManual> TABLE = new Table<BuildingManual>("buildings_manual", BuildingManual.class);

	public static final Column ID = TABLE.columnId();

	public static final Column COMPANY_ID = TABLE.column().integer("company_id").notNull().foreignKey(CompanyDao.ID).end();
	public static final Column ADDRESS = TABLE.column().text("address").notNull(DEFAULT_EMTPY).end();
	public static final Column PHONE = TABLE.column().text("phone").end();
	public static final Column FLOORS = TABLE.column().integer("floors").notNull(DEFAULT_ZERO).end();
	public static final Column SURFACE = TABLE.column().real("surface").notNull(DEFAULT_ZERO).end();
	public static final Column IS_MAIN = TABLE.column().integer("is_main").notNull(DEFAULT_FALSE).end();*/

	public static final EntityMapper<BuildingManual> MAPPER = new EntityMapper<BuildingManual>(TABLE) {

		@Override
		protected BuildingManual newInstance(Cursor cursor, int[] mappings) {
			return new BuildingManual();
		}

		@Override
		protected void mapCursorValues(BuildingManual building, Cursor cursor, int[] maps) {
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
		protected void mapContentValues(BuildingManual building, ContentValues values) {
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

	@Override
	protected void fetchRelatedFields(Cursor cursor, int[][] mappings, BuildingManual entity) {
		super.fetchRelatedFields(cursor, mappings, entity);
		entity.getCompany().fetch(cursor, mappings);
	}

}
