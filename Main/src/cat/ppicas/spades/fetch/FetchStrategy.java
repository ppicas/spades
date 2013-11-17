package cat.ppicas.spades.fetch;

import java.util.List;

import android.database.Cursor;
import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spades.Dao.EntityConsumer;
import cat.ppicas.spades.Entity;

public interface FetchStrategy<T extends Entity> {
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer);
}
