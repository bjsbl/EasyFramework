package org.easy.framekwork.orm.sql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.log.Logger;
import org.easy.framekwork.orm.pool.ConnectionManager;

public class DBAccessor {

	private static final Logger logger = Logger.getLogger(DBAccessor.class);
	private static final ConnectionManager connectionManager = InstanceFactory.getConnectionManager();

	public <T> T queryEntity(Class<T> tableClass, String sql, Object[] params) {
		T result = null;
		try {
			int index = 1;
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(index++, params[i]);
				}
			}
			ResultSet resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int colsCount = metaData.getColumnCount();
			if (resultSet.next()) {
				result = tableClass.newInstance();
				for (int i = 0; i < colsCount; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = tableClass.getDeclaredField(cols_name);
					field.setAccessible(true);
					field.set(result, cols_value);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		logger.info(String.format("[Easy] SQL - {%s}", sql));
		return result;
	}

	@SuppressWarnings("null")
	public <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> result = null;
		try {
			int index = 1;
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(index++, params[i]);
				}
			}
			ResultSet resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int colsCount;
			colsCount = metaData.getColumnCount();
			while (resultSet.next()) {
				T resultObject = entityClass.newInstance();
				for (int i = 0; i < colsCount; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = entityClass.getDeclaredField(cols_name);
					field.setAccessible(true);
					field.set(resultObject, cols_value);
				}
				result.add(resultObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		logger.info(String.format("[Easy] SQL - {%s}", sql));
		return result;
	}

	public long queryCount(String sql, Object... params) {
		long result = -1;
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("统计出错", e);
			throw new RuntimeException(e);
		}
		logger.info(String.format("[Easy] SQL - {%s}", sql));
		return result;
	}

	public int executeUpdate(String sql, Object... params) {
		int result = -1;
		try {
			int index = 1;
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(index++, params[i]);
				}
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("更新出错", e);
			throw new RuntimeException(e);
		}
		logger.info(String.format("[Easy] SQL - {%s}", sql));
		return result;
	}

	public Serializable insertReturnPK(String sql, Object... params) {
		Serializable key = null;
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			int rows = pstmt.executeUpdate();
			if (rows == 1) {
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					key = (Serializable) rs.getObject(1);
				}
			}
		} catch (SQLException e) {
			logger.error("插入出错", e);
			throw new RuntimeException(e);
		}
		logger.info(String.format("[Easy] SQL - {%s}", sql));
		return key;
	}

	public Connection getConnection() {
		return connectionManager.getConnection(ConnectionManager.defaultPoolName);
	}

}
