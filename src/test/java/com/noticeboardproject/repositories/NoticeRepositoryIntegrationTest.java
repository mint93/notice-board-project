package com.noticeboardproject.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NoticeRepositoryIntegrationTest {
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	Notice notice;
	User user;
	
	@Before
	public void setUp() throws Exception {
		notice = new Notice();
		notice.setTitle("title");
		notice.setDescription("description");
		user = new User();
		user.setEmail("email@gmail.com");
		user.addNotice(notice);
		
		userRepository.save(user);
		noticeRepository.save(notice);
	}

	@Test
	public void findByTitle() {
		Notice foundNotice = noticeRepository.findByTitle(notice.getTitle());
		assertEquals(notice, foundNotice);
	}
	
	@Test
	public void findByUser() {
		Notice foundNotice = noticeRepository.findByUser(user);
		assertEquals(notice, foundNotice);
	}
}
