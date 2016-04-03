package org.nbone.pojo;

import java.util.List;
import java.util.Map;

import org.nbone.mvc.dto.Range;

public class UserJson extends User { 

	 private Range range;
	 
	 private Map extMap;
	 
	 private List extList;
	
	 
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	
	
	public Map getExtMap() {
		return extMap;
	}
	public void setExtMap(Map extMap) {
		this.extMap = extMap;
	}
	
	
	public List getExtList() {
		return extList;
	}
	public void setExtList(List extList) {
		this.extList = extList;
	}
	public static UserJson getUserJson(){
		
		UserJson user = new UserJson();
		user.setId("001");
		user.setName("thinking");
		
		return user;
		
	}
	 
	 
}