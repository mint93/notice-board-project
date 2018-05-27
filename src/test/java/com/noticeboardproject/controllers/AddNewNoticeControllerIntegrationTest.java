package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.config.IntegrationTestConfig;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;
import com.noticeboardproject.storage.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(AddNewNoticeController.class)
public class AddNewNoticeControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	NoticeService noticeService;
	
	@MockBean
	UserService userService;
	
	@MockBean
	CategoryService categoryService;
	
	@MockBean
	StorageService storageService;
	
	@Before
	public void setUp() throws Exception {
		IntegrationTestConfig.setAuthenticationToken(SecurityContextHolder.getContext());
	}
	
	@Test
	public void showAddNewNoticeForm() throws Exception {
		User user = new User();
		when(userService.findUserByEmail(anyString())).thenReturn(user);
		
		mockMvc.perform(get("/addNewNotice")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().isOk())
		.andExpect(view().name("addNewNotice"))
		.andExpect(model().attributeExists("noticeCommand"))
		.andExpect(model().attribute("user", is(user)))
		.andExpect(model().attribute("categories", is(CategoryEnum.values())))
		.andExpect(model().hasNoErrors());
		
		verify(userService, times(1)).findUserByEmail(anyString());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void addNewNotice() throws Exception {
		User userSessionAttr = new User();
		doNothing().when(noticeService).saveNoticeCommand(any(NoticeCommand.class));
		
		mockMvc.perform(post("/addNewNotice")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("user", userSessionAttr)
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/"))
		.andExpect(model().attributeExists("noticeCommand", "user"))
		.andExpect(model().hasNoErrors());
		
		verify(noticeService, times(1)).saveNoticeCommand(any());
		verifyNoMoreInteractions(noticeService);
	}
	
}
