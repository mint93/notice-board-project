package com.noticeboardproject.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository=categoryRepository;
	}
	
	@Override
	public Set<Category> getAllCategories() {
		return new HashSet<>(categoryRepository.findAll());
	}

	@Override
	public Category getCategoryByCategory(CategoryEnum category) {
		return categoryRepository.findByCategory(category);
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).get();
	}

}
