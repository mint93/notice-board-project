package com.noticeboardproject.domain;

public enum CategoryEnum {
	AUTOMOTIVE("automotive"),
	ELECTRONICS("electronics"),
	FOR_CHILDREN("forChildren"),
	WEDDING_AND_RECEPTION("weddingAndReception"),
	FASHION("fashion"),
	SPORT_AND_HOBBY("sportAndHobby"),
	AGRICULTURE("agriculture"),
	MUSIC("music"),
	HOUSE_AND_GARDEN("houseAndGarden"),
	ANIMALS("animals"),
	BOOKS("books"),
	GAMES("games");
	
	private String category;
	
	private CategoryEnum(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return this.category;
	}
}
