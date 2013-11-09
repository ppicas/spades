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

package cat.ppicas.spades.query;

import cat.ppicas.spades.Column;
import cat.ppicas.spades.Table;

public class NameMapper {

	private static final String PREFIX = "T";

	public String alias(Table table) {
		return PREFIX + table.index;
	}

	public String ref(Column col) {
		return PREFIX + col.getTable().index + "." + col.name;
	}

	public String[] refs(Column... cols) {
		String[] alias = new String[cols.length];
		for (int i = 0; i < cols.length; i++) {
			alias[i] = ref(cols[i]);
		}
		return alias;
	}

}
