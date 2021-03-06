package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.noticeboardproject.config.IntegrationTestConfig;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.storage.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
public class IndexControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	MessageSource messages;
	
	private MessageSourceAccessor accessorMessages;
	
	@MockBean
	CategoryService categoryService;
	
	@MockBean
	StorageService storageService;
	
	@Before
	public void setUp() throws Exception {
		IntegrationTestConfig.setAuthenticationToken(SecurityContextHolder.getContext());
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