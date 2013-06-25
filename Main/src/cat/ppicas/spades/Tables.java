package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tables {

	private static Tables sInstance;

	private List<Table<?>> mTables = new ArrayList<Table<?>>();

	public static Tables getInstance() {
		if (sInstance == null) {
			sInstance = new Tables();
		}

		return sInstance;
	}

	private Tables() {}

	public Table<?> getTable(int index) {
		if (index < 0 || index >= mTables.size()) {
			return null;
		}

		return mTables.get(index);
	}

	public List<Table<?>> getTables() {
		return Collections.unmodifiableList(mTables);
	}

	public int size() {
		return mTables.size();
	}

	protected int nextTableIndex() {
		return mTables.size();
	}

	protected void addTable(Table<?> table) {
		mTables.add(table);
	}

}
