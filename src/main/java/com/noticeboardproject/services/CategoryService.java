package com.noticeboardproject.services;

import java.util.Set;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;

public interface CategoryService {
	Set<Category> getAllCategories();
	Category getCategoryByCategory(CategoryEnum category);
	Category getCategoryById(Long id);
}
