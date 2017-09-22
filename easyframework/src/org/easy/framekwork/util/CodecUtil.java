package org.easy.framekwork.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.easy.framekwork.Constant;

public class CodecUtil {

	public static String encodeURL(String str) {
		String target;
		try {
			target = URLEncoder.encode(str, Constant.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	public static String decodeURL(String str) {
		String target;
		try {
			target = URLDecoder.decode(str, Constant.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}
}
