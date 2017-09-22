package org.easy.framekwork.orm.sql;

import java.util.Map;

import org.easy.framekwork.orm.DataBaseManager;
import org.easy.framekwork.util.ClassUtil;

public class EasySQL {

	public static <T> T select(Class<T> tableClass, String condition, Object... params) {
		String sql = SqlBuilder.generateSelect(tableClass, condition, "");
		return DataBaseManager.queryEntity(tableClass, sql, params);
	}

	public static boolean insert(Object entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}
		Class<?> tableClass = entity.getClass();
		Map<String, Object> fieldMap = ClassUtil.getFieldMap(entity);
		return insert(tableClass, fieldMap);
	}

	public static boolean insert(Class<?> tableClass, Map<String, Object> fieldMap) {
		if (fieldMap == null || fieldMap.isEmpty()) {
			return true;
		}
		String sql = SqlBuilder.generateInsert(tableClass, fieldMap.keySet());
		int rows = DataBaseManager.update(sql, fieldMap.values().toArray());
		return rows > 0;
	}

	public static boolean update(Class<?> tableClass, Map<String, Object> fieldMap, String condition) {
		if (fieldMap == null || fieldMap.isEmpty()) {
			return true;
		}
		String sql = SqlBuilder.generateUpdate(tableClass, fieldMap, condition);
		int rows = DataBaseManager.update(sql, fieldMap.values().toArray());
		return rows > 0;
	}

	public static boolean delete(Class<?> tableClass, String condition, Object... params) {
		String sql = SqlBuilder.generateDelete(tableClass, condition);
		int rows = DataBaseManager.update(sql, params);
		return rows > 0;
	}
}
