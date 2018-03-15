package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
}
