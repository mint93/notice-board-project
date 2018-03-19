package com.noticeboardproject.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserToUserCommand userToUserCommand;
	
	@Autowired
	UserCommandToUser userCommandToUser;

	private final String EMAIL = "email";
	private final String PASSWORD = "password";
	
	@Before
	public void setUp() throws Exception {
	}

	@Transactional
	@Test
	public void registerNewUserCommand() {
		//given
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		userCommand.setPassword(PASSWORD);
		userCommand.setMatchingPassword(PASSWORD);
		
		//when
		UserCommand registeredUserCommand=null;
		try {
			registeredUserCommand = userService.registerNewUserCommand(userCommand);
		} catch (EmailExistsException e) {
			e.printStackTrace();
			fail();
		}
		
		//then
		assertEquals(EMAIL, registeredUserCommand.getEmail());
		assertEquals(PASSWORD, registeredUserCommand.getPassword());
		assertEquals(PASSWORD, registeredUserCommand.getMatchingPassword());
		
	}
	
	@Transactional
	@Test(expected=EmailExistsException.class)
	public void registerNewExistingUserCommand() throws EmailExistsException {
		//given
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		userCommand.setPassword(PASSWORD);
		userCommand.setMatchingPassword(PASSWORD);
		
		userRepository.save(userCommandToUser.convert(userCommand));
		
		//when
		userService.registerNewUserCommand(userCommand);
	}

}
