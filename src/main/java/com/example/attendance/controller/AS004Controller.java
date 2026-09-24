package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.DailyDto;
import com.example.attendance.service.DailyService;

@Controller
public class AS004Controller {

	private final DailyService dailyService;

	public AS004Controller(
			DailyService dailyService) {

		this.dailyService = dailyService;
	}

	@GetMapping("/AS004")
	public String display(
			@RequestParam(required = false) Integer year,

			@RequestParam(required = false) Integer month,

			Authentication authentication,

			Model model) {

		LocalDate today = LocalDate.now();

		/*
		 * 初回表示は現在年月
		 */
		if (year == null) {
			year = today.getYear();
		}

		if (month == null) {
			month = today.getMonthValue();
		}

		LocalDate selectedMonth = LocalDate.of(year, month, 1);

		/*
		 * 前月
		 */
		LocalDate previousMonth = selectedMonth.minusMonths(1);

		/*
		 * 翌月
		 */
		LocalDate nextMonth = selectedMonth.plusMonths(1);

		String userId = authentication.getName();

		List<DailyDto> dailyList = dailyService.getDailyList(userId, year, month);

		model.addAttribute("year", year);

		model.addAttribute("month", month);

		model.addAttribute("previousYear", previousMonth.getYear());

		model.addAttribute("previousMonth", previousMonth.getMonthValue());

		model.addAttribute("nextYear", nextMonth.getYear());

		model.addAttribute("nextMonth", nextMonth.getMonthValue());

		model.addAttribute("dailyList", dailyList);

		return "AS004";
	}
}