package com.example.attendance.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.AS005Dto;
import com.example.attendance.entity.UserInfo;
import com.example.attendance.service.AS005Service;
import com.example.attendance.service.UserInfoService;

@Controller
public class AS005Controller {

	private final AS005Service as005Service;

	private final UserInfoService userInfoService;

	public AS005Controller(
			AS005Service as005Service, UserInfoService userInfoService) {
		this.as005Service = as005Service;
		this.userInfoService = userInfoService;
	}

	@GetMapping("/AS005")
	public String display(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			Authentication authentication,
			Model model) {

		String userId = authentication.getName();

		AS005Dto form = as005Service.getForm(userId, date);

		model.addAttribute("form", form);

		// 承認者を設定
		UserInfo userInfo = userInfoService.getUserState(userId);
		UserInfo approverInfo = userInfoService.getUserState(userInfo.getApproverId());
		model.addAttribute("approverName", approverInfo.getUserName());

		return "AS005";
	}

	@PostMapping("/AS005/apply")
	public String apply(
			@ModelAttribute("form") AS005Dto form,
			Authentication authentication) {

		String userId = authentication.getName();

		as005Service.apply(
				userId,
				form);

		return "redirect:/AS004?year="
				+ form.getTargetDate().getYear()
				+ "&month="
				+ form.getTargetDate().getMonthValue();
	}
}