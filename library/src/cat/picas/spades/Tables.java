/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cat.picas.spades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tables {

	private static volatile Tables sInstance;

	private List<Table> mTables = new ArrayList<Table>();

	public static Tables getInstance() {
		if (sInstance == null) {
			synchronized (Tables.class) {
				if (sInstance == null) {
					sInstance = new Tables();
				}
			}
		}

		return sInstance;
	}

	private Tables() {}

	public Table getTable(int index) {
		if (index < 0 || index >= mTables.size()) {
			return null;
		}

		return mTables.get(index);
	}

	public List<Table> getTables() {
		return Collections.unmodifiableList(mTables);
	}

	public int size() {
		return mTables.size();
	}

	protected int nextTableIndex() {
		return mTables.size();
	}

	protected void addTable(Table table) {
		mTables.add(table);
	}

}
