package org.coursera.capstone.server.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class UserController {

	private static final String USER_SVC = "/user";
	
	
	@RequestMapping(value = USER_SVC, method = RequestMethod.GET)
	public @ResponseBody Collection<String> getRole(Principal p) {
		
		Collection<String> roles = Lists.newArrayList();
		
		try {
			Authentication auth = (Authentication) p;
			for(GrantedAuthority role : auth.getAuthorities()) {
				roles.add(role.getAuthority());
			}
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		return roles;
	}
	
}
