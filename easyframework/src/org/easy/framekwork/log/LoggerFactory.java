package org.easy.framekwork.log;

public interface LoggerFactory {

	Logger getLogger(Class<?> clazz);

	Logger getLogger(String clazz);
}
