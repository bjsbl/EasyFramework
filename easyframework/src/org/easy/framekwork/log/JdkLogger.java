package org.easy.framekwork.log;

import java.util.logging.Level;

public class JdkLogger extends Logger {

	private java.util.logging.Logger log;
	private String clazzName;

	JdkLogger(Class<?> clazz) {
		log = java.util.logging.Logger.getLogger(clazz.getName());
		clazzName = clazz.getName();
	}

	JdkLogger(String name) {
		log = java.util.logging.Logger.getLogger(name);
		clazzName = name;
	}

	@Override
	public void debug(String message) {
		// TODO Auto-generated method stub
		log.logp(Level.FINE, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message);
	}

	@Override
	public void debug(String message, Throwable t) {
		// TODO Auto-generated method stub
		log.logp(Level.FINE, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message, t);
	}

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		log.logp(Level.INFO, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message);
	}

	@Override
	public void info(String message, Throwable t) {
		// TODO Auto-generated method stub
		log.logp(Level.INFO, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message, t);
	}

	@Override
	public void warn(String message) {
		// TODO Auto-generated method stub
		log.logp(Level.WARNING, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message);
	}

	@Override
	public void warn(String message, Throwable t) {
		// TODO Auto-generated method stub
		log.logp(Level.WARNING, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message, t);
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		log.logp(Level.SEVERE, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message);
	}

	@Override
	public void error(String message, Throwable t) {
		// TODO Auto-generated method stub
		log.logp(Level.SEVERE, clazzName, Thread.currentThread().getStackTrace()[1].getMethodName(), message, t);
	}

}
