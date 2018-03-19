package com.noticeboardproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.noticeboardproject.domain.Role;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.repositories.UserRepository;


@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
  
    @Autowired
    private UserRepository userRepository;
    
    @Override
	public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
  
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(
              "No user found with username: "+ email);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new org.springframework.security.core.userdetails.User
          (user.getEmail(), 
          user.getPassword().toLowerCase(), enabled, accountNonExpired, 
          credentialsNonExpired, accountNonLocked, 
          getAuthorities(user.getRoles()));
    }
    
    //for now only Role functionality
    private static List<GrantedAuthority> getAuthorities (Set<Role> roles) {
    	List<GrantedAuthority> authorities = new ArrayList<>();
    	for(Role role : roles) {
    		authorities.add(new SimpleGrantedAuthority(role.getRole()));
    	}
        return authorities;
    }
}