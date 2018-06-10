package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;
import com.noticeboardproject.storage.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(NoticeController.class)
public class NoticeControllerIntegrationTest {

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
	
	private final String EMAIL = "email@gmail.com";
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";
	private final Integer PRICE = 10;
	private final String PHONE_NUMBER = "+48123123123";
	
	@Before
	public void setUp() throws Exception {
		IntegrationTestConfig.setAuthenticationToken(SecurityContextHolder.getContext());
	}
	
	@Test
	public void showAddNewNoticeForm() throws Exception {
		User user = new User();
		when(userService.findUserByEmail(anyString())).thenReturn(user);
		
		mockMvc.perform(get("/notice/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
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
		Notice savedNotice = new Notice();
		savedNotice.setId(1L);
		when(noticeService.saveNoticeCommand(any(NoticeCommand.class))).thenReturn(savedNotice);
		
		mockMvc.perform(post("/notice/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", TITLE)
				.param("description", DESCRIPTION)
				.param("price", PRICE.toString())
				.param("phoneNumber", PHONE_NUMBER)
				.param("categoryEnumName", CategoryEnum.AUTOMOTIVE.toString())
				.sessionAttr("user", userSessionAttr)
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/notice/" + savedNotice.getId() + "/show"))
		.andExpect(model().attributeExists("noticeCommand", "user"))
		.andExpect(model().hasNoErrors());
		
		verify(noticeService, times(1)).saveNoticeCommand(any());
		verifyNoMoreInteractions(noticeService);
	}
	
	@Test
	public void showNoticeById() throws Exception {
		User user = new User();
		user.setEmail(EMAIL);
		Notice notice = new Notice();
		notice.setViews(0);
		notice.setUser(user);
		notice.setMainImage(null);
		when(noticeService.findById(any())).thenReturn(notice);
		when(noticeService.saveOrUpdateNotice(any())).thenReturn(notice);
		
		mockMvc.perform(get("/notice/1/show")
			.with(csrf())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/showNotice"))
		.andExpect(model().attribute("notice", is(notice)));
		
		verify(noticeService, times(1)).findById(1L);
		verify(noticeService, times(1)).saveOrUpdateNotice(notice);
		verifyNoMoreInteractions(noticeService);
	}
	
}
