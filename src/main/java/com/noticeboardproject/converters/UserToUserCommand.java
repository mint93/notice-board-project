package com.noticeboardproject.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;

import lombok.Synchronized;

@Component
public class UserToUserCommand implements Converter<User, UserCommand>{

	@Synchronized
	@Nullable
	@Override
	public UserCommand convert(User user) {
		if (user==null) {
			return null;
		}
		final UserCommand userCommand = new UserCommand();
		userCommand.setEmail(user.getEmail());
		userCommand.setPassword(user.getPassword());
		userCommand.setMatchingPassword(user.getPassword());
		
		return userCommand;
	}
}
