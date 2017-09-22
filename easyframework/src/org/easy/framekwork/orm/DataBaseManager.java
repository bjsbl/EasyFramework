package org.easy.framekwork.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.log.Logger;
import org.easy.framekwork.orm.pool.ConnectionManager;
import org.easy.framekwork.orm.sql.DBAccessor;

public class DataBaseManager {

	private static Logger logger = Logger.getLogger(DataBaseManager.class);
	private static final ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();
	private static final DBAccessor dbAccessor = InstanceFactory.getDBAccessor();
	private static final ConnectionManager connectionManager = InstanceFactory.getConnectionManager();

	public static Connection getConnection() {
		Connection conn;
		conn = connContainer.get();
		if (conn == null) {
			conn = connectionManager.getConnection(ConnectionManager.defaultPoolName);
			if (conn != null) {
				connContainer.set(conn);
			}
		}
		return conn;
	}

	public static void beginTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("开启事务出错！", e);
				throw new RuntimeException(e);
			} finally {
				connContainer.set(conn);
			}
		}
	}

	public static void commitTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				logger.error("提交事务出错！", e);
				throw new RuntimeException(e);
			} finally {
				connContainer.remove();
			}
		}
	}

	public static void rollbackTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				logger.error("回滚事务出错！", e);
				throw new RuntimeException(e);
			} finally {
				connContainer.remove();
			}
		}
	}

	public static int update(String sql, Object... params) {
		return 0;

	}

	public static <T> T queryEntity(Class<T> tableClass, String sql, Object... params) {
		return dbAccessor.queryEntity(tableClass, sql, params);
	}

	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		return dbAccessor.queryEntityList(entityClass, sql, params);
	}
}
