package com.noticeboardproject.services;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Notice;

public interface NoticeService {
	Notice saveNoticeCommand(NoticeCommand noticeCommand);

	Notice findById(Long id);
}
