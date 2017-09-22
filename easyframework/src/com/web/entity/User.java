package com.web.entity;

import java.io.Serializable;

import org.easy.framekwork.orm.annotation.Column;
import org.easy.framekwork.orm.annotation.Table;

@Table("user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827257616126437401L;
	@Column("id")
	private String id;
	@Column("name")
	private String name;
	@Column("email")
	private String email;
	@Column("phone")
	private String phone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
