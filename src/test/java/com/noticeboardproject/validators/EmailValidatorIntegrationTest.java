package com.noticeboardproject.validators;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.commands.UserCommand;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailValidatorIntegrationTest {

	@Autowired
    private Validator emailValidator;
	
	@Before
	public void setUp() throws Exception {
	}
 
    @Test
    public void shouldValidateWrongEmail() throws Exception {
        // given
    	UserCommand userCommand = new UserCommand();
        userCommand.setEmail("email.com");
        userCommand.setPassword("password1234");
        userCommand.setMatchingPassword("password1234");
        // when
        Set<ConstraintViolation<UserCommand>> violations = emailValidator.validate(userCommand);
        // then
        assertEquals(1, violations.size());
    }
    
    @Test
    public void shouldNotValidateCorrectEmail() throws Exception {
        // given
    	UserCommand userCommand = new UserCommand();
        userCommand.setEmail("email@gmail.com");
        userCommand.setPassword("password1234");
        userCommand.setMatchingPassword("password1234");
        // when
        Set<ConstraintViolation<UserCommand>> violations = emailValidator.validate(userCommand);
        // then
        assertEquals(0, violations.size());
    }

}
