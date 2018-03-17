package com.noticeboardproject.converters;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;

public class UserCommandToUserTest {
	
	public static final String EMAIL = "email";
	public static final String PASSWORD = "pass";
	
	public UserCommandToUser converter;

	@Before
	public void setUp() throws Exception {
		converter = new UserCommandToUser();
	}

	@Test
	public void givenNullObject_whenConvert_thenReturnNull() {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void givenEmptyObject_whenConvert_thenReturnNotNull() {
		assertNotNull(converter.convert(new UserCommand()));
	}
	
	@Test
	public void givenUserObject_whenConvert_thenReturnUserCommand() {
		//given
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail(EMAIL);
		userCommand.setPassword(PASSWORD);
		userCommand.setMatchingPassword(PASSWORD);
		
		//when
		User user = converter.convert(userCommand);
		
		//then
		assertArrayEquals(new String[]{EMAIL, PASSWORD}, new String[]{user.getEmail(), user.getPassword()});
	}

}
