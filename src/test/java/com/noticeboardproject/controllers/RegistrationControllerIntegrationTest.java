package com.noticeboardproject.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.config.IntegrationTestConfig;
import com.noticeboardproject.domain.PasswordResetToken;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.listeners.RegistrationListener;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.services.SecurityUserService;
import com.noticeboardproject.services.UserService;
import com.noticeboardproject.storage.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private JavaMailSender mailSender;
	
	@MockBean
	RegistrationListener registrationListener;
	
	@Autowired
	MessageSource messages;
	
	private MessageSourceAccessor accessorMessages;
	
	@MockBean
	SecurityUserService securityUserService;
	
	@MockBean
	StorageService storageService;
	
	@Before
	public void setUp() throws Exception {
		IntegrationTestConfig.setAuthenticationToken(SecurityContextHolder.getContext());
		accessorMessages = new MessageSourceAccessor(messages, Locale.ENGLISH);
	}
	
	@Test
	public void registrationConfirm_ExpirationTimePassed() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		VerificationToken verificationToken = new VerificationToken("1234", user);
		verificationToken.setExpiryDate(new Date(0L));
		
		when(userService.getVerificationToken(any(String.class))).thenReturn(verificationToken);
		
		mockMvc.perform(get("/user/registrationConfirm")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "sometoken")
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:badUser"))
		.andExpect(model().attribute("message", is(accessorMessages.getMessage("auth.message.expired"))))
		.andExpect(model().attribute("expired", is(true)))
		.andExpect(model().attribute("token", is(verificationToken.getToken())))
		.andExpect(model().hasNoErrors());
		
		assertEquals(false, user.isEnabled());
		verify(userService, times(1)).getVerificationToken(any());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void resendRegistrationTokenTest() throws Exception {
		
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken("token");
        verificationToken.setUser(user);
		
		when(userService.generateNewVerificationToken(any(String.class))).thenReturn(verificationToken);
		when(userService.getUser(any(String.class))).thenReturn(user);
		doNothing().when(mailSender).send(any(SimpleMailMessage.class));
		
		mockMvc.perform(get("/user/resendRegistrationToken")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "token1234"))
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("message", is("resendRegistrationToken")));
		
		verify(userService, times(1)).generateNewVerificationToken(any());
		verify(userService, times(1)).getUser(any());
		verifyNoMoreInteractions(userService);
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
		verifyNoMoreInteractions(mailSender);
	}
	
	@Test
	public void givenExistingEmail_whenResetPassword_thenSendEmail() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		
		
		when(userService.findUserByEmail(any(String.class))).thenReturn(user);
		doNothing().when(userService).createPasswordResetTokenForUser(any(User.class), any(String.class));
		doNothing().when(mailSender).send(any(SimpleMailMessage.class));
		
		mockMvc.perform(post("/user/resetPassword")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "existingEmail@gmail.com")
				.param("password", "pass")
				.param("matchingPassword", "pass"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("message", is("resetPassword")))
		.andExpect(model().attribute("user", is(user)))
		.andExpect(model().errorCount(0));
		
		verify(userService, times(1)).findUserByEmail(any());
		verify(userService, times(1)).createPasswordResetTokenForUser(any(), any());
		verifyNoMoreInteractions(userService);
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
		verifyNoMoreInteractions(mailSender);
	}
	
	@Test
	public void registrationConfirm_NullToken() throws Exception {
		when(userService.getVerificationToken(any(String.class))).thenReturn(null);
		
		mockMvc.perform(get("/user/registrationConfirm")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("token", "sometoken"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:badUser"))
		.andExpect(model().attribute("message", is(accessorMessages.getMessage("auth.message.invalidToken"))))
		.andExpect(model().hasNoErrors());
		
		verify(userService, times(1)).getVerificationToken(any());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void givenNotMatchingPassword_whenRegisterUser_thenValidationFails() throws Exception, EmailExistsException {
		String email = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password";
		
		mockMvc.perform(post("/user/registration")
				.with(csrf())
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
	public void givenExistingEmail_whenRegisterUser_thenValidationFails() throws Exception, EmailExistsException {
		String exisitngEmail = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		when(userService.registerNewUserCommand(any(UserCommand.class))).thenThrow(EmailExistsException.class);
		
		mockMvc.perform(post("/user/registration")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", exisitngEmail)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/registration"))
		.andExpect(model().attribute("user", hasProperty("email", is(exisitngEmail))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attributeHasFieldErrorCode("user", "email", accessorMessages.getMessage("message.emailExists")))
		.andExpect(model().attributeErrorCount("user", 1))
		.andExpect(model().errorCount(1));
		
		verify(userService, times(1)).registerNewUserCommand(any(UserCommand.class));
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void givenCorrectUser_whenRegisterUser_thenUserRegisteredAndNoValidationErrors() throws EmailExistsException, Exception {
		String email = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(email);
		userCommand.setPassword(password);
		userCommand.setMatchingPassword(password);
		
		when(userService.registerNewUserCommand(any(UserCommand.class))).thenReturn(userCommand);
		doNothing().when(registrationListener).onApplicationEvent(any());
		
		mockMvc.perform(post("/user/registration")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("user", hasProperty("email", is(email))))
		.andExpect(model().attribute("user", hasProperty("password", is(password))))
		.andExpect(model().attribute("user", hasProperty("matchingPassword", is(matchingPassword))))
		.andExpect(model().attribute("message", is("userRegistered")))
		.andExpect(model().attributeHasNoErrors("user"))
		.andExpect(model().errorCount(0));
		
		verify(userService, times(1)).registerNewUserCommand(any(UserCommand.class));
		verifyNoMoreInteractions(userService);
		verify(registrationListener, times(1)).onApplicationEvent(any());
	}
	
	@Test
	public void givenCorrectIdAndToken_whenChangePassword_thenShowUpdatePasswordView() throws Exception{
		User user = new User();
		user.setEmail("email@gmail.com");
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(user);

		UserCommand userCommandExpected = new UserCommand();
		userCommandExpected.setEmail(user.getEmail());
		
		when(securityUserService.validatePasswordResetToken(any(Long.class), any(String.class))).thenReturn(null);
		when(securityUserService.getPasswordResetTokenByToken(any(String.class))).thenReturn(passwordResetToken);
		
		mockMvc.perform(get("/user/changePassword")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "0")
				.param("token", "token1234"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("updatePassword"))
		.andExpect(flash().attribute("user", hasProperty("email", is(userCommandExpected.getEmail()))));
		
		verify(securityUserService, times(1)).validatePasswordResetToken(anyLong(), anyString());
		verify(securityUserService, times(1)).getPasswordResetTokenByToken(anyString());
		verifyNoMoreInteractions(securityUserService);
	}
	
	@Test
	public void givenNotCorrectIdOrToken_whenChangePassword_thenShowUpdatePasswordView() throws Exception{
		when(securityUserService.validatePasswordResetToken(any(Long.class), any(String.class))).thenReturn("invalidToken");
		
		mockMvc.perform(get("/user/changePassword")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "0")
				.param("token", "token1234"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("badToken"))
		.andExpect(flash().attribute("message", is(accessorMessages.getMessage("auth.message.invalidToken"))));
		
		verify(securityUserService, times(1)).validatePasswordResetToken(anyLong(), anyString());
		verifyNoMoreInteractions(securityUserService);
	}
	
	@Test
	public void givenMatchingPassword_whenSavePassword_thenReturnSuccessRegisterView() throws Exception {
		String email = "email@gmail.com";
		String password = "password1234";
		String matchingPassword = "password1234";
		
		User user = new User();
		user.setEmail(email);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"))));
		
		doNothing().when(userService).changeUserPassword(any(), any());
		
		mockMvc.perform(post("/user/savePassword")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password)
				.param("matchingPassword", matchingPassword))
		.andExpect(status().isOk())
		.andExpect(view().name("user/successRegister"))
		.andExpect(model().attribute("message", is("passwordUpdated")))
		.andExpect(model().hasNoErrors());
		
		verify(userService, times(1)).changeUserPassword(any(User.class), anyString());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void givenLoggedUser_whenChangePassword_thenShowUpdatePasswordView() throws Exception{
		User user = new User();
		user.setEmail("email@gmail.com");
		
		when(userService.findUserByEmail(anyString())).thenReturn(user);
		
		mockMvc.perform(get("/user/changePasswordForLoggedUser")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
		.andExpect(status().isOk())
		.andExpect(view().name("user/updatePassword"))
		.andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))));
		
		verify(userService, times(1)).findUserByEmail(anyString());
		verifyNoMoreInteractions(userService);
	}
}
