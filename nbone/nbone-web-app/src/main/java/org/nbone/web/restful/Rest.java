package org.nbone.web.restful;

import org.nbone.test.domain.User;
import org.springframework.web.client.RestTemplate;

public class Rest {
	
	
	public static void main(String[] args) {
		//delete();
		put();
	}

	
	static void put(){
		//put
		RestTemplate rest = new RestTemplate();
		User user = new User("001","thinking");
		rest.put("http://localhost:8080/user/22",user,"id","001");

		
	}
	
	static void delete(){
		//delete
		RestTemplate rest = new RestTemplate();
		rest.delete("http://localhost:8080/user/22");
		
	}
}
