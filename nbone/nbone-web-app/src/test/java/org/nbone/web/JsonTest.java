package org.nbone.web;

import java.io.IOException;
import java.util.List;

import org.nbone.test.domain.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


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
