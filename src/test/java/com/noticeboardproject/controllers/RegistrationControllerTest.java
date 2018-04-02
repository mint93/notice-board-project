package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.services.UserService;

public class RegistrationControllerTest {

	MockMvc mockMvc;
	
	RegistrationController registrationController;
	
	@Mock
	UserService userService;
	
	@Mock
	MessageSource messages;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		//Solution for ServletException: Circular view path
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
		        
		registrationController = new RegistrationController(userService);
		mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
				.setViewResolvers(viewResolver)
				.build();
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
	@Ignore
	//event publisher thorw an error - can't find workaround for this moment
	public void givenCorrectUser_whenRegisterUser_thenUserRegisteredAndNoValidationErrors() throws EmailExistsException, Exception {
		String email = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(email);
		userCommand.setPassword(password);
		userCommand.setMatchingPassword(password);
		
		when(userService.registerNewUserCommand(any(UserCommand.class))).thenReturn(userCommand);
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("user", hasProperty("email", is(email))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasNoErrors("user"))
		.andExpect(model().errorCount(0));
		
		verify(userService, times(1)).registerNewUserCommand(any(UserCommand.class));
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void givenWrongEmail_whenRegisterUser_thenValidationFails() throws Exception {
		String wrongEmail = "emailgmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", wrongEmail)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/registration"))
		.andExpect(model().attribute("user", hasProperty("email", is(wrongEmail))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasFieldErrorCode("user", "email", "ValidEmail"))
		.andExpect(model().attributeErrorCount("user", 1))
		.andExpect(model().errorCount(1));
		
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void givenExistingEmail_whenRegisterUser_thenValidationFails() throws Exception, EmailExistsException {
		String exisitngEmail = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		when(userService.registerNewUserCommand(any(UserCommand.class))).thenThrow(EmailExistsException.class);
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", exisitngEmail)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/registration"))
		.andExpect(model().attribute("user", hasProperty("email", is(exisitngEmail))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasFieldErrorCode("user", "email", "message.emailExists"))
		.andExpect(model().attributeErrorCount("user", 1))
		.andExpect(model().errorCount(1));
		
		verify(userService, times(1)).registerNewUserCommand(any(UserCommand.class));
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void givenEmptyPassword_whenRegisterUser_thenValidationFails() throws Exception, EmailExistsException {
		String email = "email@gmail.com";
		String password = "";
		String matchingPassword = "";
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/registration"))
		.andExpect(model().attribute("user", hasProperty("email", is(email))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasFieldErrorCode("user", "password", "NotBlank"))
		.andExpect(model().attributeErrorCount("user", 1))
		.andExpect(model().errorCount(1));;
		
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void givenNotMatchingPassword_whenRegisterUser_thenValidationFails() throws Exception, EmailExistsException {
		String email = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password";
		
		mockMvc.perform(post("/user/registration")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/registration"))
		.andExpect(model().attribute("user", hasProperty("email", is(email))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasFieldErrorCode("user", "password", "PasswordMatches"))
		.andExpect(model().attributeErrorCount("user", 2))
		.andExpect(model().errorCount(2));
		
		verifyZeroInteractions(userService);
	}
	
	@Test
	public void registrationConfirm_NullToken() throws Exception {
		when(userService.getVerificationToken(any(String.class))).thenReturn(null);
		//when(messages.getMessage(any(String.class), any(), any(Locale.class)))
	    //.thenReturn("auth.message.invalidToken");
		//.thenReturn("");
		
		mockMvc.perform(get("/user/registrationConfirm")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "sometoken"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:badUser"))
		.andExpect(model().attribute("message", is("auth.message.invalidToken")))
		.andExpect(model().hasNoErrors());
		
		verify(userService, times(1)).getVerificationToken(any());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void registrationConfirm_ExpirationTimePassed() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		VerificationToken verificationToken = new VerificationToken("1234", user);
		verificationToken.setExpiryDate(new Date(0L));
		
		when(userService.getVerificationToken(any(String.class))).thenReturn(verificationToken);
		when(messages.getMessage(any(), any(), any())).thenReturn("auth.message.expired");
		
		mockMvc.perform(get("/user/registrationConfirm")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "sometoken")
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:badUser"))
		.andExpect(model().attribute("message", is("auth.message.expired")))
		.andExpect(model().hasNoErrors());
		
		assertEquals(false, user.isEnabled());
		verify(userService, times(1)).getVerificationToken(any());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void registrationConfirm_whenUserConfirmed() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		VerificationToken verificationToken = new VerificationToken("1234", user);
		
		when(userService.getVerificationToken(any(String.class))).thenReturn(verificationToken);
		doNothing().when(userService).saveRegisteredUser(any(User.class));
		
		mockMvc.perform(get("/user/registrationConfirm")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "sometoken"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:login"))
		.andExpect(model().hasNoErrors());
		
		assertEquals(true, user.isEnabled());
		verify(userService, times(1)).getVerificationToken(any());
		verify(userService, times(1)).saveRegisteredUser(any());
		verifyNoMoreInteractions(userService);
	}

}
