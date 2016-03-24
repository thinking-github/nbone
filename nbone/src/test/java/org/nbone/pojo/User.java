package org.nbone.pojo;

public class User {
	
	private String id;
	private String name;
	
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
		System.out.println("======"+name);
		this.name = name;
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
