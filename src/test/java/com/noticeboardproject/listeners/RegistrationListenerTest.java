package com.noticeboardproject.listeners;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.events.OnRegistrationCompleteEvent;
import com.noticeboardproject.services.UserService;

public class RegistrationListenerTest {
	
	@Mock
	MessageSource messages;
	
	@Mock
	UserService userService;
	
	@Mock
	JavaMailSender mailSender;
	
	@InjectMocks
	RegistrationListener registrationListener = new RegistrationListener();
	
	OnRegistrationCompleteEvent onRegistrationCompleteEvent;
	
	private final String APPURL = "appURL";
	private final String EMAIL = "email";
	private final String MESSAGE = "You registered successfully";
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void givenEvent_whenRegistration_thenCreateTokenAndSendEmail() {
		//given
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		onRegistrationCompleteEvent = new OnRegistrationCompleteEvent(userCommand, Locale.ENGLISH, APPURL);
		
		doNothing().when(userService).createVerificationToken(any(), any());
		when(messages.getMessage(anyString(), any(), any(Locale.class))).thenReturn(MESSAGE);
		doNothing().when(mailSender).send(any(SimpleMailMessage.class));
		
		//when
		registrationListener.onApplicationEvent(onRegistrationCompleteEvent);
		
		//then
		verify(userService, timeout(1)).createVerificationToken(any(), any());
		verifyNoMoreInteractions(userService);
		verify(messages, timeout(1)).getMessage(anyString(), any(), any(Locale.class));
		verifyNoMoreInteractions(messages);
		verify(mailSender, timeout(1)).send(any(SimpleMailMessage.class));
		verifyNoMoreInteractions(mailSender);
	}
	
}
