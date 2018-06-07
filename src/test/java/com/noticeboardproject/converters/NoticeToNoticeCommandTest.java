package com.noticeboardproject.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;

public class NoticeToNoticeCommandTest {
	
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final Integer PRICE = 100;
	public static final Integer VIEWS = 10;
	public static final Date CREATION_DATE = new Date(2018, 1, 1);
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static Category category = new Category();
	public static User user = new User();
	public static final String USERNAME = "user@gmail.com";
	
	private NoticeToNoticeCommand converter;
	
	@Before
	public void setUp() throws Exception {
		user.setEmail(USERNAME);
		category.setCategory(CategoryEnum.AUTOMOTIVE);
		converter = new NoticeToNoticeCommand();
	}

	@Test
	public void givenNullObject_whenConvert_thenReturnNull() {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void givenNoticeCommandObject_whenConvert_thenReturnNotice() {
		//given
		Notice notice = new Notice();
		notice.setTitle(TITLE);
		notice.setDescription(DESCRIPTION);
		notice.setPrice(PRICE);
		notice.setViews(VIEWS);
		notice.setCreationDate(CREATION_DATE);
		notice.setCity(CITY);
		notice.setState(STATE);
		notice.setCategory(category);
		notice.setUser(user);
		
		//when
		NoticeCommand noticeCommand = converter.convert(notice);
		
		//then
		assertEquals(TITLE, noticeCommand.getTitle());
		assertEquals(DESCRIPTION, noticeCommand.getDescription());
		assertEquals(PRICE, noticeCommand.getPrice());
		assertEquals(VIEWS, noticeCommand.getViews());
		assertEquals(CREATION_DATE, noticeCommand.getCreationDate());
		assertEquals(CITY, noticeCommand.getCity());
		assertEquals(STATE, noticeCommand.getState());
		assertEquals(category.getCategory(), CategoryEnum.valueOf(noticeCommand.getCategoryEnumName()));
		assertEquals(user, noticeCommand.getUser());		
	}
}
