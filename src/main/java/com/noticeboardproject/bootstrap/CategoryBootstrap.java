package com.noticeboardproject.bootstrap;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.repositories.CategoryRepository;

@Component
public class CategoryBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private final CategoryRepository categoryRepository;

	public CategoryBootstrap(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		createCategories();
	}
	
	private void createCategories() {
		Category automotive = new Category();
		automotive.setCategory("automotive");
		
		Category electronics = new Category();
		electronics.setCategory("electronics");
		
		Category forChildren = new Category();
		forChildren.setCategory("forChildren");
		
		Category weddingAndReception = new Category();
		weddingAndReception.setCategory("weddingAndReception");
		
		Category fashion = new Category();
		fashion.setCategory("fashion");
		
		Category sportAndHobby = new Category();
		sportAndHobby.setCategory("sportAndHobby");
		
		Category agriculture = new Category();
		agriculture.setCategory("agriculture");
		
		Category music = new Category();
		music.setCategory("music");
		
		Category houseAndGarden = new Category();
		houseAndGarden.setCategory("houseAndGarden");
		
		Category animals = new Category();
		animals.setCategory("animals");
		
		Category books = new Category();
		books.setCategory("books");
		
		Category games = new Category();
		games.setCategory("games");
		
		categoryRepository.saveAll(Arrays.asList(automotive, electronics, forChildren, weddingAndReception, fashion, sportAndHobby, agriculture, music, houseAndGarden, animals, books, games));
	}

}
