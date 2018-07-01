package com.noticeboardproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.converters.NoticeCommandToNotice;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.SearchBy;
import com.noticeboardproject.exceptions.NotFoundException;
import com.noticeboardproject.repositories.CategoryRepository;
import com.noticeboardproject.repositories.NoticeRepository;
import com.noticeboardproject.specifications.NoticeSpecifications;
import com.noticeboardproject.storage.StorageService;

@Service
public class NoticeServiceImpl implements NoticeService{

	private NoticeRepository noticeRepository;
	private CategoryRepository categoryRepository;
	private NoticeCommandToNotice noticeCommandToNotice;
	private StorageService storageService;
	
	@Autowired
	public NoticeServiceImpl(NoticeRepository noticeRepository, NoticeCommandToNotice noticeCommandToNotice, StorageService storageService, CategoryRepository categoryRepository) {
		this.noticeRepository = noticeRepository;
		this.noticeCommandToNotice = noticeCommandToNotice;
		this.storageService = storageService;
		this.categoryRepository = categoryRepository;
	}
	
	@Override
	public Notice saveNoticeCommand(NoticeCommand noticeCommand) {
		Notice savedNotice = noticeRepository.save(noticeCommandToNotice.convert(noticeCommand));
		storageService.deleteAllFilesForUser(noticeCommand.getUser().getEmail());
		return savedNotice;
	}
	
	@Override
	public Notice saveOrUpdateNotice(Notice notice) {
		return noticeRepository.save(notice);
	}
	
	@Override
	public Notice findById(Long id) {
		Optional<Notice> noticeOptional = noticeRepository.findById(id);

		if (!noticeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found. For ID value: " + id.toString() );
		}

		return noticeOptional.get();
	}

	@Override
	public Page<Notice> findBySearchTerm(Pageable pageable, SearchBy searchBy) {
		int priceFrom = 0;
		int priceTo = Integer.MAX_VALUE;
		if (!searchBy.getPriceFrom().equals("")) {
			priceFrom = Integer.valueOf(searchBy.getPriceFrom());
		}
		if (!searchBy.getPriceTo().equals("")) {			
			priceTo = Integer.valueOf(searchBy.getPriceTo());
		}
    	
		Specification<Notice> searchNoticeByTitileSpec = NoticeSpecifications.titleOrDescriptionContainsIgnoreCase(searchBy.getTitle(), searchBy.isSearchInDescription());
		Specification<Notice> searchNoticeByCityOrStateSpec = NoticeSpecifications.cityOrStateContainsIgnoreCase(searchBy.getCity(), searchBy.isSearchByWholeState());
		Specification<Notice> searchNoticeByCategorySpec = NoticeSpecifications.categotyEquals(searchBy.getCategory());
		Specification<Notice> searchNoticeWithMainImageOnly = NoticeSpecifications.mainImageContains(searchBy.isOnlyWithImage());
		Specification<Notice> searchNoticeInPriceRange = NoticeSpecifications.priceInRange(priceFrom, priceTo);
		return noticeRepository.findAll(Specification.where(searchNoticeByTitileSpec).and(searchNoticeByCityOrStateSpec).and(searchNoticeByCategorySpec).and(searchNoticeWithMainImageOnly).and(searchNoticeInPriceRange), pageable);
	}
	
}
