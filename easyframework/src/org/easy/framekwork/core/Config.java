package org.easy.framekwork.core;

import java.util.Map;
import java.util.Properties;

import org.easy.framekwork.Constant;
import org.easy.framekwork.util.PropsUtil;

public class Config {

	private static final Properties configProps = PropsUtil.loadProps(Constant.CONFIG_PROPS);

	public static String getString(String key) {
		return PropsUtil.getString(configProps, key);
	}

	public static String getString(String key, String defaultValue) {
		return PropsUtil.getString(configProps, key, defaultValue);
	}

	public static int getInt(String key) {
		return PropsUtil.getNumber(configProps, key);
	}

	public static int getInt(String key, int defaultValue) {
		return PropsUtil.getNumber(configProps, key, defaultValue);
	}

	public static boolean getBoolean(String key) {
		return PropsUtil.getBoolean(configProps, key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return PropsUtil.getBoolean(configProps, key, defaultValue);
	}

	public static Map<String, Object> getMap(String prefix) {
		return PropsUtil.getMap(configProps, prefix);
	}
}
