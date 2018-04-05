package com.noticeboardproject.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.repositories.RoleRepository;
import com.noticeboardproject.repositories.UserRepository;
import com.noticeboardproject.repositories.VerificationTokenRepository;

public class UserServiceImplTest {

	private final String PASS = "pass";
	
	private final String ENCODEDPASS = "encodedPass";

	private final String EMAIL = "email";

	UserServiceImpl userServiceImpl;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	RoleRepository roleRepository;
	
	@Mock
	VerificationTokenRepository verificationTokenRepository;
	
	@Mock
	UserToUserCommand userToUserCommand;
	
	@Mock
	UserCommandToUser userCommandToUser;
	
	@Mock
	PasswordEncoder passwordEncoder;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userServiceImpl = new UserServiceImpl(userRepository, roleRepository, userToUserCommand, userCommandToUser, verificationTokenRepository, passwordEncoder);
	}

	@Test
	public void registerNewUserCommand() {
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		userCommand.setPassword(PASS);
		userCommand.setMatchingPassword(PASS);
		
		User savedUser = new User();
		savedUser.setEmail(EMAIL);
		savedUser.setPassword(ENCODEDPASS);
		
		User convertedUser = new User();
		convertedUser.setEmail(EMAIL);
		convertedUser.setPassword(PASS);
		
		UserCommand convertedUserCommand = new UserCommand();
		convertedUserCommand.setEmail(EMAIL);
		convertedUserCommand.setPassword(ENCODEDPASS);
		convertedUserCommand.setMatchingPassword(ENCODEDPASS);
		
		Role userRole = new Role();
		userRole.setUsers(new HashSet<User>());
		userRole.setRole("USER_ROLE");
		
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(roleRepository.findByRole(anyString())).thenReturn(userRole);
		when(userCommandToUser.convert(any(UserCommand.class))).thenReturn(convertedUser);
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(userToUserCommand.convert(any(User.class))).thenReturn(convertedUserCommand);
		when(passwordEncoder.encode(any())).thenReturn(ENCODEDPASS);
		
		UserCommand returendUserCommand=null;
		try {
			returendUserCommand = userServiceImpl.registerNewUserCommand(userCommand);
		} catch (EmailExistsException e) {
			e.printStackTrace();
			fail();
		}
		
		assertNotNull(returendUserCommand);
		assertArrayEquals(new String[] {EMAIL, ENCODEDPASS, ENCODEDPASS}, new String[] {returendUserCommand.getEmail(), returendUserCommand.getPassword(), returendUserCommand.getMatchingPassword()});
		verify(userRepository, times(1)).findByEmail(anyString());
		verify(userRepository, times(1)).save(any());
		verifyNoMoreInteractions(userRepository);
		verify(roleRepository, times(1)).findByRole(anyString());
		verifyNoMoreInteractions(roleRepository);
		verify(userCommandToUser, times(1)).convert(any());
		verifyNoMoreInteractions(userCommandToUser);
		verify(userToUserCommand, times(1)).convert(any());
		verifyNoMoreInteractions(userToUserCommand);
		verify(passwordEncoder, times(1)).encode(any());
		verifyNoMoreInteractions(passwordEncoder);
	}

	@Test(expected=EmailExistsException.class)
	public void registerNewExistingUserCommand() throws EmailExistsException {
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		userCommand.setPassword(PASS);
		userCommand.setMatchingPassword(PASS);
		
		User existingUser = new User();
		existingUser.setEmail(EMAIL);
		existingUser.setPassword(PASS);
		
		when(userRepository.findByEmail(anyString())).thenReturn(existingUser);
		
		userServiceImpl.registerNewUserCommand(userCommand);
		
	}
}
