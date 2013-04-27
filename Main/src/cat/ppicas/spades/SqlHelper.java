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
