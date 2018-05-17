package com.noticeboardproject.domain;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class NoticeTest {
	@Test
	public void noticeHashCodeAndEqualstest(){
		Notice notice1Group1 = new Notice();
		notice1Group1.setId(1L);
		notice1Group1.setTitle("title1");
		notice1Group1.setDescription("description1");
		notice1Group1.setPrice(1000);
		
		Notice notice2Group1 = new Notice();
		notice2Group1.setId(1L);                     
		notice2Group1.setTitle("title2");            
		notice2Group1.setDescription("description2");
		notice2Group1.setPrice(2000);                
		
		Notice notice1Group2 = new Notice();
		notice1Group2.setId(2L);                     
		notice1Group2.setTitle("title1");            
		notice1Group2.setDescription("description1");
		notice1Group2.setPrice(1000);                
		
		Notice notice2Group2 = new Notice();
		notice2Group2.setId(2L);                     
		notice2Group2.setTitle("title2");            
		notice2Group2.setDescription("description2");
		notice2Group2.setPrice(2000);                
		
		new EqualsTester()
			.addEqualityGroup(notice1Group1, notice2Group1)
			.addEqualityGroup(notice1Group2, notice2Group2)
			.testEquals();
	}
}
