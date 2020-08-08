package org.nbone.demo.common.domain;

import lombok.Data;
import org.nbone.persistence.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

@Data
public class User extends BaseEntity<User,String> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String password;
	private int age;
	private Date createDate;

	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public User(String id, String name,int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	public User() {

	}

	public static User getUser(){
		return getUser("userId0000000");
	}
	
	
	public static User getUser(String id ){
		User user  = new User();
		user.setId(id);
		user.setName("name:thinking");
		user.setAge(25);
		user.setCreateDate(new Date());
		return user;
	}
		

}
