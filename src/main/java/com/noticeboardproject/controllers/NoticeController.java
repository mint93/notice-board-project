package com.noticeboardproject.controllers;

import java.security.Principal;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.noticeboardproject.commands.NoticeCommand;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.domain.SearchBy;
import com.noticeboardproject.domain.State;
import com.noticeboardproject.domain.User;
import com.noticeboardproject.services.NoticeService;
import com.noticeboardproject.services.UserService;

@Controller
@SessionAttributes("user")
public class NoticeController {
	
	private NoticeService noticeService;
	
	private UserService userService;
	
	private final int MAX_NOTICES_PER_PAGE = 10;
	
	
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
	public String AddNewNotice(Model model, @ModelAttribute("noticeCommand") @Valid NoticeCommand noticeCommand, BindingResult bindingResult, @ModelAttribute("user") User user) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", CategoryEnum.values());
			return "notice/addNewNotice";
		}
		
		noticeCommand.setUser(user);
		noticeCommand.setCreationDate(Calendar.getInstance().getTime());
		noticeCommand.setViews(0);
		Notice savedNotice = noticeService.saveNoticeCommand(noticeCommand);
		return "redirect:/notice/" + savedNotice.getId() + "/show";
	}
	
	@GetMapping("/notice/{id}/show")
	public String showNoticeById(@PathVariable String id, Model model) {
		Notice foundNotice = noticeService.findById(new Long(id));
		foundNotice.incrementViews();
		Notice updatedNotice = noticeService.saveOrUpdateNotice(foundNotice);
		model.addAttribute("notice", updatedNotice);
		return "notice/showNotice";
	}
	
	@GetMapping("/search")
	public String searchNotices(Model model, @RequestParam(defaultValue="1", name="page") int page, @ModelAttribute("searchBy") SearchBy searchBy, @RequestParam(name="category", required=false) CategoryEnum categoryEnum) {
		searchBy.setCategory(categoryEnum);
		searchBy.setSearchByWholeState(State.generateStates().stream()
			.map(State::getState)
			.anyMatch(state -> state.equals(searchBy.getCity())));
		
		Page<Notice> notices = noticeService.findBySearchTerm(PageRequest.of(page-1, MAX_NOTICES_PER_PAGE, new Sort(Sort.Direction.ASC, "creationDate")), searchBy);
		if (page>notices.getSize()) {
			page = notices.getSize();
		}
		model.addAttribute("categories", CategoryEnum.values());
		model.addAttribute("states", State.generateStates());
		model.addAttribute("notices", notices);
		model.addAttribute("currentPage", page);
		model.addAttribute("searchBy", searchBy);
		return "notice/listNotices";
	}
	
}