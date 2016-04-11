package org.nbone.web;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;
import org.nbone.test.domain.User;


public class JsonTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		
		String ss = "[{\"id\":\"001\",\"name\":\"thinking\",\"password\":\"hhhhh\",\"age\":0,\"createDate\":null},{\"id\":\"002\",\"name\":\"thinking\",\"password\":\"hhhhh\",\"age\":0,\"createDate\":null}]";
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference users = new TypeReference<List<User>>() {
		};
		
		Object object = mapper.readValue(ss, users);
		
		
		

	}

}
