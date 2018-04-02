package com.noticeboardproject.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.UserCommand;
import com.noticeboardproject.events.OnRegistrationCompleteEvent;
import com.noticeboardproject.services.UserService;

@Component
public class RegistrationListener implements
  ApplicationListener<OnRegistrationCompleteEvent> {
  
    @Autowired
    private UserService userService;
  
    @Autowired
    private MessageSource messages;
  
    @Autowired
    private JavaMailSender mailSender;
 
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
 
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserCommand userCommand = event.getUserCommand();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(userCommand, token);
         
        String recipientAddress = userCommand.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/user/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLokale());
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}