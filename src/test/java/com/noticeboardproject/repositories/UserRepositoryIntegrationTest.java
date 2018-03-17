package com.noticeboardproject.repositories;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

	@Autowired
	UserRepository userRepository;
	
	User user;
	
	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setEmail("email");
		user.setPassword("pass");
		userRepository.save(user);
	}

	@Test
	public void findByEmail() {
		User foundUser = userRepository.findByEmail(user.getEmail());
		assertArrayEquals(new Object[] {user}, new Object[] {foundUser});
	}

}
