package com.noticeboardproject.domain;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class RoleTest {
	
	@Test
	public void roleHashCodeAndEqualstest(){
		Role role1Group1 = new Role();
		role1Group1.setRole("role1");
		role1Group1.setId(1L);
		
		Role role2Group1 = new Role();
		role2Group1.setRole("role1");
		role2Group1.setId(2L);
		
		Role role1Group2 = new Role();
		role1Group2.setRole("role2");
		role1Group2.setId(3L);
		
		Role role2Group2 = new Role();
		role2Group2.setRole("role2");
		role2Group2.setId(4L);
		
		new EqualsTester()
			.addEqualityGroup(role1Group1, role2Group1)
			.addEqualityGroup(role1Group2, role2Group2)
			.testEquals();
	}
}
