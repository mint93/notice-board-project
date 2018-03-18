package com.noticeboardproject.JPA;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

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
    	Privilege privilege2 = new Privilege();
    	privilege2.setPrivilege("privilege2");
    	
    	Role role1 = new Role();
    	role1.setRole("role1");
    	Role role2 = new Role();
    	role2.setRole("role2");
    	
    	User user = new User();
    	user.setEmail("email@gmail.com");
    	user.setPassword("pass");
    	
    	role1.setPrivileges(new HashSet<>(Arrays.asList(privilege1, privilege2)));
    	role2.setPrivileges(new HashSet<>(Arrays.asList(privilege1, privilege2)));
    	privilege1.setRoles(new HashSet<>(Arrays.asList(role1, role2)));
    	privilege2.setRoles(new HashSet<>(Arrays.asList(role1, role2)));
    	user.setRoles(new HashSet<>(Arrays.asList(role1, role2)));
    	role1.setUsers(new HashSet<>(Arrays.asList(user)));
    	role2.setUsers(new HashSet<>(Arrays.asList(user)));
    	privilegeRepository.save(privilege1);
    	privilegeRepository.save(privilege2);
    	roleRepository.save(role1);
    	roleRepository.save(role2);
    	User savedUser = userRepository.save(user);
    	User retrievedUser = userRepository.findById(savedUser.getId()).get();
  
        assertNotNull(retrievedUser);
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(savedUser.getPassword(), retrievedUser.getPassword());
        assertEquals(savedUser.getId(), retrievedUser.getId());
        java.util.Set<Role> savedRoles = savedUser.getRoles();
        java.util.Set<Role> retrievedRoles = retrievedUser.getRoles();
        assertEquals(savedRoles.size(), retrievedRoles.size());
        
        boolean contain = false;
        assertArrayEquals(savedRoles.toArray(), retrievedRoles.toArray());
        for(Role savedRole : savedRoles) {
        	for(Role retrievedRole : retrievedRoles) {
            	if(savedRole.getPrivileges().containsAll(retrievedRole.getPrivileges())) {
            		assertEquals(savedRole.getPrivileges(), retrievedRole.getPrivileges());
            		contain = true;
            		break;
            	}
            }
        	assertTrue(contain);
        	contain=false;
        }
    }
}