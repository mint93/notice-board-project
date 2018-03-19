package com.noticeboardproject.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.exceptions.EmailExistsException;
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
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserCommand userCommand, BindingResult result) {
		UserCommand registeredUserCommand=null;
		
		if (!result.hasErrors()) {
			try {
		        registeredUserCommand = userService.registerNewUserCommand(userCommand);
		    } catch (EmailExistsException e) {
		    	result.rejectValue("email", "message.emailExists");
		    }    
		}
	    
	    if (result.hasErrors()) {
			return new ModelAndView("user/registration", "user", userCommand);
		}
	    else {
	        return new ModelAndView("user/successRegister", "user", registeredUserCommand);
	    }
		
	}

	
}
