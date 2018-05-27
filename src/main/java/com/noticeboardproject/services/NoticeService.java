package com.noticeboardproject.services;

import com.noticeboardproject.commands.NoticeCommand;

public interface NoticeService {
	void saveNoticeCommand(NoticeCommand noticeCommand);
}
