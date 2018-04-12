package com.noticeboardproject.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import com.noticeboardproject.domain.PasswordResetToken;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.PasswordResetTokenRepository;

public class SecurityUserServiceTest {
	
	SecurityUserServiceImpl securityUserService;
	
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		securityUserService = new SecurityUserServiceImpl(passwordResetTokenRepository);
	}
    
    @Test
    public void givenWrongPasswordResetToken_whenValidatePasswordResetToken_thenReturnErrorMessage() {
    	when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(null);
    	String errorMessage = securityUserService.validatePasswordResetToken(1L, "token");
    	
    	verify(passwordResetTokenRepository, timeout(1)).findByToken(anyString());
    	verifyNoMoreInteractions(passwordResetTokenRepository);
    	assertEquals("invalidToken", errorMessage);
    }
    
    @Test
    public void givenExpiredPasswordResetToken_whenValidatePasswordResetToken_thenReturnErrorMessage() {
    	PasswordResetToken expiredPasswordResetToken = new PasswordResetToken();
    	User user = new User();
    	user.setId(1L);
    	expiredPasswordResetToken.setUser(user);
    	expiredPasswordResetToken.setExpiryDate(new Date(0L));
    	when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(expiredPasswordResetToken);
    	String errorMessage = securityUserService.validatePasswordResetToken(1L, "token");
    	
    	verify(passwordResetTokenRepository, timeout(1)).findByToken(anyString());
    	verifyNoMoreInteractions(passwordResetTokenRepository);
    	assertEquals("expired", errorMessage);
    }
    
    @Test
    public void givenCorrectPasswordResetToken_whenValidatePasswordResetToken_thenReturnNoErrorMessage() {
    	PasswordResetToken passwordResetToken = new PasswordResetToken();
    	User user = new User();
    	user.setId(1L);
    	passwordResetToken.setUser(user);
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.YEAR, 5);
    	passwordResetToken.setExpiryDate(calendar.getTime());
    	when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);
    	String errorMessage = securityUserService.validatePasswordResetToken(1L, "token");
    	
    	verify(passwordResetTokenRepository, timeout(1)).findByToken(anyString());
    	verifyNoMoreInteractions(passwordResetTokenRepository);
    	assertEquals(null, errorMessage);
    	assertEquals(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), user);
    }
}