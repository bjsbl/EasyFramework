package org.easy.framekwork.orm.sql;

import java.util.Map;
import java.util.Set;

import org.easy.framekwork.orm.TableHandler;
import org.easy.framekwork.util.StringUtil;

public class SqlBuilder {

	public static String generateSelect(Class<?> tableClass, String where, String order) {
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(getTableName(tableClass));
		sql.append(generateWhere(where));
		sql.append(generateOrder(order));
		return sql.toString();
	}

	public static String generateInsert(Class<?> tableClass, Set<String> fields) {
		StringBuilder sql = new StringBuilder();
		if (fields != null && !fields.isEmpty()) {
			int i = 0;
			StringBuilder columns = new StringBuilder(" ");
			StringBuilder values = new StringBuilder(" values ");
			for (String fieldName : fields) {
				String columnName = TableHandler.getColumnName(tableClass, fieldName);
				if (i == 0) {
					columns.append("(").append(columnName);
					values.append("(?");
				} else {
					columns.append(", ").append(columnName);
					values.append(", ?");
				}
				if (i == fields.size() - 1) {
					columns.append(")");
					values.append(")");
				}
				i++;
			}
			sql.append(columns).append(values);
		}
		return sql.toString();
	}

	public static String generateUpdate(Class<?> tableClass, Map<String, Object> fieldMap, String where) {
		StringBuilder sql = new StringBuilder("update ").append(getTableName(tableClass));
		if (fieldMap != null && !fieldMap.isEmpty()) {
			sql.append(" set ");
			int i = 0;
			for (Map.Entry<String, Object> fieldEntry : fieldMap.entrySet()) {
				String fieldName = fieldEntry.getKey();
				String columnName = TableHandler.getColumnName(tableClass, fieldName);
				if (i == 0) {
					sql.append(columnName).append(" = ?");
				} else {
					sql.append(", ").append(columnName).append(" = ?");
				}
				i++;
			}
		}
		sql.append(generateWhere(where));
		return sql.toString();
	}

	public static String generateDelete(Class<?> tableClass, String where) {
		StringBuilder sql = new StringBuilder("delete from ").append(getTableName(tableClass));
		sql.append(generateWhere(where));
		return sql.toString();
	}

	public static String generateSelectForCount(Class<?> tableClass, String where) {
		StringBuilder sql = new StringBuilder("select count(*) from ").append(getTableName(tableClass));
		sql.append(generateWhere(where));
		return sql.toString();
	}

	public static String generateSelectSqlForPager(int pageNumber, int pageSize, Class<?> entityClass, String condition, String sort) {
		StringBuilder sql = new StringBuilder();
		String table = getTableName(entityClass);
		String where = generateWhere(condition);
		String order = generateOrder(sort);
		String dbType = "mysql";
		if (dbType.equalsIgnoreCase("mysql")) {
			int pageStart = (pageNumber - 1) * pageSize;
			appendForMySql(sql, table, where, order, pageStart, pageSize);
		} else if (dbType.equalsIgnoreCase("oracle")) {
			int pageStart = (pageNumber - 1) * pageSize + 1;
			int pageEnd = pageStart + pageSize;
			appendForOracle(sql, table, where, order, pageStart, pageEnd);
		}
		return sql.toString();
	}

	private static void appendForOracle(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
		sql.append("select a.* from (select rownum rn, t.* from ").append(table).append(" t");
		sql.append(where);
		sql.append(order);
		sql.append(") a where a.rn >= ").append(pageStart).append(" and a.rn < ").append(pageEnd);
	}

	private static void appendForMySql(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
		sql.append("select * from ").append(table);
		sql.append(where);
		sql.append(order);
		sql.append(" limit ").append(pageStart).append(", ").append(pageEnd);
	}

	private static String getTableName(Class<?> tableClass) {
		return TableHandler.getTableName(tableClass);
	}

	private static String generateWhere(String condition) {
		String where = "";
		if (StringUtil.isNotEmpty(condition)) {
			where += " where " + condition;
		}
		return where;
	}

	private static String generateOrder(String sort) {
		String order = "";
		if (StringUtil.isNotEmpty(sort)) {
			order += " order by " + sort;
		}
		return order;
	}
}
