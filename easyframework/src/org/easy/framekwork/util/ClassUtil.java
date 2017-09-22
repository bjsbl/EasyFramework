package org.easy.framekwork.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassUtil {

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static Class<?> loadClass(String className) {
		return loadClass(className, true);
	}

	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> cls;
		System.out.println(className);
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return cls;
	}

	public static Map<String, Object> getFieldMap(Object obj) {
		Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		Method[] methods = obj.getClass().getMethods();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String fieldName = field.getName();
			Object fieldValue = getFieldValue(obj, methods, fieldName);
			fieldMap.put(fieldName, fieldValue);
		}
		return fieldMap;
	}

	public static Object getFieldValue(Object obj, Method[] methods, String fieldName) {
		Object value = null;
		try {
			for (Method method : methods) {
				if (method.getName().equalsIgnoreCase("get" + fieldName)) {
					value = method.invoke(obj);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return value;
	}
}
