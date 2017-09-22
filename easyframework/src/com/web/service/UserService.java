package com.web.service;

import java.util.List;

import org.easy.framekwork.ioc.annotation.Inject;
import org.easy.framekwork.orm.annotation.Service;
import org.easy.framekwork.orm.annotation.Transaction;

import com.web.dao.UserDao;
import com.web.entity.User;

@Service
public class UserService {

	@Inject
	private UserDao userDao;

	@Transaction
	public void sayHello() {
		List<User> user = userDao.find("");
		System.out.println("sayHello");
		System.out.println(user);
	}
}
