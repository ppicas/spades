package cat.picas.spades.fetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.Entity;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;

public class HashMapFetchStrategy<T extends Entity> implements FetchStrategy<T> {
	private Table mTable;
	private EntityMapper<T> mMapper;

	public HashMapFetchStrategy(Table table, EntityMapper<T> mapper) {
		mTable = table;
		mMapper = mapper;
	}

	@Override
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer) {
		if (!cursorInfo.hasTable(mTable)) {
			Collections.emptyList();
		}

		Map<Long, T> map = new HashMap<Long, T>();

		int oldPosition = cursor.getPosition();
		int idColIndex = cursorInfo.getColumnIndex(mTable.getColumnId());

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			T entity = map.get(cursor.getLong(idColIndex));
			if (entity == null) {
				entity = mMapper.createFromCursor(cursor, cursorInfo);
				map.put(entity.getEntityId(), entity);
			}
			if (consumer != null) {
				consumer.accept(cursor, cursorInfo, entity);
			}
		}

		cursor.moveToPosition(oldPosition);

		return new ArrayList<T>(map.values());
	}
}
