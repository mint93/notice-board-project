package com.noticeboardproject.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VerificationTokenRepositoryIntegrationTest {
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	
	VerificationToken verificationToken;
	
	User user;
	
	@Before
	public void setUp() throws Exception {
		user = new User();
		userRepository.save(user);
		
		verificationToken = new VerificationToken("token", user);
		verificationToken.setExpiryDate(new Date(0L));
	
		verificationTokenRepository.save(verificationToken);
	}

	@Test
	public void findByToken() {
		VerificationToken foundToken = verificationTokenRepository.findByToken("token");
		assertEquals(verificationToken, foundToken);
	}
	
	@Test
	public void findByToken_wrongToken() {
		VerificationToken foundToken = verificationTokenRepository.findByToken("wrongToken");
		assertEquals(null, foundToken);
	}

	@Test
	public void findByUser() {
		VerificationToken foundToken = verificationTokenRepository.findByUser(user);
		assertEquals(verificationToken, foundToken);
	}
}