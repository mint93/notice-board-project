package com.noticeboardproject.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.converters.NoticeCommandToNotice;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.SearchBy;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.exceptions.NotFoundException;
import com.noticeboardproject.repositories.CategoryRepository;
import com.noticeboardproject.repositories.NoticeRepository;
import com.noticeboardproject.storage.StorageService;

public class NoticeServiceTest {

	private NoticeService noticeService;
	
	@Mock
	private NoticeRepository noticeRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private NoticeCommandToNotice noticeCommandToNotice;
	
	@Mock
	private StorageService storageService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		noticeService = new NoticeServiceImpl(noticeRepository, noticeCommandToNotice, storageService, categoryRepository);
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
	
	@Test(expected=NotFoundException.class)
	public void findById_NotExistingIdGiven_ShouldThrowException() {
		when(noticeRepository.findById(any())).thenReturn(Optional.empty());
		noticeService.findById(1L);
	}
	
	@Test
	public void findById_ExistingIdGiven_ShouldReturnNotice() {
		Notice notice = new Notice();
		notice.setId(1L);
		Optional<Notice> optionalNotice = Optional.of(notice);
		when(noticeRepository.findById(any())).thenReturn(optionalNotice);
		
		Notice foundNotice = noticeService.findById(1L);
		assertEquals(foundNotice, notice);
		verify(noticeRepository, times(1)).findById(any());
		verifyNoMoreInteractions(noticeRepository);
	}
	
	@Test
	public void findBySearchTerm_ShouldReturnNoticePage() {
		Notice notice1 = new Notice();
		Notice notice2 = new Notice();
		List<Notice> noticesList = new ArrayList<>();
		noticesList.add(notice1);
		noticesList.add(notice2);
		Page<Notice> notices = new PageImpl<>(noticesList);
		SearchBy searchBy = new SearchBy();
		searchBy.setTitle("title");
		searchBy.setCity("city");
		
		when(noticeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(notices);
		
		Page<Notice> foundNoticePage = noticeService.findBySearchTerm(PageRequest.of(1, 10), searchBy);
		assertEquals(foundNoticePage, notices);
		verify(noticeRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
		verifyNoMoreInteractions(noticeRepository);
	}
}
