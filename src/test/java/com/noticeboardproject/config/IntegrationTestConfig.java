package com.noticeboardproject.config;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import com.noticeboardproject.domain.User;

public class IntegrationTestConfig {

	private IntegrationTestConfig() {};
	
	final static String EMAIL = "email@gmail.com";

	public static void setAuthenticationToken(SecurityContext securityContext) {
		User user = new User();
		user.setEmail(EMAIL);
	    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
	}
}
