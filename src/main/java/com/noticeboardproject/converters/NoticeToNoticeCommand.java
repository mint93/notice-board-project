package com.noticeboardproject.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Notice;

import lombok.Synchronized;

@Component
public class NoticeToNoticeCommand implements Converter<Notice, NoticeCommand>{

	@Synchronized
	@Nullable
	@Override
	public NoticeCommand convert(Notice notice) {
		if (notice==null) {
			return null;
		}
		NoticeCommand noticeCommand = new NoticeCommand();
		noticeCommand.setTitle(notice.getTitle());
		noticeCommand.setDescription(notice.getDescription());
		noticeCommand.setPrice(notice.getPrice());
		noticeCommand.setViews(notice.getViews());
		noticeCommand.setPhoneNumber(notice.getPhoneNumber());
		noticeCommand.setCreationDate(notice.getCreationDate());
		noticeCommand.setCity(notice.getCity());
		noticeCommand.setState(notice.getState());
		noticeCommand.setCategoryEnum(notice.getCategory().getCategory());
		noticeCommand.setUser(notice.getUser());
		
		return noticeCommand;
	}

}
