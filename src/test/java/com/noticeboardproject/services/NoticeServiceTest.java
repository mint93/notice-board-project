package com.noticeboardproject.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.converters.NoticeCommandToNotice;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.NoticeRepository;
import com.noticeboardproject.storage.StorageService;

public class NoticeServiceTest {

	private NoticeService noticeService;
	
	@Mock
	private NoticeRepository noticeRepository;
	
	@Mock
	private NoticeCommandToNotice noticeCommandToNotice;
	
	@Mock
	private StorageService storageService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		noticeService = new NoticeServiceImpl(noticeRepository, noticeCommandToNotice, storageService);
	}
	
	@Test
	public void saveNoticeCommandTest() {
		User user = new User();
		user.setEmail("email@gmail.com");
		NoticeCommand noticeCommand = new NoticeCommand();
		noticeCommand.setUser(user);
		when(noticeRepository.save(any())).thenReturn(new Notice());
		when(noticeCommandToNotice.convert(any())).thenReturn(new Notice());
		doNothing().when(storageService).deleteAllFilesForUser(anyString());
		
		noticeService.saveNoticeCommand(noticeCommand);
		
		verify(noticeRepository, times(1)).save(any());
		verify(storageService, times(1)).deleteAllFilesForUser(anyString());
	}
}
