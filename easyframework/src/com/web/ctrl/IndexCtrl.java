package com.web.ctrl;

import org.easy.framekwork.ioc.annotation.Inject;
import org.easy.framekwork.mvc.annotation.Action;
import org.easy.framekwork.mvc.annotation.Request;
import org.easy.framekwork.mvc.bean.Result;
import org.easy.framekwork.mvc.bean.View;
import org.easy.framekwork.orm.sql.SqlBuilder;

import com.web.entity.User;
import com.web.service.UserService;

@Action(path = "/test")
public class IndexCtrl {

	@Inject
	private UserService userService;

	@Request.Get(value = "/")
	public Result index() {
		Result rt = new Result(true);
		rt.setData("hello world");
		userService.sayHello();
		return rt;
	}

	@Request.Get(value = "/t")
	public String test() {
		return "nick : " + SqlBuilder.generateSelect(User.class, " name>10 ", "");
	}

	@Request.Get(value = "/e")
	public View sec() {
		View view = new View("index.jsp");
		return view;
	}
}
