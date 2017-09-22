package org.easy.framekwork.log;

public abstract class Logger {

	private static LoggerFactory factory;

	static {
		factory = new JdkLoggerFactory();
	}

	public static void setLoggerFactory(LoggerFactory loggerFactory) {
		if (loggerFactory != null)
			Logger.factory = loggerFactory;
	}

	public static Logger getLogger(Class<?> clazz) {
		return factory.getLogger(clazz);
	}

	public static Logger getLogger(String name) {
		return factory.getLogger(name);
	}

	public abstract void debug(String message);

	public abstract void debug(String message, Throwable t);

	public abstract void info(String message);

	public abstract void info(String message, Throwable t);

	public abstract void warn(String message);

	public abstract void warn(String message, Throwable t);

	public abstract void error(String message);

	public abstract void error(String message, Throwable t);
}
