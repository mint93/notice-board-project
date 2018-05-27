package com.noticeboardproject.bootstrap;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;
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
		automotive.setCategory(CategoryEnum.AUTOMOTIVE);
		
		Category electronics = new Category();
		electronics.setCategory(CategoryEnum.ELECTRONICS);
		
		Category forChildren = new Category();
		forChildren.setCategory(CategoryEnum.FOR_CHILDREN);
		
		Category weddingAndReception = new Category();
		weddingAndReception.setCategory(CategoryEnum.WEDDING_AND_RECEPTION);
		
		Category fashion = new Category();
		fashion.setCategory(CategoryEnum.FASHION);
		
		Category sportAndHobby = new Category();
		sportAndHobby.setCategory(CategoryEnum.SPORT_AND_HOBBY);
		
		Category agriculture = new Category();
		agriculture.setCategory(CategoryEnum.AGRICULTURE);
		
		Category music = new Category();
		music.setCategory(CategoryEnum.MUSIC);
		
		Category houseAndGarden = new Category();
		houseAndGarden.setCategory(CategoryEnum.HOUSE_AND_GARDEN);
		
		Category animals = new Category();
		animals.setCategory(CategoryEnum.ANIMALS);
		
		Category books = new Category();
		books.setCategory(CategoryEnum.BOOKS);
		
		Category games = new Category();
		games.setCategory(CategoryEnum.GAMES);
		
		categoryRepository.saveAll(Arrays.asList(automotive, electronics, forChildren, weddingAndReception, fashion, sportAndHobby, agriculture, music, houseAndGarden, animals, books, games));
	}

}
