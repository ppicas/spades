package cat.picas.spades.fetch;

import java.util.List;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.Entity;

public interface FetchStrategy<T extends Entity> {
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer);
}
