package cat.picas.spadessamples.model.inheritance;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;

import static cat.picas.spades.Column.ColumnId;

public class PlaceDao {

	public static final Table TABLE = Tables.newTable("places", Place.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column NAME = TABLE.newColumnAuto("name", "mName").notNull().end();
	public static final Column ADDRESS = TABLE.newColumnAuto("address", "mAddress").end();
	public static final Column TYPE = TABLE.newColumnAuto("type", "mType").end();

	public static final EntityMapper<Place> MAPPER = new AutoEntityMapper<Place>(TABLE);

}
