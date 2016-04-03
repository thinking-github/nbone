package org.nbone.pojo;

import java.io.Serializable;

public class User implements Serializable {
	
	private String id;
	private String name;
	private String password;
	
	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public User() {
	}

	public String getId() {
		System.out.println("---------------------getid");
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
	
	public static  String getChen(){
		return "getchen==========================";
	}
	
	
	public static User getUser(){
		return getUser("userId0000000");
	}
	
	
	public static User getUser(String id ){
		User user  = new User();
		user.setId(id);
		user.setName("name:==test");
		return user;
	}
	
	
	
		

}
