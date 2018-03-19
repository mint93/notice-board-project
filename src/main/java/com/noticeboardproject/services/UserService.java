package com.noticeboardproject.services;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.exceptions.EmailExistsException;

public interface UserService {

	UserCommand registerNewUserCommand(UserCommand userCommand) throws EmailExistsException;
	
}
