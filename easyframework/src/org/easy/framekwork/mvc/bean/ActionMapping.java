package org.easy.framekwork.mvc.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

public class ActionMapping {

	private String requestMethod;
	private String requestPath;
	private Class<?> actionClass;
	private Method actionMethod;
	private Matcher requestPathMatcher;

	public ActionMapping(Class<?> actionClass, Method actionMethod, String requestMethod, String requestPath) {
		super();
		this.actionClass = actionClass;
		this.actionMethod = actionMethod;
		this.requestMethod = requestMethod;
		this.requestPath = requestPath;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public Class<?> getActionClass() {
		return actionClass;
	}

	public void setActionClass(Class<?> actionClass) {
		this.actionClass = actionClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method actionMethod) {
		this.actionMethod = actionMethod;
	}

	public Matcher getRequestPathMatcher() {
		return requestPathMatcher;
	}

	public void setRequestPathMatcher(Matcher requestPathMatcher) {
		this.requestPathMatcher = requestPathMatcher;
	}

}
