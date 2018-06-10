package com.noticeboardproject.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;
import com.noticeboardproject.events.OnRegistrationCompleteEvent;
import com.noticeboardproject.exceptions.EmailExistsException;
import com.noticeboardproject.services.SecurityUserService;
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
	SecurityUserService securityUserService;
	
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
			result.rejectValue("password", "PasswordMatches", messages.getMessage("message.passwordsNotMatch", null, request.getLocale()));
		}
		
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
	
	
	@GetMapping("/user/registrationConfirm")
	public ModelAndView confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
		
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    
	    if (verificationToken == null) {
	    	String message = messages.getMessage("auth.message.invalidToken", null, request.getLocale());
	    	model.addAttribute("message", message);
	    	model.addAttribute("expired", false);
	    	return new ModelAndView("redirect:badUser");
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	String message = messages.getMessage("auth.message.expired", null, request.getLocale());
	    	model.addAttribute("message", message);
	        model.addAttribute("expired", true);
	        model.addAttribute("token", verificationToken.getToken());
	    	return new ModelAndView("redirect:badUser");
	    }
	    
	    user.setEnabled(true);
	    userService.saveRegisteredUser(user);
	    
	    return new ModelAndView("user/successRegister", "message", "userConfirmed");
	}
	
	@GetMapping("/user/resendRegistrationToken")
    public ModelAndView resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new ModelAndView("user/successRegister", "message", "resendRegistrationToken");
	}
	
	@GetMapping("/user/resetPassword")
	public String resetPasswordForm(Model model) {
		UserCommand userCommand = new UserCommand();
		userCommand.setPassword("a");
		userCommand.setMatchingPassword("a");
		model.addAttribute("user", userCommand);
		return "user/forgotPassword";
	}

	@PostMapping("/user/resetPassword")
    public ModelAndView resetPassword(final HttpServletRequest request, @ModelAttribute("user") @Valid UserCommand userCommand, BindingResult result, Errors errors, Model model) {
		User user = null;
		if (!result.hasErrors()) {
			user = userService.findUserByEmail(userCommand.getEmail());
			if (user==null) {
				result.rejectValue("email", "message.emailDontExists");
			}
		}
		
	    if (result.hasErrors()) {
			return new ModelAndView("user/forgotPassword", "user", userCommand);
		}
	    else {
	    	final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
            model.addAttribute("message", "resetPassword");
            model.addAttribute("user", user);
            return new ModelAndView("user/successRegister");
	    }
		
	}
	
	@GetMapping("/user/changePassword")
    public RedirectView showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        final String result = securityUserService.validatePasswordResetToken(id, token);
        if (result != null) {
        	redirectAttributes.addFlashAttribute("message", messages.getMessage("auth.message." + result, null, request.getLocale()));
            return new RedirectView("badToken");
        }
        UserCommand userCommand = new UserCommand();
        userCommand.setEmail(securityUserService.getPasswordResetTokenByToken(token).getUser().getEmail());
        redirectAttributes.addFlashAttribute("user", userCommand);
        return new RedirectView("updatePassword");
	}
	
	@GetMapping("/user/changePasswordForLoggedUser")
    public ModelAndView showChangePasswordPageForLoggedUser(final Locale locale, final Model model, HttpServletRequest request, Principal principal) {
		User loggedUser = userService.findUserByEmail(principal.getName());
		final Authentication auth = new UsernamePasswordAuthenticationToken(loggedUser, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
	    SecurityContextHolder.getContext().setAuthentication(auth);
		
        UserCommand userCommand = new UserCommand();
        userCommand.setEmail(loggedUser.getEmail());
        model.addAttribute("user", userCommand);
        return new ModelAndView("user/updatePassword");
	}
	
	@PostMapping("/user/savePassword")
	public ModelAndView savePassword(Locale locale, @ModelAttribute("user") @Valid UserCommand userCommand, BindingResult result, Errors errors, Model model, HttpServletRequest request) {
		User user = null;
		if (errors.getAllErrors().stream().anyMatch(error -> error.getCode().equals("PasswordMatches"))) {
			result.rejectValue("password", "PasswordMatches", messages.getMessage("message.passwordsNotMatch", null, request.getLocale()));
		}
		if (!result.hasErrors()) {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userService.changeUserPassword(user, userCommand.getPassword());
			
			model.addAttribute("message", "passwordUpdated");
		    return new ModelAndView("user/successRegister");
		}else {
			return new ModelAndView("user/updatePassword", "user", userCommand);
		}
	}
	
	
	
	
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/user/registrationConfirm?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail(messages.getMessage("message.subject.resendToken", null, locale), message + confirmationUrl, user);
	}
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + url, user);
	}
			 
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject(subject);
	    email.setText(body);
	    email.setTo(user.getEmail());
	    return email;
	}
	
}
