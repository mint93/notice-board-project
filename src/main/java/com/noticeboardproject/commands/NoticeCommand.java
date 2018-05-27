package com.noticeboardproject.commands;

import java.util.Date;

import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeCommand {
	private String title;
	private String description;
	private Integer price;
	private Integer views;
	private Date creationDate;
	private String city;
	private String state;
	private CategoryEnum categoryEnum;
	private String mainImageName;
	private String image1Name;
	private String image2Name;
	private User user;
}
