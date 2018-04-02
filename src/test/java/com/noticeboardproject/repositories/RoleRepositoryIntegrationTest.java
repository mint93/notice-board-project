package com.noticeboardproject.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.domain.Role;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryIntegrationTest {
	
	@Autowired
	RoleRepository roleRepository;
	
	Role role;
	
	@Before
	public void setUp() throws Exception {
		role = new Role();
		role.setRole("role");
		
		roleRepository.save(role);
	}

	@Test
	public void findByRole() {
		Role foundRole = roleRepository.findByRole(role.getRole());
		assertEquals(role, foundRole);
	}

}