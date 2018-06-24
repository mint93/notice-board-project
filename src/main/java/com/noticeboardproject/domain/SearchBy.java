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
	private CategoryEnum category;
}
