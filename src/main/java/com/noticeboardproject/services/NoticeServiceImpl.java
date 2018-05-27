package com.noticeboardproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.converters.NoticeCommandToNotice;
import com.noticeboardproject.repositories.NoticeRepository;
import com.noticeboardproject.storage.StorageService;

@Service
public class NoticeServiceImpl implements NoticeService{

	private NoticeRepository noticeRepository;
	private NoticeCommandToNotice noticeCommandToNotice;
	private StorageService storageService;
	
	@Autowired
	public NoticeServiceImpl(NoticeRepository noticeRepository, NoticeCommandToNotice noticeCommandToNotice, StorageService storageService) {
		this.noticeRepository = noticeRepository;
		this.noticeCommandToNotice = noticeCommandToNotice;
		this.storageService = storageService;
	}
	
	@Override
	public void saveNoticeCommand(NoticeCommand noticeCommand) {
		noticeRepository.save(noticeCommandToNotice.convert(noticeCommand));
		storageService.deleteAllFilesForUser(noticeCommand.getUser().getEmail());
	}
	
}
