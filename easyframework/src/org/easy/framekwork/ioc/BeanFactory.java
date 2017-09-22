package org.easy.framekwork.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easy.framekwork.core.ClassScanner;
import org.easy.framekwork.core.InitializationError;
import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.ioc.annotation.Bean;
import org.easy.framekwork.ioc.annotation.Inject;
import org.easy.framekwork.mvc.annotation.Action;
import org.easy.framekwork.orm.TxManager;
import org.easy.framekwork.orm.annotation.Service;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class BeanFactory {

	private static final Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
	private static final String basePackage = "com";
	private static final ClassScanner scanner = InstanceFactory.getClassScanner();

	static {
		try {
			List<Class<?>> classList = scanner.getClassListByAnnotation(basePackage, Action.class);
			List<Class<?>> serviceList = scanner.getClassListByAnnotation(basePackage, Service.class);
			List<Class<?>> beanList = scanner.getClassListByAnnotation(basePackage, Bean.class);
			for (Class<?> service : serviceList) {
				Object serviceProxy = Enhancer.create(service, new MethodInterceptor() {
					@Override
					public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
						// TODO Auto-generated method stub
						return TxManager.class.newInstance().doTransaction(targetMethod, methodProxy, targetObject, methodParams);
					}
				});
				beanMap.put(service, serviceProxy);
			}
			for (Class<?> cls : classList) {
				Object beanInstance = cls.newInstance();
				beanMap.put(cls, beanInstance);
			}
			for (Class<?> cls : beanList) {
				Object beanInstance = cls.newInstance();
				beanMap.put(cls, beanInstance);
			}
		} catch (Exception e) {
			throw new InitializationError("初始化 BeanFactory 出错！", e);
		}
		try {
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				Field[] beanFields = beanClass.getDeclaredFields();
				if (beanFields != null && beanFields.length > 0) {
					for (Field beanField : beanFields) {
						if (beanField.isAnnotationPresent(Inject.class)) {
							Object instance = beanMap.get(beanField.getType());
							if (instance != null) {
								System.out.println("Inject :" + beanInstance + "/" + instance);
								beanField.setAccessible(true);
								beanField.set(beanInstance, instance);
							} else {
								throw new InitializationError("注入失败,类名：" + beanClass.getSimpleName() + "，字段名：" + beanField.getName());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new InitializationError("初始化对象注入失败 " + e.getMessage(), e);
		}
	}

	public static Map<Class<?>, Object> getBeanMap() {
		return beanMap;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls) {
		if (!beanMap.containsKey(cls)) {
			throw new RuntimeException("根据类名无法获取实例" + cls);
		}
		return (T) beanMap.get(cls);
	}

	public static void setBean(Class<?> cls, Object obj) {
		beanMap.put(cls, obj);
	}

}
