package com.noticeboardproject.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.noticeboardproject.domain.Role;
import com.noticeboardproject.repositories.RoleRepository;

@Component
public class RoleBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final RoleRepository roleRepository;

	public RoleBootstrap(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Role roleUser = new Role();
		roleUser.setRole("ROLE_USER");
		roleRepository.save(roleUser);
	}

}
