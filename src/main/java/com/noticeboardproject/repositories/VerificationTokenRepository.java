package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.User;
import com.noticeboardproject.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	
	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);
}
