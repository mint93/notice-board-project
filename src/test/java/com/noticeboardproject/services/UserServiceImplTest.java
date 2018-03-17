package com.noticeboardproject.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.UserRepository;

public class UserServiceImplTest {

	UserServiceImpl userServiceImpl;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	UserToUserCommand userToUserCommand;
	
	@Mock
	UserCommandToUser userCommandToUser;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userServiceImpl = new UserServiceImpl(userRepository, userToUserCommand, userCommandToUser);
	}

	@Test
	public void registerNewUserCommand() {
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail("email");
		userCommand.setPassword("pass");
		userCommand.setMatchingPassword("pass");
		
		User savedUser = new User();
		savedUser.setEmail("email");
		savedUser.setPassword("pass");
		
		User convertedUser = new User();
		convertedUser.setEmail("email");
		convertedUser.setPassword("pass");
		
		UserCommand convertedUserCommand = new UserCommand();
		convertedUserCommand.setEmail("email");
		convertedUserCommand.setPassword("pass");
		convertedUserCommand.setMatchingPassword("pass");
		
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(userCommandToUser.convert(any(UserCommand.class))).thenReturn(convertedUser);
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(userToUserCommand.convert(any(User.class))).thenReturn(convertedUserCommand);
		
		UserCommand returendUserCommand = userServiceImpl.registerNewUserCommand(userCommand);
		
		assertNotNull(returendUserCommand);
		assertArrayEquals(new String[] {"email", "pass", "pass"}, new String[] {returendUserCommand.getEmail(), returendUserCommand.getPassword(), returendUserCommand.getMatchingPassword()});
	}

}
