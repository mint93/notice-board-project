package com.noticeboardproject.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.noticeboardproject.commands.UserCommand;

//By default, the LocalValidatorFactoryBean configures a SpringConstraintValidatorFactory that uses Spring to create ConstraintValidator instances. This allows your custom ConstraintValidators to benefit from dependency injection like any other Spring bean. 
public class PasswordMatchesValidator 
implements ConstraintValidator<PasswordMatches, Object> { 
   
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {       
  }
  
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){   
      UserCommand userCommand = (UserCommand) obj;
      return userCommand.getPassword().equals(userCommand.getMatchingPassword());
  }     
}