package com.noticeboardproject.converters;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;

public class UserToUserCommandTest {
	
	public static final String EMAIL = "email";
	public static final String PASSWORD = "pass";
	
	UserToUserCommand converter;

	@Before
	public void setUp() throws Exception {
		converter = new UserToUserCommand();
	}

	@Test
	public void givenNullObject_whenConvert_thenReturnNull() {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void givenEmptyObject_whenConvert_thenReturnNotNull() {
		assertNotNull(converter.convert(new User()));
	}
	
	@Test
	public void givenUserObject_whenConvert_thenReturnUserCommand() {
		//given
		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		
		//when
		UserCommand userCommand = converter.convert(user);
		
		//then
		assertArrayEquals(new String[]{EMAIL, PASSWORD, PASSWORD}, new String[]{userCommand.getEmail(), userCommand.getPassword(), userCommand.getMatchingPassword()});
	}

}
