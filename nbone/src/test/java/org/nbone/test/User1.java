package org.nbone.test;

public class User1 {
	public static int count = 0;
	private String id;
	private String userName;
	
	public User1() {
		count++;
		System.out.println("User1Count---"+count);
	}
	
	public User1(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public static User1 getUser(String id) {
		return new User1(id,"User1111111111");
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
