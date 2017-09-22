package org.easy.framekwork.orm.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.easy.framekwork.log.Logger;

public class ConnectionPool {

	private Logger logger = Logger.getLogger(ConnectionPool.class);

	private Vector<Connection> activeConnections = new Vector<Connection>();
	private Vector<Connection> freeConnections = new Vector<Connection>();
	private PoolConfig config;
	private int poolActive = 0;
	private boolean isActive = false;
	private static ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();

	public ConnectionPool(PoolConfig config) {
		this.config = config;
		try {
			for (int i = 0; i < config.getInitialSize(); i++) {
				Connection con;
				con = createConnection();
				if (con != null) {
					freeConnections.add(con);
					++poolActive;
				}
			}
			isActive = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized Connection createConnection() throws Exception {
		Class.forName(config.getDriverClassName());
		Connection connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
		logger.info("create connection");
		return connection;
	}

	public synchronized Connection getConnection() {
		Connection conn = null;
		try {
			if (poolActive < this.config.getMaxActive()) {
				if (freeConnections.size() > 0) {
					conn = freeConnections.get(0);
					if (conn != null) {
						currentConnection.set(conn);
						activeConnections.add(conn);
					}
					freeConnections.remove(0);
				} else {
					conn = createConnection();
				}
			} else {
				wait(this.config.getMaxWait());
				conn = getConnection();
			}
			if (isValid(conn)) {
				activeConnections.add(conn);
				poolActive++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public Connection getCurrentConnecton() {
		Connection conn = currentConnection.get();
		if (!isValid(conn)) {
			conn = this.getConnection();
		}
		return conn;
	}

	private boolean isValid(Connection conn) {
		try {
			if (conn == null || conn.isClosed()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public synchronized void destroy() {
		for (Connection conn : freeConnections) {
			try {
				if (isValid(conn)) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (Connection conn : activeConnections) {
			try {
				if (isValid(conn)) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		isActive = false;
		poolActive = 0;
	}

	public boolean isActive() {
		return isActive;
	}

	public synchronized void releaseConn(Connection conn) throws SQLException {
		if (isValid(conn) && !(freeConnections.size() > config.getMaxActive())) {
			activeConnections.remove(conn);
			currentConnection.remove();
			if (isValid(conn)) {
				freeConnections.add(conn);
			}
			poolActive--;
			notifyAll();
		}
	}
}
