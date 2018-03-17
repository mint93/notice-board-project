package com.noticeboardproject.services;

import static org.junit.Assert.assertEquals;

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

	@Before
	public void setUp() throws Exception {
	}

	@Transactional
	@Test
	public void registerNewUserCommand() {
		//given
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail("email");
		userCommand.setPassword("pass");
		userCommand.setMatchingPassword("pass");
		
		//when
		UserCommand registeredUserCommand = userService.registerNewUserCommand(userCommand);
		
		//then
		assertEquals("email", registeredUserCommand.getEmail());
		assertEquals("pass", registeredUserCommand.getPassword());
		assertEquals("pass", registeredUserCommand.getMatchingPassword());
		
	}

}
