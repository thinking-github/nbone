package org.nbone.demo.spring.mvc;

import org.nbone.demo.common.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring MVC 传统使用模式
 * @author thinking
 *
 */
@RestController
@RequestMapping("user")
public class UserRestController {


	@PostMapping(value = "add")
	public String add(User user){
		return "index";
	}


	@RequestMapping("update")
	public String update(User user){
		return "index";
	}


}
