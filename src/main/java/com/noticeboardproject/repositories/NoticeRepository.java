package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;

public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice>{
	Notice findByTitle(String title);
	Notice findByUser(User user);
}