package com.noticeboardproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchBy {
	private String title = "";
	private String city = "";
	private String priceFrom = "";
	private String priceTo = "";
	private boolean searchInDescription;
	private boolean onlyWithImage;
	private boolean searchByWholeState;
	private CategoryEnum category;
}
