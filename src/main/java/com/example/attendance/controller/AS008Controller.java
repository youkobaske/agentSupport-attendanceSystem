package com.example.attendance.controller;

import java.time.YearMonth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.dto.AS008Dto;
import com.example.attendance.service.AS008Service;

@Controller
public class AS008Controller {

	private final AS008Service as008Service;

	public AS008Controller(AS008Service as008Service) {
		this.as008Service = as008Service;
	}

	@GetMapping("/AS008")
	public String show(
			Authentication authentication,
			Model model) {

		String userId = authentication.getName();

		YearMonth previousMonth = YearMonth.now().minusMonths(1);

		AS008Dto summary = as008Service.getPreviousMonthSummary(userId);

		model.addAttribute("summary", summary);
		model.addAttribute(
				"year",
				previousMonth.getYear());
		model.addAttribute(
				"month",
				previousMonth.getMonthValue());

		return "AS008";
	}

	@PostMapping("/AS008/apply")
	public String apply(
			Authentication authentication,
			RedirectAttributes redirectAttributes) {

		String userId = authentication.getName();

		as008Service.applyPreviousMonth(userId);

		// リダイレクト先へ1回だけ渡す
		redirectAttributes.addFlashAttribute(
				"successMessage",
				"勤怠締め申請が完了しました。");

		return "redirect:/AS006";
	}
}