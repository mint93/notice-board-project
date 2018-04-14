package com.noticeboardproject.domain;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class UserTest {
	@Test
	public void userHashCodeAndEqualstest(){
		User user1Group1 = new User();
		user1Group1.setEmail("email1");
		user1Group1.setId(1L);
		user1Group1.setPassword("password1");
		user1Group1.setEnabled(true);
		
		User user2Group1 = new User();
		user2Group1.setEmail("email1");
		user2Group1.setId(2L);
		user2Group1.setPassword("password2");
		user2Group1.setEnabled(false);
		
		User user1Group2 = new User();
		user1Group2.setEmail("email2");
		user1Group2.setId(3L);
		user1Group2.setPassword("password3");
		user1Group2.setEnabled(true);
		
		User user2Group2 = new User();
		user2Group2.setEmail("email2");
		user2Group2.setId(4L);
		user2Group2.setPassword("password4");
		user2Group2.setEnabled(false);
		
		new EqualsTester()
			.addEqualityGroup(user1Group1, user2Group1)
			.addEqualityGroup(user1Group2, user2Group2)
			.testEquals();
	}
}
