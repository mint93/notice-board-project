package com.noticeboardproject.converters;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.storage.StorageService;

import lombok.Synchronized;

@Component
public class NoticeCommandToNotice implements Converter<NoticeCommand, Notice>{

	private CategoryService categoryService;
	
	private StorageService storageService;
	
	@Autowired
	public NoticeCommandToNotice(CategoryService categoryService, StorageService storageService) {
		this.categoryService = categoryService;
		this.storageService = storageService;
	}
	
	@Synchronized
	@Nullable
	@Override
	public Notice convert(NoticeCommand noticeCommand) {
		if (noticeCommand==null) {
			return null;
		}
		Notice notice = new Notice();
		notice.setTitle(noticeCommand.getTitle());
		notice.setDescription(noticeCommand.getDescription());
		notice.setPrice(noticeCommand.getPrice());
		notice.setViews(noticeCommand.getViews());
		notice.setPhoneNumber(noticeCommand.getPhoneNumber());
		notice.setCreationDate(noticeCommand.getCreationDate());
		notice.setCity(noticeCommand.getCity());
		notice.setState(noticeCommand.getState());
		notice.setCategory(categoryService.getCategoryByCategory(noticeCommand.getCategoryEnum()));
		notice.setUser(noticeCommand.getUser());
		notice.setMainImage(storageService.loadAsByteArray(noticeCommand.getMainImageName(), noticeCommand.getUser().getEmail()));
		notice.setImage1(storageService.loadAsByteArray(noticeCommand.getImage1Name(), noticeCommand.getUser().getEmail()));
		notice.setImage2(storageService.loadAsByteArray(noticeCommand.getImage2Name(), noticeCommand.getUser().getEmail()));
		return notice;
	}
	
}
