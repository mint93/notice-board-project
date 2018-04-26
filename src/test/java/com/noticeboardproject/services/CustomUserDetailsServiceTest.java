package com.noticeboardproject.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.noticeboardproject.domain.Privilege;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.UserRepository;

public class CustomUserDetailsServiceTest {

	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
	
	private final String EMAIL = "emal";
	private final String PASSWORD = "password";
	private final boolean ENABLED = true;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void givenNotExistingEmail_whenLoadUserByUserName_thenThrowException() {
		when(userRepository.findByEmail(any(String.class))).thenReturn(null);
		customUserDetailsService.loadUserByUsername("notExistingEmail");
	}
	
	@Test
	public void givenExistingEmail_whenLoadUserByUserName_thenReturnUser() {
		Privilege privilege1 = new Privilege();
		privilege1.setPrivilege("privilege1");
		Privilege privilege2 = new Privilege();
		privilege2.setPrivilege("privilege2");
		Role role1 = new Role();
		role1.setRole("role1");
		role1.getPrivileges().add(privilege1);
		role1.getPrivileges().add(privilege2);
		
		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		user.getRoles().add(role1);
		user.setEnabled(ENABLED);
		
		when(userRepository.findByEmail(any(String.class))).thenReturn(user);
		UserDetails userDetail = customUserDetailsService.loadUserByUsername(user.getEmail());
		assertEquals(user.getEmail(), userDetail.getUsername());
		assertEquals(user.getPassword(), userDetail.getPassword());
		assertEquals(user.getRoles().stream()
							.flatMap(role -> role.getPrivileges().stream())
							.map(privelege -> privelege.getPrivilege())
							.sorted()
							.collect(Collectors.toList()), 
					 userDetail.getAuthorities().stream()
							.map(authority -> authority.getAuthority())
							.sorted()
							.collect(Collectors.toList()));
		assertEquals(user.isEnabled(), userDetail.isEnabled());
		assertEquals(true, userDetail.isCredentialsNonExpired());
		assertEquals(true, userDetail.isAccountNonExpired());
		assertEquals(true, userDetail.isAccountNonLocked());
		
		verify(userRepository, timeout(1)).findByEmail(any());
		verifyNoMoreInteractions(userRepository);
	}
}
