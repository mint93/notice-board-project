package com.noticeboardproject.commands;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.noticeboardproject.validators.PasswordMatches;
import com.noticeboardproject.validators.ValidEmail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class UserCommand {

	@NotNull
	@Size(min=1, message = "{Size.userCommand.email}")
	@ValidEmail
	private String email;
	
	@NotNull
	@NotBlank
	private String password;
	
	private String matchingPassword;
	
}