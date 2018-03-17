package com.noticeboardproject.services;

import com.noticeboardproject.commands.UserCommand;

public interface UserService {

	UserCommand registerNewUserCommand(UserCommand userCommand);
	
}
