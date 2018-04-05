package com.noticeboardproject.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	JavaMailSender mailSender;
	
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
	    	Map<String, Object> model = new HashMap<>();
	    	model.put("user", registeredUserCommand);
	    	model.put("message", "userRegistered");
	        return new ModelAndView("user/successRegister", model);
	    }
	}
	
	
	@GetMapping(value = "/user/registrationConfirm")
	public ModelAndView confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
		
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    
	    if (verificationToken == null) {
	    	//String message = messages.getMessage("auth.message.invalidToken", null, request.getLocale());
	    	String message = "auth.message.invalidToken";
	    	model.addAttribute("message", message);
	    	model.addAttribute("expired", false);
	    	return new ModelAndView("redirect:badUser");	    	
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	//String message = messages.getMessage("auth.message.expired", null, request.getLocale());
	    	String message = "auth.message.expired";
	    	model.addAttribute("message", message);
	        model.addAttribute("expired", true);
	        model.addAttribute("token", verificationToken.getToken());
	    	return new ModelAndView("redirect:badUser");
	    }
	    
	    user.setEnabled(true);
	    userService.saveRegisteredUser(user);
	    
	    return new ModelAndView("user/successRegister", "message", "userConfirmed");
	}
	
	@GetMapping("/user/badUser")
	public String badUser(Model model) {
		return "user/badUser";
	}
	
	@GetMapping("/user/login")
	public String loginPage(Model model) {
		return "user/login";
	}
	
	@GetMapping("/user/resendRegistrationToken")
    public ModelAndView resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new ModelAndView("user/successRegister", "message", "resendRegistrationToken");
	}

	
	
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/user/registrationConfirm?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(messages.getMessage("message.subject.resendToken", null, locale));
        email.setText(message + confirmationUrl);
        email.setTo(user.getEmail());
        return email;
	}
	
}
