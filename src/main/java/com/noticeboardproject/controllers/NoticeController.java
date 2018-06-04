package com.noticeboardproject.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;

@Controller
@SessionAttributes("user")
public class NoticeController {
	
	private NoticeService noticeService;
	
	private UserService userService;
	
	
	@Autowired
	public NoticeController(NoticeService noticeService, UserService userService) {
		this.noticeService = noticeService;
		this.userService = userService;
	}

	@GetMapping("/notice/new")
	public String showAddNewNoticeFrom(Model model, Principal principal) {
		NoticeCommand noticeCommand = new NoticeCommand();
		User user = userService.findUserByEmail(principal.getName());
		model.addAttribute("noticeCommand", noticeCommand);
		model.addAttribute("user", user);
		model.addAttribute("categories", CategoryEnum.values());
		return "notice/addNewNotice";
	}
	
	@PostMapping("/notice/new")
	public String AddNewNotice(Model model, @ModelAttribute("noticeCommand") NoticeCommand noticeCommand, @ModelAttribute("user") User user) {
		noticeCommand.setUser(user);
		Notice savedNotice = noticeService.saveNoticeCommand(noticeCommand);
		return "redirect:/notice/" + savedNotice.getId() + "/show";
	}
	
	@GetMapping("/notice/{id}/show")
	public String showNoticeById(@PathVariable String id, Model model) {
		model.addAttribute("notice", noticeService.findById(new Long(id)));
		return "notice/showNotice";
	}
	
}