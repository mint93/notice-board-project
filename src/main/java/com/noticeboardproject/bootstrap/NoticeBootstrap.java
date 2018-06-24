package com.noticeboardproject.bootstrap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.NoticeRepository;
import com.noticeboardproject.repositories.UserRepository;
import com.noticeboardproject.services.CategoryService;


@Component
public class NoticeBootstrap implements ApplicationListener<ContextRefreshedEvent>{

	private final NoticeRepository noticeRepository;
	private final UserRepository userRepository;
	private final CategoryService categoryService;

	public NoticeBootstrap(NoticeRepository noticeRepository, UserRepository userRepository, CategoryService categoryService) {
		this.noticeRepository = noticeRepository;
		this.userRepository = userRepository;
		this.categoryService = categoryService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		createNotices();
	}
	
	private void createNotices() {
		User user1 = new User();
		user1.setEmail("user1@email.com");
		user1.setEnabled(true);
		User savedUser1 = userRepository.save(user1);
		User user2 = new User();
		user2.setEmail("user2@email.com");
		user2.setEnabled(true);
		User savedUser2 = userRepository.save(user2);
		
		List<Notice> notices = new ArrayList<>();
		
		for (int i=1; i<=100; i++) {
		
			Notice notice = new Notice();
			if (i<=50) {
				notice.setTitle("title" + i);
				notice.setDescription("Description of notice" + i);
				notice.setPrice(100 + i);
				notice.setCategory(categoryService.getCategoryByCategory(CategoryEnum.AUTOMOTIVE));
				notice.setCity("city" + i);
				notice.setCreationDate(new Date(i*1000*60*60));
				notice.setPhoneNumber("+48" + i);
				notice.setViews(0);
				notice.setUser(savedUser1);
				notices.add(notice);
			}else {				
				notice.setTitle("title" + i);
				notice.setDescription("Description of notice" + i);
				notice.setPrice(100 + i);
				notice.setCategory(categoryService.getCategoryByCategory(CategoryEnum.BOOKS));
				notice.setCity("city" + i);
				notice.setCreationDate(new Date(i*1000*60*60));
				notice.setPhoneNumber("+48" + i);
				notice.setViews(0);
				notice.setUser(savedUser2);
				notices.add(notice);
			}
		}
		noticeRepository.saveAll(notices);
	}

}
