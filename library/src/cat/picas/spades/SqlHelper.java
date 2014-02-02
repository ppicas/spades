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

import static android.text.TextUtils.join;
import cat.picas.spades.query.NameMapper;
import android.text.TextUtils;

public class SqlHelper {

	private static final NameMapper sMapper = new NameMapper();

	public static String table(Table table) {
		return table.name + " AS " + sMapper.alias(table);
	}

	public static String column(Column col) {
		return sMapper.ref(col);
	}

	public static String expr(Column col, String exp) {
		return String.format("%s %s", sMapper.ref(col), exp);
	}

	public static String expr(String expr, Column... cols) {
		return String.format(expr, sMapper.refs(cols));
	}

	public static String and(Iterable<String> exprs) {
		return "(" + join(" AND ", exprs) + ")";
	}

	public static String and(String... exprs) {
		return "(" + join(" AND ", exprs) + ")";
	}

	public static String or(Iterable<String> exprs) {
		return "(" + join(" OR ", exprs) + ")";
	}

	public static String or(String... exprs) {
		return "(" + join(" OR ", exprs) + ")";
	}

	public static String createTable(String tableName, String... colDefs) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(\n");
		sql.append(TextUtils.join(",\n", colDefs));
		sql.append(")");

		return sql.toString();
	}

	public static String createIndex(String indexName, boolean unique, String tableName,
			String... colDefs) {
		StringBuilder sql = new StringBuilder();
		if (unique) {
			sql.append("CREATE UNIQUE INDEX IF NOT EXISTS ");
		} else {
			sql.append("CREATE INDEX IF NOT EXISTS ");
		}
		sql.append(indexName).append(" ON ").append(tableName).append("(\n");
		sql.append(TextUtils.join(",\n", colDefs));
		sql.append(")");

		return sql.toString();
	}

	public static String alterTableAddColumn(String tableName, String colDef) {
		StringBuilder sql = new StringBuilder();
		sql.append("ALTER TABLE ").append(tableName).append(" ");
		sql.append("ADD COLUMN ").append(colDef);

		return sql.toString();
	}

	public static String renameTable(String tableName, String newTableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("ALTER TABLE ").append(tableName).append(" ");
		sql.append("RENAME TO ").append(newTableName);

		return sql.toString();
	}

	public static String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS " + tableName;
	}

	public static String dropIndex(String indexName) {
		return "DROP INDEX IF EXISTS " + indexName;
	}

}
