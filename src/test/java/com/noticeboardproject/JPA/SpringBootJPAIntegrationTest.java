package com.noticeboardproject.JPA;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.noticeboardproject.NoticeBoardProjectApplication;
import com.noticeboardproject.domain.Privilege;
import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.PrivilegeRepository;
import com.noticeboardproject.repositories.RoleRepository;
import com.noticeboardproject.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoticeBoardProjectApplication.class)
public class SpringBootJPAIntegrationTest {
  
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PrivilegeRepository privilegeRepository;
 
    @Test
    public void givenUserRepository_whenSaveAndRetreiveEntity_thenOK() {
    	Privilege privilege = new Privilege();
    	privilege.setPrivilege("privilege");
    	
    	Role role = new Role();
    	role.setRole("role");
    	
    	User user = new User();
    	user.setEmail("email@gmail.com");
    	user.setPassword("pass");
    	
    	role.setPrivileges(Arrays.asList(privilege));
    	privilege.setRoles(Arrays.asList(role));
    	user.setRoles(Arrays.asList(role));
    	role.setUsers(Arrays.asList(user));
    	privilegeRepository.save(privilege);
    	roleRepository.save(role);
    	User savedUser = userRepository.save(user);
    	User retrievedUser = userRepository.findById(savedUser.getId()).get();
  
        assertNotNull(retrievedUser);
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(savedUser.getPassword(), retrievedUser.getPassword());
    }
}