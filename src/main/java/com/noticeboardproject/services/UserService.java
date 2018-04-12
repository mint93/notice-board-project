package com.noticeboardproject.services;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.exceptions.EmailExistsException;

public interface UserService {

	UserCommand registerNewUserCommand(UserCommand userCommand) throws EmailExistsException;
	
	User getUser(String verificationToken);
	
	void saveRegisteredUser(User user);
	 
	void createVerificationToken(UserCommand userCommand, String token);
	 
	VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String existingVerificationToken);

	User findUserByEmail(String email);

	void createPasswordResetTokenForUser(User user, String token);

	void changeUserPassword(User user, String password);
}
