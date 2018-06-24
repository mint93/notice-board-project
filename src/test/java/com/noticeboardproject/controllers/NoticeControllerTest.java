package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.SearchBy;
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
	private final String CITY = "city";	
	private final String STATE = "state";	
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
				.param("city", CITY)
				.param("state", STATE)
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
	public void givenBlankCityAndState_whenAddNewNotice_thenValidationFails() throws Exception {
		String blankCity = " ";
		String blankState = " ";
		
		User user = new User();
		user.setEmail(EMAIL);
		
		mockMvc.perform(post("/notice/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", TITLE)
				.param("description", DESCRIPTION)
				.param("price", PRICE.toString())
				.param("phoneNumber", PHONE_NUMBER)
				.param("categoryEnumName", CategoryEnum.AUTOMOTIVE.toString())
				.param("city", blankCity)
				.param("state", blankState)
				.sessionAttr("user", user))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/addNewNotice"))
		.andExpect(model().attribute("noticeCommand", hasProperty("city", is(blankCity))))
		.andExpect(model().attribute("noticeCommand", hasProperty("state", is(blankState))))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "city", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("noticeCommand", "state", "NotBlank"))
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
				.param("city", CITY)
				.param("state", STATE)
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
				.param("city", CITY)
				.param("state", STATE)
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
				.param("city", CITY)
				.param("state", STATE)
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
	
	@Test
	public void givenSearchTerm_whenSearchNotices_thenShowListNoticesView() throws Exception {
		Notice notice1 = new Notice();
		Notice notice2 = new Notice();
		List<Notice> noticesList = new ArrayList<>();
		noticesList.add(notice1);
		noticesList.add(notice2);
		Page<Notice> notices = new PageImpl<>(noticesList);
		SearchBy searchBy = new SearchBy();
		searchBy.setTitle(TITLE);
		searchBy.setCity(CITY);
		
		when(noticeService.findBySearchTerm(any(), any())).thenReturn(notices);
		
		mockMvc.perform(get("/search")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("page", "1")
				.param("category", CategoryEnum.AUTOMOTIVE.toString())
				.flashAttr("searchBy", searchBy))
		.andExpect(status().isOk())
		.andExpect(view().name("notice/listNotices"))
		.andExpect(model().attribute("notices", is(notices)))
		.andExpect(model().attributeExists("currentPage"))
		.andExpect(model().attribute("searchBy", hasProperty("title", is(TITLE))))
		.andExpect(model().attribute("searchBy", hasProperty("city", is(CITY))))
		.andExpect(model().attribute("searchBy", hasProperty("category", is(CategoryEnum.AUTOMOTIVE))));	
		
		verify(noticeService, times(1)).findBySearchTerm(any(), any());
		verifyNoMoreInteractions(noticeService);
	}
}

