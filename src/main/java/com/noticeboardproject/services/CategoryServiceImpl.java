package com.noticeboardproject.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noticeboardproject.domain.Category;
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

}
