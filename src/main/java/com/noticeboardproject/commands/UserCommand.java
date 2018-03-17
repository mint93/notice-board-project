package com.noticeboardproject.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommand {

	private String email;
	
	private String password;
	
	private String matchingPassword;
	
}