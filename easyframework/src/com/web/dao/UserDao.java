package com.web.dao;

import java.util.List;

import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.ioc.annotation.Bean;
import org.easy.framekwork.orm.sql.DBAccessor;
import org.easy.framekwork.orm.sql.SqlBuilder;

import com.web.entity.User;

@Bean
public class UserDao {

	private static final DBAccessor dBAccessor = InstanceFactory.getDBAccessor();

	public List<User> find(String id) {
		List<User> users = (List<User>) dBAccessor.queryEntityList(User.class, SqlBuilder.generateSelect(User.class, "", ""), null);
		return users;
	}
}
