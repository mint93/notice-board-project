package com.noticeboardproject.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.Notice;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryIntergrationTest {
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	NoticeRepository noticeRepository;

	Category category;
	Notice notice1;
	Notice notice2;
	
	@Before
	public void setUp() throws Exception {
		notice1 = new Notice();
		notice1.setTitle("title1");
		notice1.setDescription("description1");
		notice2 = new Notice();
		notice2.setTitle("title2");
		notice2.setDescription("description2");
		
		category = new Category();
		category.setCategory("category");
		
		noticeRepository.saveAll(Arrays.asList(notice1, notice2));
		category.addNotice(notice1);
		category.addNotice(notice2);
		
		categoryRepository.save(category);
	}

	@Test
	public void findByCategory_shouldntContainNotices() {
		Category foundCategory = categoryRepository.findByCategory(category.getCategory());
		assertEquals(category, foundCategory);
		Set<Notice> notices = foundCategory.getNotices();
		assertTrue(notices.containsAll(Arrays.asList(notice1, notice2)));
	}
	
}
