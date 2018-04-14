package com.noticeboardproject.domain;

import java.util.Date;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class PasswordResetTokenTest {
	
	@Test
	public void passwordResetTokenHashCodeAndEqualstest() {
		User user1 = new User();
		user1.setEmail("email");
		user1.setPassword("password1");
		user1.setEnabled(true);
		User user2 = new User();
		user2.setEmail("email");
		user2.setPassword("password2");
		user1.setEnabled(false);
		
		PasswordResetToken passwordResetToken1Group1 = new PasswordResetToken("token1", user1);
		passwordResetToken1Group1.setExpiryDate(new Date(1000));
		passwordResetToken1Group1.setId(1L);
		
		PasswordResetToken passwordResetToken2Group1 = new PasswordResetToken("token1", user2);
		passwordResetToken2Group1.setExpiryDate(new Date(1000));
		passwordResetToken2Group1.setId(2L);
		
		PasswordResetToken passwordResetToken1Group2 = new PasswordResetToken("token2", user1);
		passwordResetToken1Group2.setExpiryDate(new Date(1000));
		passwordResetToken1Group2.setId(3L);
		
		PasswordResetToken passwordResetToken2Group2 = new PasswordResetToken("token2", user2);
		passwordResetToken2Group2.setExpiryDate(new Date(1000));
		passwordResetToken2Group2.setId(4L);
		
		new EqualsTester()
			.addEqualityGroup(passwordResetToken1Group1, passwordResetToken2Group1)
			.addEqualityGroup(passwordResetToken1Group2, passwordResetToken2Group2)
			.testEquals();
	}
}
