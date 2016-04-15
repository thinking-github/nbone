package org.nbone.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.nbone.test.domain.User;

public class UserTest {
	
	public static int count = 0;
	private String id;
	private String userName;
	private String sex;
	private int age;
	private UserTest parent;
	
	private long lll;
	private Date dates = new  Date();
	private Timestamp times = new Timestamp(dates.getTime());
	
	private User user1 ;
	
	private List<String> stringlist;
	
	static UserTest root =  new UserTest("root","ParnetName");
	
	
	
	
	public long getLll() {
		return lll;
	}
	public void setLll(long lll) {
		this.lll = lll;
	}
	public UserTest(String id, String userName) {
		this(id, userName, null);
	}
	
	public UserTest(String id, String userName,UserTest parent) {
		this.id = id;
		this.userName = userName;
		this.parent = parent;
	}
	public UserTest() {
		count ++;
		System.out.println("UserTestCount---"+count);
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
	
	
	public static UserTest getUser(String id) {
		return new UserTest(id,"hhhhhChen");
	}
	
	public static UserTest getUserAndParent(String id) {
		return new UserTest(id,"hhhhhChen",root);
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
/*	public Timestamp getTimes() {
		return times;
	}
	public void setTimes(Timestamp times) {
		this.times = times;
	}*/
	public User getUser() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public List<String> getStringlist() {
		return stringlist;
	}
	public void setStringlist(List<String> stringlist) {
		this.stringlist = stringlist;
	}

	
	public static void main(String[] args) {
		
		UserTest user  = UserTest.getUser("001");
		
		System.out.println(user);
		
		System.out.println(ToStringBuilder.reflectionToString(user,ToStringStyle.MULTI_LINE_STYLE));
		
		
		
	}

}
