package org.easy.framekwork.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.easy.framekwork.mvc.ActionInvoker;
import org.easy.framekwork.mvc.ViewResolver;
import org.easy.framekwork.orm.pool.ConnectionManager;
import org.easy.framekwork.orm.pool.PoolConfig;
import org.easy.framekwork.orm.sql.DBAccessor;
import org.easy.framekwork.util.ClassUtil;

public class InstanceFactory {

	private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

	public static ClassScanner getClassScanner() {
		return getInstance("ClassScanner", ClassScanner.class);
	}

	public static ActionInvoker getHandlerInvoker() {
		return getInstance("ActionInvoker", ActionInvoker.class);
	}

	public static ConnectionManager getConnectionManager() {
		return getInstance("ConnectionManager", ConnectionManager.class);
	}

	public static DBAccessor getDBAccessor() {
		return getInstance("DBAccessor", DBAccessor.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
		if (cache.containsKey(cacheKey)) {
			return (T) cache.get(cacheKey);
		}
		Class<?> cls = ClassUtil.loadClass(defaultImplClass.getName(), true);
		T instance = null;
		try {
			instance = (T) cls.newInstance();
			if (instance != null) {
				cache.put(cacheKey, instance);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public static ViewResolver getViewResolver() {
		return getInstance("ViewResolver", ViewResolver.class);
	}

	public static PoolConfig getConfig() {
		PoolConfig config = new PoolConfig();
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setHost("127.0.0.1");
		config.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
		config.setInitialSize(5);
		config.setMaxActive(10);
		config.setUser("root");
		config.setPassword("123456");
		config.setPort("3306");
		return config;
	}
}
