package org.nbone.pojo;

public class UserDto  extends User{
	
	private String id;
	private String name;
	private String password;
	
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
	
	
	public static UserDto getUser(){
		return getUser("userId0000000");
	}
	
	
	public static UserDto getUser(String id ){
		UserDto user  = new UserDto();
		user.setId(id);
		user.setName("name:==test");
         //user.setPassword("00012");
		return user;
	}
	
	
	
		

}
