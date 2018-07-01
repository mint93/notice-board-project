package com.noticeboardproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.SearchBy;
import com.noticeboardproject.domain.State;
import com.noticeboardproject.services.CategoryService;

@Controller
public class IndexController {
	
	@Autowired
	MessageSource messages;
	
	@Autowired
	CategoryService categoryService;

	@GetMapping("/")
	public String showRegistrationFrom(Model model, WebRequest request) {
		model.addAttribute("states", State.generateStates());
		model.addAttribute("categories", CategoryEnum.values());
		model.addAttribute("searchBy", new SearchBy());
		model.addAttribute("searchMessage", messages.getMessage("index.placeholder.search", null, request.getLocale()));
		return "index";
	}
	
}
