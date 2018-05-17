package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	Notice findByTitle(String title);
	Notice findByUser(User user);
}
