package com.noticeboardproject.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.noticeboardproject.commands.UserCommand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent{

	private String appUrl;
	
	private Locale lokale;
	
	private UserCommand userCommand;
	
	public OnRegistrationCompleteEvent(UserCommand userCommand, Locale lokale, String appUrl) {
		super(userCommand);
		
		this.userCommand=userCommand;
		this.lokale=lokale;
		this.appUrl=appUrl;
	}

}
