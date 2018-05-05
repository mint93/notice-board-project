package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
public class IndexControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	MessageSource messages;
	
	private MessageSourceAccessor accessorMessages;
	
	
	@Before
	public void setUp() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
		accessorMessages = new MessageSourceAccessor(messages, Locale.ENGLISH);
	}
	
	@Test
	public void showIndexView() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attributeExists("states"))
			.andExpect(model().attribute("searchMessage", is(accessorMessages.getMessage("index.placeholder.search"))));
	}
}