package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.services.UserService;

public class RegistrationControllerTest {

	MockMvc mockMvc;
	
	RegistrationController registrationController;
	
	@Mock
	UserService userService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		//Solution for ServletException: Circular view path
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
		
		registrationController = new RegistrationController(userService);
		mockMvc = MockMvcBuilders.standaloneSetup(registrationController).setViewResolvers(viewResolver).build();
	}

	@Test
	public void showRegistrationFrom() throws Exception {
		mockMvc.perform(get("/user/registration"))
			.andExpect(status().isOk())
			.andExpect(view().name("user/registration"))
			.andExpect(model().attributeExists("user"));
			
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void registerUserAccount() throws Exception {
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail("email");
		userCommand.setPassword("pass");
		userCommand.setMatchingPassword("pass");
		
		when(userService.registerNewUserCommand(any(UserCommand.class))).thenReturn(userCommand);
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "someemail")
				.param("password", "1234")
				.param("matchingPassword", "1234"))
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("user", hasProperty("email", is("email"))))
		.andExpect(model().attribute("user", hasProperty("password", is("pass"))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is("pass"))));
		
		verify(userService, times(1)).registerNewUserCommand(any(UserCommand.class));
		verifyNoMoreInteractions(userService);
	}
}
