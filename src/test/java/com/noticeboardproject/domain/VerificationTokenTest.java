package com.noticeboardproject.domain;

import java.util.Date;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class VerificationTokenTest {
	
	@Test
	public void verificationTokenHashCodeAndEqualstest(){
		User user1 = new User();
		user1.setEmail("email");
		user1.setPassword("password1");
		user1.setEnabled(true);
		User user2 = new User();
		user2.setEmail("email");
		user2.setPassword("password2");
		user1.setEnabled(false);
		
		VerificationToken verificationToken1Group1 = new VerificationToken("token1", user1);
		verificationToken1Group1.setExpiryDate(new Date(1000));
		verificationToken1Group1.setId(1L);
		
		VerificationToken verificationToken2Group1 = new VerificationToken("token1", user2);
		verificationToken2Group1.setExpiryDate(new Date(1000));
		verificationToken2Group1.setId(2L);
		
		VerificationToken verificationToken1Group2 = new VerificationToken("token2", user1);
		verificationToken1Group2.setExpiryDate(new Date(1000));
		verificationToken1Group2.setId(3L);
		
		VerificationToken passwordResetToken2Group = new VerificationToken("token2", user2);
		passwordResetToken2Group.setExpiryDate(new Date(1000));
		passwordResetToken2Group.setId(4L);
		
		new EqualsTester()
			.addEqualityGroup(verificationToken1Group1, verificationToken2Group1)
			.addEqualityGroup(verificationToken1Group2, passwordResetToken2Group)
			.testEquals();

	}
}
