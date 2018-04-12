package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	PasswordResetToken findByToken(String token);
}
