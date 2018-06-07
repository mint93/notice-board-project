package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;

public class NoticeControllerTest {
	
	MockMvc mockMvc;
	
	NoticeController noticeController;
	
	@Mock
	NoticeService noticeService;
	
	@Mock
	UserService userService;
	
	private final String EMAIL = "email@gmail.com";
	private final String TITLE = "title";	
	private final String DESCRIPTION = "description";	
	private final String PHONE_NUMBER = "+48123123123";	
	private final Integer PRICE = 10;	
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		//Solution for ServletException: Circular view path
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        
		noticeController = new NoticeController(noticeService, userService);
		mockMvc = MockMvcBuilders.standaloneSetup(noticeController)
				.setViewResolvers(viewResolver)
				.build();
	}
	
	@Test
	public void givenBlankTitleAndDescription_whenAddNewNotice_thenValidationFails() throws Exception {
		String blankTitle = " ";
		String blankDescription = " ";
		
		User user = new User();
		user.setEmail(EMAIL);
		
		mockMvc.perform(post("/notice/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", blankTitle)
				.param("description", blankDescription)
				.param("price", PRICE.toString())
				.param("phoneNumber", PHONE_NUMBER)
				.param("categoryEnumName", CategoryEnum.AUTOMOTIVE.toString())
				.sessionAttr("user", user))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
		.andExpect(model().attribute("noticeCommand", hasProperty("title", is(blankTitle))))
		.andExpect(model().attribute("noticeCommand", hasProperty("description", is(blankDescription))))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "title", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "description", "NotBlank"))
		.andExpect(model().attributeErrorCount("noticeCommand", 2))
		.andExpect(model().errorCount(2));
		
		verifyZeroInteractions(noticeService);
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void givenWrongPrice_whenAddNewNotice_thenValidationFails() throws Exception {
		Integer price = 0;
		
		User user = new User();
		user.setEmail(EMAIL);
		
		mockMvc.perform(post("/notice/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", TITLE)
				.param("description", DESCRIPTION)
				.param("price", price.toString())
				.param("phoneNumber", PHONE_NUMBER)
				.param("categoryEnumName", CategoryEnum.AUTOMOTIVE.toString())
				.sessionAttr("user", user))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
		.andExpect(model().attribute("noticeCommand", hasProperty("price", is(price))))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "price", "Min"))
		.andExpect(model().attributeErrorCount("noticeCommand", 1))
		.andExpect(model().errorCount(1));
		
		verifyZeroInteractions(noticeService);
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void givenWrongPhoneNumber_whenAddNewNotice_thenValidationFails() throws Exception {
		String phoneNumber = "+4812343121";
		
		User user = new User();
		user.setEmail(EMAIL);
		
		mockMvc.perform(post("/notice/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", TITLE)
				.param("description", DESCRIPTION)
				.param("price", PRICE.toString())
				.param("phoneNumber", phoneNumber)
				.param("categoryEnumName", CategoryEnum.AUTOMOTIVE.toString())
				.sessionAttr("user", user))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
		.andExpect(model().attribute("noticeCommand", hasProperty("phoneNumber", is(phoneNumber))))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "phoneNumber", "Pattern"))
		.andExpect(model().attributeErrorCount("noticeCommand", 1))
		.andExpect(model().errorCount(1));
		
		verifyZeroInteractions(noticeService);
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void givenEmptyCategory_whenAddNewNotice_thenValidationFails() throws Exception {
		
		User user = new User();
		user.setEmail(EMAIL);
		
		mockMvc.perform(post("/notice/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", TITLE)
				.param("description", DESCRIPTION)
				.param("price", PRICE.toString())
				.param("phoneNumber", PHONE_NUMBER)
				.flashAttr("categoryEnumName", "")
				.sessionAttr("user", user))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
		.andExpect(model().attribute("noticeCommand", hasProperty("categoryEnumName", is(nullValue()))))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "categoryEnumName", "NotEmpty"))
		.andExpect(model().attributeErrorCount("noticeCommand", 1))
		.andExpect(model().errorCount(1));
		
		verifyZeroInteractions(noticeService);
		verifyZeroInteractions(userService);
	}
}

