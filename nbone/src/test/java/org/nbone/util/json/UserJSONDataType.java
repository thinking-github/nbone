package org.nbone.util.json;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 各种数据类型 转换实验
 * @author thinking
 *
 */
public class UserJSONDataType {
	private String id;
	private String name;
	private int age;
	private long lll;
	private double doubl;
	private float floa;
	private Date dates;
	private Timestamp times;
	
	private java.sql.Date sqlDate;
	
	private List<String> stringlist;
	
	
	
	
	public UserJSONDataType() {
	}


	public UserJSONDataType(String id, String name, int age, long lll,
			double doubl, float floa, Date dates, Timestamp times,
			List<String> stringlist) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.lll = lll;
		this.doubl = doubl;
		this.floa = floa;
		this.dates = dates;
		this.times = times;
		this.stringlist = stringlist;
	}
	
	public static UserJSONDataType getUser(String id ) {
		UserJSONDataType user = new UserJSONDataType();
		user.setAge(23);
		
		Date date = new Date();
		user.setDates(new Date());
		user.setDoubl(9.9);
		user.setFloa(8.8F);
		user.setId("id_"+id);
		user.setLll(100000);
		user.setName("name_ooo");
		user.setStringlist(null);
		user.setTimes(new Timestamp(date.getTime()));
		user.setSqlDate(new java.sql.Date(date.getTime()));
		
		
		
		return user;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getLll() {
		return lll;
	}
	public void setLll(long lll) {
		this.lll = lll;
	}
	public double getDoubl() {
		return doubl;
	}
	public void setDoubl(double doubl) {
		this.doubl = doubl;
	}
	public float getFloa() {
		return floa;
	}
	public void setFloa(float floa) {
		this.floa = floa;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
	public Timestamp getTimes() {
		return times;
	}
	public void setTimes(Timestamp times) {
		this.times = times;
	}
	public java.sql.Date getSqlDate() {
		return sqlDate;
	}


	public void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}


	public List<String> getStringlist() {
		return stringlist;
	}
	public void setStringlist(List<String> stringlist) {
		this.stringlist = stringlist;
	}
	
	
	
	
	

}
