package org.easy.framekwork.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easy.framekwork.core.ClassScanner;
import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.orm.annotation.Column;
import org.easy.framekwork.orm.annotation.Table;
import org.easy.framekwork.util.StringUtil;

public class TableHandler {

	private static final Map<Class<?>, String> tableClassNameMap = new HashMap<Class<?>, String>();
	private static final Map<Class<?>, Map<String, String>> tableClassFieldMapMap = new HashMap<Class<?>, Map<String, String>>();
	private static final ClassScanner scanner = InstanceFactory.getClassScanner();

	static {
		List<Class<?>> entityClassList = scanner.getClassListByAnnotation("com", Table.class);
		for (Class<?> tableClass : entityClassList) {
			String tableName = tableClass.getAnnotation(Table.class).value();
			tableClassNameMap.put(tableClass, tableName);
			Field[] fields = tableClass.getDeclaredFields();
			Map<String, String> fieldMap = new HashMap<String, String>();
			for (Field field : fields) {
				String fieldName = field.getName();
				if (field.isAnnotationPresent(Column.class)) {
					fieldMap.put(fieldName, field.getAnnotation(Column.class).value());
				}
			}
			tableClassFieldMapMap.put(tableClass, fieldMap);
		}
	}

	public static String getTableName(Class<?> tableClass) {
		return tableClassNameMap.get(tableClass);
	}

	public static String getColumnName(Class<?> tableClass, String fieldName) {
		String columnName = tableClassFieldMapMap.get(tableClass).get(fieldName);
		return StringUtil.isNotEmpty(columnName) ? columnName : fieldName;
	}
}
