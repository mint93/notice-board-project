package com.noticeboardproject.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.SearchBy;

public interface NoticeService {
	Notice saveNoticeCommand(NoticeCommand noticeCommand);

	Notice findById(Long id);

	Notice saveOrUpdateNotice(Notice notice);
	
	Page<Notice> findBySearchTerm(Pageable pageable, SearchBy searchBy);
}
