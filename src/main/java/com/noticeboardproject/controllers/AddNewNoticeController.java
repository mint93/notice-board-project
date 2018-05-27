package com.noticeboardproject.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;

@Controller
@SessionAttributes("user")
public class AddNewNoticeController {
	
	private NoticeService noticeService;
	
	private UserService userService;
	
	
	@Autowired
	public AddNewNoticeController(NoticeService noticeService, UserService userService) {
		this.noticeService = noticeService;
		this.userService = userService;
	}

	@GetMapping("/addNewNotice")
	public String showAddNewNoticeFrom(Model model, Principal principal) {
		NoticeCommand noticeCommand = new NoticeCommand();
		User user = userService.findUserByEmail(principal.getName());
		model.addAttribute("noticeCommand", noticeCommand);
		model.addAttribute("user", user);
		model.addAttribute("categories", CategoryEnum.values());
		return "addNewNotice";
	}
	
	@PostMapping("/addNewNotice")
	public String AddNewNotice(Model model, @ModelAttribute("noticeCommand") NoticeCommand noticeCommand, @ModelAttribute("user") User user) {
		noticeCommand.setUser(user);
		noticeService.saveNoticeCommand(noticeCommand);
		//todo: add new view for correct notice insertion
		return "redirect:/";
	}
	
}