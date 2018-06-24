package com.noticeboardproject.commands;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.noticeboardproject.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeCommand {
	@NotBlank
	private String title;
	@NotBlank
	private String description;
	@NotNull
	@Min(value=1)
	private Integer price;
	private Integer views;
	@NotBlank
	@Pattern(regexp="^(([\\+]?[0-9]{11})|([0-9]{9}))")
	private String phoneNumber;
	private Date creationDate;
	@NotBlank
	private String city;
	@NotBlank(message="{message.selectCityFromList}")
	private String state;
	@NotEmpty
	private String categoryEnumName;
	private String mainImageName;
	private String image1Name;
	private String image2Name;
	private User user;
}
