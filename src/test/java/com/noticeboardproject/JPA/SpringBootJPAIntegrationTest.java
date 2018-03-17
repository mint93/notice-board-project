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
    	Privilege privilege1 = new Privilege();
    	privilege1.setPrivilege("privilege1");
    	
    	Role role1 = new Role();
    	role1.setRole("role1");
    	
    	User user = new User();
    	user.setEmail("email@gmail.com");
    	user.setPassword("pass");
    	
    	role1.setPrivileges(Arrays.asList(privilege1));
    	privilege1.setRoles(Arrays.asList(role1));
    	user.setRoles(Arrays.asList(role1));
    	role1.setUsers(Arrays.asList(user));
    	privilegeRepository.save(privilege1);
    	roleRepository.save(role1);
    	User savedUser = userRepository.save(user);
    	User retrievedUser = userRepository.findById(savedUser.getId()).get();
  
        assertNotNull(retrievedUser);
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(savedUser.getPassword(), retrievedUser.getPassword());
        assertEquals(savedUser.getId(), retrievedUser.getId());
        java.util.List<Role> savedRoles = savedUser.getRoles();
        java.util.List<Role> retrievedRoles = retrievedUser.getRoles();
        assertEquals(savedRoles.size(), retrievedRoles.size());
        
        for(int i=0; i<savedRoles.size(); i++) {
        	assertEquals(savedRoles.get(i).getRole(), retrievedRoles.get(i).getRole());
        	assertEquals(savedRoles.get(i).getPrivileges().size(), retrievedRoles.get(i).getPrivileges().size());
        	for(int j=0; j<savedRoles.get(i).getPrivileges().size(); j++) {
        		assertEquals(savedRoles.get(i).getPrivileges().get(j).getPrivilege(), retrievedRoles.get(i).getPrivileges().get(j).getPrivilege());
        	}
        }
        
    }
}