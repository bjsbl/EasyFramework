package test.tx;

import java.lang.reflect.Proxy;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Person p = new Person();
		p.say();
		PersonHandler handler = new PersonHandler(p);
		Animal obj = (Animal) Proxy.newProxyInstance(p.getClass().getClassLoader(), p.getClass().getInterfaces(), handler);
		obj.say();
		Dog d1 = new Dog();
		d1.eat();
		Dog d2 = (Dog) new DogProxy().newProxy(d1);
		d2.eat();
	}

}
