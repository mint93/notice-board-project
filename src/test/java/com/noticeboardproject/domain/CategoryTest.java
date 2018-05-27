package com.noticeboardproject.domain;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class CategoryTest {
	@Test
	public void categoryHashCodeAndEqualstest(){
		Category category1Group1 = new Category();
		category1Group1.setId(1L);
		category1Group1.setCategory(CategoryEnum.AUTOMOTIVE);
		
		Category category2Group1 = new Category();
		category2Group1.setId(2L);                     
		category2Group1.setCategory(CategoryEnum.AUTOMOTIVE);;
		
		Category category1Group2 = new Category();
		category1Group2.setId(3L);                     
		category1Group2.setCategory(CategoryEnum.ELECTRONICS);            
		
		Category category2Group2 = new Category();
		category2Group2.setId(4L);                     
		category2Group2.setCategory(CategoryEnum.ELECTRONICS); 
		
		new EqualsTester()
			.addEqualityGroup(category1Group1, category2Group1)
			.addEqualityGroup(category1Group2, category2Group2)
			.testEquals();
	}
}
