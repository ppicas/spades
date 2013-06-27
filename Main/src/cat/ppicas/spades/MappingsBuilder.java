package cat.ppicas.spades;

import java.util.Arrays;
import java.util.List;

public class MappingsBuilder {

	private int mOffset;
	private Tables mTables;
	private int[][] mMappings;

	public MappingsBuilder() {
		mOffset = 0;
		mTables = Tables.getInstance();
		mMappings = new int[mTables.size()][];
	}

	public MappingsBuilder addOffset() {
		mOffset++;
		return this;
	}

	public MappingsBuilder addOffset(int inc) {
		mOffset += inc;
		return this;
	}

	public MappingsBuilder add(Table table) {
		add(table.getColumns());
		return this;
	}

	public MappingsBuilder add(Column column) {
		int tableIndex = column.table.index;
		if (mMappings[tableIndex] == null) {
			mMappings[tableIndex] = new int[column.table.getColumnsNumber()];
			Arrays.fill(mMappings[tableIndex], -1);
		}

		if (mMappings[tableIndex][column.index] == -1) {
			mMappings[tableIndex][column.index] = mOffset++;
		}

		return this;
	}

	public MappingsBuilder add(List<? extends Column> columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public MappingsBuilder add(Column... columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public int[][] build() {
		return mMappings;
	}

}
