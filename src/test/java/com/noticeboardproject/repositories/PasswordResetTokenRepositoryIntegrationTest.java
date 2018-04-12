package com.noticeboardproject.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.PasswordResetToken;
import com.noticeboardproject.domain.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PasswordResetTokenRepositoryIntegrationTest {
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	UserRepository userRepository;
	
	PasswordResetToken passwordResetToken;
	
	@Before
	public void setUp() throws Exception {
		User user = new User();
		userRepository.save(user);
		passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken("token");
		passwordResetToken.setUser(user);
		
		passwordResetTokenRepository.save(passwordResetToken);
	}
	
	@Test
	public void findByTokenTest() {
		PasswordResetToken passwordResetTokenFound = passwordResetTokenRepository.findByToken(passwordResetToken.getToken());
		assertEquals(passwordResetToken, passwordResetTokenFound);
	}
}
