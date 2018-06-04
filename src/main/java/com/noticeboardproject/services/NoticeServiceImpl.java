package com.noticeboardproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.converters.NoticeCommandToNotice;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.exceptions.NotFoundException;
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
	public Notice saveNoticeCommand(NoticeCommand noticeCommand) {
		Notice savedNotice = noticeRepository.save(noticeCommandToNotice.convert(noticeCommand));
		storageService.deleteAllFilesForUser(noticeCommand.getUser().getEmail());
		return savedNotice;
	}
	
	@Override
	public Notice findById(Long id) {
		Optional<Notice> noticeOptional = noticeRepository.findById(id);

		if (!noticeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found. For ID value: " + id.toString() );
		}

		return noticeOptional.get();
	}
	
}
