package com.noticeboardproject.bootstrap;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.Privilege;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.repositories.PrivilegeRepository;
import com.noticeboardproject.repositories.RoleRepository;
import com.noticeboardproject.services.UserService;

@Component
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final RoleRepository roleRepository;
	
	private final PrivilegeRepository privilegeRepository;
	
	private final UserService userService;

	public UserBootstrap(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, UserService userService) {
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Privilege privilege1 = new Privilege();
		privilege1.setPrivilege("READ_PRIVILEGE");
		privilegeRepository.save(privilege1);
		
		Role roleUser = new Role();
		roleUser.getPrivileges().add(privilege1);
		roleUser.setRole("ROLE_USER");
		roleRepository.save(roleUser);
		
		createUser();
	}
	
	private void createUser() {
		User registeredUser = null;
		UserCommand registeredUserCommand = null;
		UserCommand userCommand = new UserCommand();
		userCommand.setEmail("test@email.com");
		userCommand.setPassword("a");
		userCommand.setMatchingPassword("a");
		try {
			registeredUserCommand = userService.registerNewUserCommand(userCommand);
		} catch (EmailExistsException e) {
			e.printStackTrace();
		}
		String token = UUID.randomUUID().toString();
        userService.createVerificationToken(registeredUserCommand, token);
        VerificationToken verificationToken = userService.getVerificationToken(token);
        registeredUser = verificationToken.getUser();
        registeredUser.setEnabled(true);
	    userService.saveRegisteredUser(registeredUser);
	}

}
