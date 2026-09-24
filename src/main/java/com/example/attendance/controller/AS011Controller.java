package com.example.attendance.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.dto.AS011Dto;
import com.example.attendance.service.AS011Service;

@Controller
public class AS011Controller {

	private final AS011Service as011Service;

	public AS011Controller(
			AS011Service as011Service) {

		this.as011Service = as011Service;
	}

	@GetMapping("/AS011")
	public String show(
			@RequestParam Long applyId,
			Model model) {

		AS011Dto application = as011Service.getApplication(applyId);

		model.addAttribute(
				"applicationData",
				application);

		return "AS011";
	}

	/*
	 * 承認
	 */
	@PostMapping("/AS011/approve")
	public String approve(
			@RequestParam Long applyId,
			@RequestParam(required = false) String remark,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {

		String approverId = authentication.getName();

		as011Service.approve(
				applyId,
				approverId,
				remark);

		redirectAttributes.addFlashAttribute(
				"successMessage",
				"申請の承認が完了しました。");

		return "redirect:/AS009";
	}

	/*
	 * 差戻し
	 */
	@PostMapping("/AS011/return")
	public String returnApplication(
			@RequestParam Long applyId,
			@RequestParam(required = false) String remark,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {

		AS011Dto application = as011Service.getApplication(applyId);

		/*
		 * 画面側だけでなくサーバーでも必須チェック
		 */
		if (remark == null
				|| remark.isBlank()) {

			model.addAttribute(
					"applicationData",
					application);

			model.addAttribute(
					"errorMessage",
					"差戻しを行う場合は備考欄に差戻し理由をを記載してください。");

			return "AS011";
		}

		String approverId = authentication.getName();

		as011Service.returnApplication(
				applyId,
				approverId,
				remark);

		redirectAttributes.addFlashAttribute(
				"successMessage",
				"申請の差戻しが完了しました。");

		return "redirect:/AS009";
	}
}