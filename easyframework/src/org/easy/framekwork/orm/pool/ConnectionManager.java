package org.easy.framekwork.orm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import org.easy.framekwork.core.InstanceFactory;

public class ConnectionManager {

	public Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();
	public static final String defaultPoolName = "MysqlPool";

	public ConnectionManager() {
		ConnectionPool pool = new ConnectionPool(InstanceFactory.getConfig());
		if (pool != null) {
			pools.put(defaultPoolName, pool);
		}
	}

	public void close(String poolName, Connection conn) {
		ConnectionPool pool = getPool(poolName);
		try {
			if (pool != null) {
				pool.releaseConn(conn);
			}
		} catch (SQLException e) {
			System.out.println("连接池已经销毁");
			e.printStackTrace();
		}
	}

	public void destroy(String poolName) {
		ConnectionPool pool = getPool(poolName);
		if (pool != null) {
			pool.destroy();
		}
	}

	public ConnectionPool getPool(String poolName) {
		ConnectionPool pool = null;
		if (pools.size() > 0) {
			pool = pools.get(poolName);
		}
		return pool;
	}

	public Connection getConnection(String poolName) {
		Connection conn = null;
		if (pools.size() > 0 && pools.containsKey(poolName)) {
			conn = getPool(poolName).getConnection();
		} else {
			System.out.println("Error:Can't find this connecion pool ->" + poolName);
		}
		return conn;
	}
}
