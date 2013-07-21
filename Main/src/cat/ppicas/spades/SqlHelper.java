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

package cat.ppicas.spades;

import android.text.TextUtils;

public class SqlHelper {

	public static String genCreateTable(String tableName, String... colDefs) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS " + tableName + "(\n");
		sql.append(TextUtils.join(",\n", colDefs));
		sql.append(")");

		return sql.toString();
	}

	public static String genCreateIndex(String tableName, int indexNum, String... colDefs) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE INDEX IF NOT EXISTS " + tableName + "_idx" + indexNum
				+ " ON " + tableName + "(\n");
		sql.append(TextUtils.join(",\n", colDefs));
		sql.append(")");

		return sql.toString();
	}

	public static String genDropTable(String tableName) {
		return "DROP TABLE IF EXISTS " + tableName;
	}

	public static String genDropIndex(String tableName, int indexNum) {
		return "DROP INDEX IF EXISTS " + tableName + "_idx" + indexNum;
	}

}
