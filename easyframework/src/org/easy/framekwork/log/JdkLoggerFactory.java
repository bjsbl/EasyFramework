package org.easy.framekwork.log;

public class JdkLoggerFactory implements LoggerFactory {

	@Override
	public Logger getLogger(Class<?> clazz) {
		// TODO Auto-generated method stub
		return new JdkLogger(clazz);
	}

	@Override
	public Logger getLogger(String clazz) {
		// TODO Auto-generated method stub
		return new JdkLogger(clazz);
	}

}
