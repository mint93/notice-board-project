package com.noticeboardproject.converters;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.CategoryService;
import com.noticeboardproject.storage.StorageService;

@RunWith(MockitoJUnitRunner.class)
public class NoticeCommandToNoticeTest {
	
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final Integer PRICE = 100;
	public static final Integer VIEWS = 10;
	public static final Date CREATION_DATE = new Date(2018, 1, 1);
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final CategoryEnum CATEGORY = CategoryEnum.AUTOMOTIVE;
	public static final String MAIN_IMAGE_NAME = "main image name";
	public static final String IMAGE1_NAME = "image1 name";
	public static final String IMAGE2_NAME = "image2 name";
	public static User user = new User();
	public static final String USERNAME = "user@gmail.com";
	
	private NoticeCommandToNotice converter;
	
	@Mock
	private CategoryService categoryService;
	
	@Mock
	private StorageService storageService;

	@Before
	public void setUp() throws Exception {
		user.setEmail(USERNAME);
		converter = new NoticeCommandToNotice(categoryService, storageService);
	}

	@Test
	public void givenNullObject_whenConvert_thenReturnNull() {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void givenNoticeCommandObject_whenConvert_thenReturnNotice() {
		//given
		NoticeCommand noticeCommand = new NoticeCommand();
		noticeCommand.setTitle(TITLE);
		noticeCommand.setDescription(DESCRIPTION);
		noticeCommand.setPrice(PRICE);
		noticeCommand.setViews(VIEWS);
		noticeCommand.setCreationDate(CREATION_DATE);
		noticeCommand.setCity(CITY);
		noticeCommand.setState(STATE);
		noticeCommand.setCategoryEnumName(CATEGORY.toString());
		noticeCommand.setUser(user);
		noticeCommand.setMainImageName(MAIN_IMAGE_NAME);
		noticeCommand.setImage1Name(IMAGE1_NAME);
		noticeCommand.setImage2Name(IMAGE2_NAME);
		
		Category categoryFromService = new Category();
		categoryFromService.setCategory(CategoryEnum.AUTOMOTIVE);
		when(categoryService.getCategoryByCategory(any())).thenReturn(categoryFromService);
		
		Byte[] mainImageByteArray = new Byte[] {0, 1, 2};
		Byte[] image1ByteArray = new Byte[] {3, 4, 5};
		Byte[] image2ByteArray = new Byte[] {6, 7, 8};
		when(storageService.loadAsByteArray(MAIN_IMAGE_NAME, USERNAME)).thenReturn(mainImageByteArray);
		when(storageService.loadAsByteArray(IMAGE1_NAME, USERNAME)).thenReturn(image1ByteArray);
		when(storageService.loadAsByteArray(IMAGE2_NAME, USERNAME)).thenReturn(image2ByteArray);
		
		//when
		Notice notice = converter.convert(noticeCommand);
		
		//then
		assertEquals(TITLE, notice.getTitle());
		assertEquals(DESCRIPTION, notice.getDescription());
		assertEquals(PRICE, notice.getPrice());
		assertEquals(VIEWS, notice.getViews());
		assertEquals(CREATION_DATE, notice.getCreationDate());
		assertEquals(CITY, notice.getCity());
		assertEquals(STATE, notice.getState());
		assertEquals(CATEGORY, notice.getCategory().getCategory());
		assertEquals(user, notice.getUser());
		assertArrayEquals(mainImageByteArray, notice.getMainImage());
		assertArrayEquals(image1ByteArray, notice.getImage1());
		assertArrayEquals(image2ByteArray, notice.getImage2());
		
	}
}
