package org.nbone.util.bean;


import org.apache.commons.beanutils.PropertyUtils;
import org.nbone.pojo.User;
import org.nbone.pojo.UserDto;
import org.springframework.beans.BeanUtils;

public class BeanTest {

	public static void main(String[] args) {
		try {
			
			UserDto dto = UserDto.getUser("001");
			
			User user = new User();
			user.setPassword("9999");
			
			//BeanUtils.copyProperties(dto, user);
			
			//org.apache.commons.beanutils.BeanUtils.copyProperties(user, dto);
			
			PropertyUtils.copyProperties(user, dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
