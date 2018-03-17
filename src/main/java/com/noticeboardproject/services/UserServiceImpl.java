package com.noticeboardproject.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	UserToUserCommand userToUserCommand;
	
	UserCommandToUser userCommandToUser;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, UserToUserCommand userToUserCommand, UserCommandToUser userCommandToUser) {
		this.userRepository = userRepository;
		this.userToUserCommand = userToUserCommand;
		this.userCommandToUser = userCommandToUser;
	}

	@Transactional
	@Override
    public UserCommand registerNewUserCommand(final UserCommand userCommand) {
        if (emailExist(userCommand.getEmail())) {
        	//todo: Exception - User already exists
        	return null;
        }
        final User user = userCommandToUser.convert(userCommand);
        //todo: set roles for user
        User savedUser = userRepository.save(user);
        return userToUserCommand.convert(savedUser);
	}
	
	private boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
	}
}
