package com.noticeboardproject.services;

import java.util.Arrays;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.noticeboardproject.domain.PasswordResetToken;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.PasswordResetTokenRepository;

@Service
@Transactional
public class SecurityUserServiceImpl implements SecurityUserService {

    private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
    public SecurityUserServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
		super();
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}

	@Override
    public String validatePasswordResetToken(long id, String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != id)) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        final User user = passToken.getUser();
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }

	@Override
	public PasswordResetToken getPasswordResetTokenByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
    
    

}
