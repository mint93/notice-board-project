package com.noticeboardproject.services;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.converters.UserCommandToUser;
import com.noticeboardproject.converters.UserToUserCommand;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.repositories.RoleRepository;
import com.noticeboardproject.repositories.UserRepository;
import com.noticeboardproject.repositories.VerificationTokenRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private VerificationTokenRepository verificationTokenRepository;
	
	private UserToUserCommand userToUserCommand;
	
	private UserCommandToUser userCommandToUser;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			UserToUserCommand userToUserCommand, UserCommandToUser userCommandToUser, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userToUserCommand = userToUserCommand;
		this.userCommandToUser = userCommandToUser;
		this.verificationTokenRepository = verificationTokenRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
    public UserCommand registerNewUserCommand(final UserCommand userCommand) throws EmailExistsException {
        if (emailExist(userCommand.getEmail())) {
        	throw new EmailExistsException("There is an account with that email adress: "
                    +  userCommand.getEmail());
        }
        final User user = userCommandToUser.convert(userCommand);
        user.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        Role roleUser = roleRepository.findByRole("ROLE_USER");
        roleUser.getUsers().add(user);
        user.getRoles().add(roleUser);
        User savedUser = userRepository.save(user);
        return userToUserCommand.convert(savedUser);
	}
	
	private boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
	}
	
	@Override
    public User getUser(String verificationToken) {
        User user = verificationTokenRepository.findByToken(verificationToken).getUser();
        return user;
    }
    
    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }
    
    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }
     
    @Override
    public void createVerificationToken(UserCommand userCommand, String token) {
        VerificationToken verificationToken = new VerificationToken(token, userRepository.findByEmail(userCommand.getEmail()));
        verificationTokenRepository.save(verificationToken);
    }
    
    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken token = verificationTokenRepository.findByToken(existingVerificationToken);
        token.updateToken(UUID.randomUUID().toString());
        token = verificationTokenRepository.save(token);
        return token;
    }
}
