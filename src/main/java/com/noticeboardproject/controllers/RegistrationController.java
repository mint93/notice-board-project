package com.noticeboardproject.controllers;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.events.OnRegistrationCompleteEvent;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.services.UserService;

@Controller
public class RegistrationController {
	
	UserService userService;
	
	@Autowired
	MessageSource messages;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
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
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserCommand userCommand, BindingResult result, Errors errors, HttpServletRequest request) {
		UserCommand registeredUserCommand=null;

		//@Target of interface PasswordMatches is ElementType.TYPE, so error is saved in object 'user', not in user's field 'password'
		//in thymeleaf we can only iterate through errors of fields
		//in order to display error message in thymleaf I add this error to 'password' field
		//now in 'errors' the same error is duplicated for object 'user' and for field 'password' in object 'user', but I can now display error massage
		if (errors.getAllErrors().stream().anyMatch(error -> error.getCode().equals("PasswordMatches"))) {
			//result.rejectValue("password", "PasswordMatches", messages.getMessage("message.passwordsNotMatch", null, request.getLocale()));
			result.rejectValue("password", "PasswordMatches", "message.passwordsNotMatch");
		}
		
		if (!result.hasErrors()) {
			try {
		        registeredUserCommand = userService.registerNewUserCommand(userCommand);
		    } catch (EmailExistsException e) {
		    	//result.rejectValue("email", messages.getMessage("message.emailExists", null, request.getLocale()));
		    	result.rejectValue("email", "message.emailExists");
		    }
		}
		
	    
	    if (result.hasErrors()) {
			return new ModelAndView("user/registration", "user", userCommand);
		}
	    else {
	    	try {
				String appUrl = request.getContextPath();
		        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUserCommand, request.getLocale(), appUrl));
			} catch (Exception e) {
				e.printStackTrace();
				return new ModelAndView("emailError", "user", userCommand);
			}
	        return new ModelAndView("user/successRegister", "user", registeredUserCommand);
	    }
	}
	
	
	@GetMapping(value = "/user/registrationConfirm")
	public ModelAndView confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
		
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    
	    if (verificationToken == null) {
	    	//String message = messages.getMessage("auth.message.invalidToken", null, request.getLocale());
	    	String message = "auth.message.invalidToken";
	    	return new ModelAndView("redirect:badUser", "message", message);	    	
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	//String message = messages.getMessage("auth.message.expired", null, request.getLocale());
	    	String message = "auth.message.expired";
	    	return new ModelAndView("redirect:badUser", "message", message);
	    }
	    
	    user.setEnabled(true);
	    userService.saveRegisteredUser(user);
    	return new ModelAndView("redirect:login");	    
	}
	
	@GetMapping("/user/badUser")
	public String badUser(Model model) {
		return "user/badUser";
	}
	
	@GetMapping("/user/login")
	public String loginPage(Model model) {
		return "user/login";
	}
	
}
