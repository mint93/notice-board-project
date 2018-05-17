package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategory(String category);
	
}
