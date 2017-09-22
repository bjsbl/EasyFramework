package test.tx;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DogProxy implements MethodInterceptor {

	public Object newProxy(Object target) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		enhancer.setClassLoader(target.getClass().getClassLoader());
		return enhancer.create();
	}

	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("start------------");
		Object rs = arg3.invokeSuper(arg0, arg2);
		System.out.println("end------------");
		return rs;
	}

}
