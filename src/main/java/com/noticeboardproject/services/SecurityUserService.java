package com.noticeboardproject.services;

import com.noticeboardproject.domain.PasswordResetToken;

public interface SecurityUserService {
	String validatePasswordResetToken(long id, String token);
	PasswordResetToken getPasswordResetTokenByToken(String token);
}
