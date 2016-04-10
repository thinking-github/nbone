package org.springframework.boot.web.test;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class User implements Serializable {
	
	/**
	 * 
	 */
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
	
	public User() {
	}

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
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
