package test.tx;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PersonHandler implements InvocationHandler {

	private Person p;

	public PersonHandler(Person p) {
		super();
		this.p = p;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("start------------");
		Object rs = method.invoke(p, args);
		System.out.println("end------------");
		return rs;
	}

}
