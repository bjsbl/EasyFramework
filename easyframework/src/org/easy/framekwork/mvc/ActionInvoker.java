package org.easy.framekwork.mvc;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.ioc.BeanFactory;
import org.easy.framekwork.mvc.bean.ActionMapping;
import org.easy.framekwork.mvc.bean.Params;
import org.easy.framekwork.util.CodecUtil;
import org.easy.framekwork.util.StringUtil;

public class ActionInvoker {

	private ViewResolver viewResolver = InstanceFactory.getViewResolver();

	public void invokeHandler(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws Exception {
		Class<?> actionClass = mapping.getActionClass();
		Method actionMethod = mapping.getActionMethod();
		Object actionInstance = BeanFactory.getBean(actionClass);
		List<Object> actionMethodParamList = createActionMethodParamList(request, mapping);
		checkParamList(actionMethod, actionMethodParamList);
		Object actionMethodResult = actionMethod.invoke(actionInstance, actionMethodParamList.toArray());
		viewResolver.resolveView(request, response, actionMethodResult);
	}

	private void checkParamList(Method actionMethod, List<Object> actionMethodParamList) {
		Class<?>[] actionMethodParameterTypes = actionMethod.getParameterTypes();
		if (actionMethodParameterTypes.length != actionMethodParamList.size()) {
			String message = String.format("因为参数个数不匹配，所以无法调用 Action 方法！原始参数个数：%d，实际参数个数：%d", actionMethodParameterTypes.length, actionMethodParamList.size());
			throw new RuntimeException(message);
		}
	}

	public List<Object> createActionMethodParamList(HttpServletRequest request, ActionMapping mapping) throws Exception {
		List<Object> paramList = new ArrayList<Object>();
		Class<?>[] actionParamTypes = mapping.getActionMethod().getParameterTypes();
		paramList.addAll(createPathParamList(mapping.getRequestPathMatcher(), actionParamTypes));
		Map<String, Object> requestParamMap = getRequestParamMap(request);
		if (requestParamMap != null && !requestParamMap.isEmpty()) {
			paramList.add(new Params(requestParamMap));
		}
		return paramList;
	}

	private Collection<? extends Object> createPathParamList(Matcher requestPathMatcher, Class<?>[] actionParamTypes) {
		// TODO Auto-generated method stub
		List<Object> paramList = new ArrayList<Object>();
		for (int i = 1; i <= requestPathMatcher.groupCount(); i++) {
			String param = requestPathMatcher.group(i);
			Class<?> paramType = actionParamTypes[i - 1];
			if (paramType.equals(Integer.class) || paramType.equals(int.class)) {
				paramList.add(Integer.valueOf(param));
			} else if (paramType.equals(Long.class) || paramType.equals(long.class)) {
				paramList.add(Long.valueOf(param));
			} else if (paramType.equals(Double.class) || paramType.equals(double.class)) {
				paramList.add(Double.valueOf(param));
			} else if (paramType.equals(String.class)) {
				paramList.add(param);
			}
		}
		return paramList;
	}

	public Map<String, Object> getRequestParamMap(HttpServletRequest request) {
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		try {
			String method = request.getMethod();
			if (method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")) {
				String queryString = CodecUtil.decodeURL(StringUtil.getRequestStream2String(request.getInputStream()));
				if (queryString != null) {
					String[] qsArray = queryString.split("&");
					if (qsArray != null && qsArray.length > 0) {
						for (String qs : qsArray) {
							String[] array = qs.split("=");
							if (array != null && array.length == 2) {
								String paramName = array[0];
								String paramValue = array[1];
								if (paramMap.containsKey(paramName)) {
									paramValue = paramMap.get(paramName) + File.pathSeparator + paramValue;
								}
								paramMap.put(paramName, paramValue);
							}
						}
					}
				}
			} else {
				Enumeration<String> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = paramNames.nextElement();
					String[] paramValues = request.getParameterValues(paramName);
					if (paramValues != null && paramValues.length > 0) {
						if (paramValues.length == 1) {
							paramMap.put(paramName, paramValues[0]);
						} else {
							StringBuilder paramValue = new StringBuilder();
							for (int i = 0; i < paramValues.length; i++) {
								paramValue.append(paramValues[i]);
								if (i != paramValues.length - 1) {
									paramValue.append(File.separator);
								}
							}
							paramMap.put(paramName, paramValue.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return paramMap;

	}
}
