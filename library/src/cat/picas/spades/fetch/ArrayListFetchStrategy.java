package cat.picas.spades.fetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Entity;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;

public class ArrayListFetchStrategy<T extends Entity> implements FetchStrategy<T> {
	private Table mTable;
	private EntityMapper<T> mMapper;

	public ArrayListFetchStrategy(Table table, EntityMapper<T> mapper) {
		mTable = table;
		mMapper = mapper;
	}

	@Override
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer) {
		if (!cursorInfo.hasTable(mTable)) {
			Collections.emptyList();
		}

		ArrayList<T> list = new ArrayList<T>();

		int oldPosition = cursor.getPosition();

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			T entity = mMapper.createFromCursor(cursor, cursorInfo);
			list.add(entity);
			if (consumer != null) {
				consumer.accept(cursor, cursorInfo, entity);
			}
		}

		cursor.moveToPosition(oldPosition);

		return list;
	}
}
