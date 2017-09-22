package org.easy.framekwork.mvc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easy.framekwork.util.CodecUtil;

public class Context {

	private static final ThreadLocal<Context> contextContainer = new ThreadLocal<Context>();

	private HttpServletRequest request;
	private HttpServletResponse response;

	public static void init(HttpServletRequest request, HttpServletResponse response) {
		Context dataContext = new Context();
		dataContext.request = request;
		dataContext.response = response;
		contextContainer.set(dataContext);
	}

	public static void destroy() {
		contextContainer.remove();
	}

	public static Context getInstance() {
		return contextContainer.get();
	}

	public static HttpServletRequest getRequest() {
		return getInstance().request;
	}

	public static HttpServletResponse getResponse() {
		return getInstance().response;
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static javax.servlet.ServletContext getServletContext() {
		return getRequest().getServletContext();
	}

	public static class Request {

		public static void put(String key, Object value) {
			getRequest().setAttribute(key, value);
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(String key) {
			return (T) getRequest().getAttribute(key);
		}

		public static void remove(String key) {
			getRequest().removeAttribute(key);
		}

		public static Map<String, Object> getAll() {
			Map<String, Object> map = new HashMap<String, Object>();
			Enumeration<String> names = getRequest().getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				map.put(name, getRequest().getAttribute(name));
			}
			return map;
		}
	}

	public static class Response {

		public static void put(String key, Object value) {
			getResponse().setHeader(key, value.toString());
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(String key) {
			return (T) getResponse().getHeader(key);
		}

		public static Map<String, Object> getAll() {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String name : getResponse().getHeaderNames()) {
				map.put(name, getResponse().getHeader(name));
			}
			return map;
		}
	}

	public static class Session {
		public static void put(String key, Object value) {
			getSession().setAttribute(key, value);
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(String key) {
			return (T) getSession().getAttribute(key);
		}

		public static void remove(String key) {
			getSession().removeAttribute(key);
		}

		public static Map<String, Object> getAll() {
			Map<String, Object> map = new HashMap<String, Object>();
			Enumeration<String> names = getSession().getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				map.put(name, getSession().getAttribute(name));
			}
			return map;
		}

		public static void removeAll() {
			getSession().invalidate();
		}
	}

	public static class Cookie {

		public static void put(String key, Object value) {
			String strValue = value.toString();
			javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key, strValue);
			getResponse().addCookie(cookie);
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(String key) {
			T value = null;
			javax.servlet.http.Cookie[] cookieArray = getRequest().getCookies();
			if (cookieArray!=null && cookieArray.length>0) {
				for (javax.servlet.http.Cookie cookie : cookieArray) {
					if (key.equals(cookie.getName())) {
						value = (T) CodecUtil.decodeURL(cookie.getValue());
						break;
					}
				}
			}
			return value;
		}
	}

	public static class ServletContext {

		public static void put(String key, Object value) {
			getServletContext().setAttribute(key, value);
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(String key) {
			return (T) getServletContext().getAttribute(key);
		}

		public static void remove(String key) {
			getServletContext().removeAttribute(key);
		}

		public static Map<String, Object> getAll() {
			Map<String, Object> map = new HashMap<String, Object>();
			Enumeration<String> names = getServletContext().getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				map.put(name, getServletContext().getAttribute(name));
			}
			return map;
		}
	}

}
