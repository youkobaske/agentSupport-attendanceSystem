package com.example.attendance.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.dto.AS007Form;
import com.example.attendance.entity.UserInfo;
import com.example.attendance.service.AS007Service;
import com.example.attendance.service.UserInfoService;

@Controller
public class AS007Controller {

	private final AS007Service as007Service;

	private final UserInfoService userInfoService;

	public AS007Controller(
			AS007Service as007Service,
			UserInfoService userInfoService) {

		this.as007Service = as007Service;
		this.userInfoService = userInfoService;
	}

	/**
	 * 初期表示
	 */
	@GetMapping("/AS007")
	public String display(Authentication authentication, Model model) {

		model.addAttribute("form", new AS007Form());

		// 承認者設定
		String userId = authentication.getName();
		UserInfo userInfo = userInfoService.getUserState(userId);
		UserInfo approverInfo = userInfoService.getUserState(userInfo.getApproverId());
		model.addAttribute("approverName", approverInfo.getUserName());

		return "AS007";
	}

	/**
	 * 休暇申請
	 */
	@PostMapping("/AS007/apply")
	public String apply(
			@ModelAttribute("form") AS007Form form,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {

		String userId = authentication.getName();

		try {
			as007Service.applyVacation(userId, form);

		} catch (IllegalStateException e) {

			model.addAttribute("errorMessage", e.getMessage());

			return "AS007";
		}

		// リダイレクト先へ1回だけ渡す
		redirectAttributes.addFlashAttribute(
				"successMessage",
				"休暇申請が完了しました。");

		/*
		 * 処理完了後は申請画面へ
		 */
		return "redirect:/AS006";
	}
}