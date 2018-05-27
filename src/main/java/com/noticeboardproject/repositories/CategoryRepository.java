package com.noticeboardproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategory(CategoryEnum category);
}
