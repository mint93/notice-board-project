package com.noticeboardproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.services.UserService;

@Controller
public class RegistrationController {
	
	UserService userService;
	
	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/user/registration")
	public String showRegistrationFrom(Model model) {
		UserCommand userCommand = new UserCommand();
		model.addAttribute("user", userCommand);
		return "user/registration";
	}

	@PostMapping("/user/registration")
	public ModelAndView registerUserAccount(@ModelAttribute("user") UserCommand userCommand) {
		UserCommand registeredUserCommand = userService.registerNewUserCommand(userCommand);
	    
	    if (registeredUserCommand==null) {
	        return new ModelAndView("user/registration", "user", userCommand);
	    }
	    else {
	        return new ModelAndView("user/successRegister", "user", registeredUserCommand);
	    }
	}

	
}
