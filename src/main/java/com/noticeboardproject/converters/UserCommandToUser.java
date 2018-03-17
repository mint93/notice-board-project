package com.noticeboardproject.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;

import lombok.Synchronized;

@Component
public class UserCommandToUser implements Converter<UserCommand, User>{

	@Synchronized
	@Nullable
	@Override
	public User convert(UserCommand userCommand) {
		if (userCommand==null) {
			return null;
		}
		final User user= new User();
		user.setEmail(userCommand.getEmail());
		user.setPassword(userCommand.getPassword());
	
		return user;
	}
}
