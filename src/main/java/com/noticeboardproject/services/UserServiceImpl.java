package com.noticeboardproject.services;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.repositories.RoleRepository;
import com.noticeboardproject.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private UserToUserCommand userToUserCommand;
	
	private UserCommandToUser userCommandToUser;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			UserToUserCommand userToUserCommand, UserCommandToUser userCommandToUser) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userToUserCommand = userToUserCommand;
		this.userCommandToUser = userCommandToUser;
	}

	@Transactional
	@Override
    public UserCommand registerNewUserCommand(final UserCommand userCommand) throws EmailExistsException {
        if (emailExist(userCommand.getEmail())) {
        	throw new EmailExistsException("There is an account with that email adress: "
                    +  userCommand.getEmail());
        }
        final User user = userCommandToUser.convert(userCommand);
        user.setRoles(new HashSet<Role>());
        Role roleUser = roleRepository.findByRole("ROLE_USER");
        roleUser.getUsers().add(user);
        user.getRoles().add(roleUser);
        User savedUser = userRepository.save(user);
        return userToUserCommand.convert(savedUser);
	}
	
	private boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
	}
}
