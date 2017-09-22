package org.easy.framekwork.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.easy.framekwork.core.ClassScanner;
import org.easy.framekwork.core.Config;
import org.easy.framekwork.log.Logger;
import org.easy.framekwork.mvc.annotation.Action;
import org.easy.framekwork.mvc.annotation.Request;
import org.easy.framekwork.mvc.bean.ActionMapping;

public class ActionHandler {

	private static final Map<String, ActionMapping> actionMap = new LinkedHashMap<String, ActionMapping>();
	private static Logger logger = Logger.getLogger(ActionHandler.class);

	static {
		ClassScanner scanner = new ClassScanner();
		Map<String, ActionMapping> commonActionMap = new HashMap<String, ActionMapping>();
		String base = Config.getString("easy.framework.base_package");
		List<Class<?>> actionClassList = scanner.getClassListByAnnotation(base, Action.class);
		for (Class<?> actionClass : actionClassList) {
			Action action = actionClass.getAnnotation(Action.class);
			Method[] actionMethods = actionClass.getDeclaredMethods();
			if (actionMethods != null && actionMethods.length > 0) {
				for (Method actionMethod : actionMethods) {
					if (actionMethod.isAnnotationPresent(Request.Get.class)) {
						String requestPath = action.path() + actionMethod.getAnnotation(Request.Get.class).value();
						putActionMap("GET", requestPath, actionClass, actionMethod, commonActionMap);
					} else if (actionMethod.isAnnotationPresent(Request.Post.class)) {
						String requestPath = action.path() + actionMethod.getAnnotation(Request.Post.class).value();
						putActionMap("POST", requestPath, actionClass, actionMethod, commonActionMap);
					} else if (actionMethod.isAnnotationPresent(Request.Put.class)) {
						String requestPath = action.path() + actionMethod.getAnnotation(Request.Put.class).value();
						putActionMap("PUT", requestPath, actionClass, actionMethod, commonActionMap);
					} else if (actionMethod.isAnnotationPresent(Request.Delete.class)) {
						String requestPath = action.path() + actionMethod.getAnnotation(Request.Delete.class).value();
						putActionMap("DELETE", requestPath, actionClass, actionMethod, commonActionMap);
					}
				}
			}
		}
		actionMap.putAll(commonActionMap);
	}

	private static void putActionMap(String requestMethod, String requestPath, Class<?> actionClass, Method actionMethod, Map<String, ActionMapping> commonActionMap) {
		if (requestPath.lastIndexOf("/") == requestPath.length()-1) {
			requestPath = requestPath.substring(0, requestPath.length() - 1);
		}
		logger.info(String.format("easy:{%s} {%s} ---{%s} {%s}", requestMethod, requestPath, actionClass.getName(), actionMethod.getName()));
		commonActionMap.put(requestPath, new ActionMapping(actionClass, actionMethod, requestMethod, requestPath));
	}
	
	public static void main(String[] arg){
		String t = "/hello/index/";
		System.out.println(t.lastIndexOf("/")+"/"+(t.length()-1)+t.substring(0, t.length()-1));
	}

	public static ActionMapping getActionMapping(String method, String path) {
		ActionMapping mapping = null;
		Map<String, ActionMapping> actionMap = getActionMap();
		for (Map.Entry<String, ActionMapping> actionEntry : actionMap.entrySet()) {
			ActionMapping requester = actionEntry.getValue();
			String requestMethod = requester.getRequestMethod();
			String requestPath = requester.getRequestPath();
			Matcher requestPathMatcher = Pattern.compile(requestPath).matcher(path);
			if (requestMethod.equalsIgnoreCase(method) && requestPathMatcher.matches()) {
				mapping = actionEntry.getValue();
				if (mapping != null) {
					mapping.setRequestPathMatcher(requestPathMatcher);
				}
				break;
			}
		}
		return mapping;
	}

	public static Map<String, ActionMapping> getActionMap() {
		return actionMap;
	}
}
