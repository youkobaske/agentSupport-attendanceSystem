package com.example.attendance.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.DailyDto;
import com.example.attendance.entity.Apply;
import com.example.attendance.entity.UserInfo;
import com.example.attendance.service.DailyService;

@Controller
public class AS010Controller {

	private final DailyService dailyService;

	public AS010Controller(
			DailyService dailyService) {
		this.dailyService = dailyService;
	}

	@GetMapping("/AS010")
	public String show(
			@RequestParam String userId,
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			Model model) {

		YearMonth targetMonth;

		if (year == null || month == null) {
			targetMonth = YearMonth.now();
		} else {
			targetMonth = YearMonth.of(year, month);
		}

		Integer targetYear = targetMonth.getYear();
		Integer targetMonthValue = targetMonth.getMonthValue();

		UserInfo user = dailyService.getUser(userId);

		List<DailyDto> dailyList = dailyService.getDailyList(userId, targetYear, targetMonthValue);

		List<Apply> applications = dailyService.getPendingApplications(userId, targetYear, targetMonthValue);

		model.addAttribute("user", user);
		model.addAttribute("dailyList", dailyList);
		model.addAttribute("applications", applications);
		model.addAttribute("hasApplication", !applications.isEmpty());
		model.addAttribute("year", targetYear);
		model.addAttribute("month", targetMonthValue);
		model.addAttribute("previousMonth", targetMonth.minusMonths(1));
		model.addAttribute("nextMonth", targetMonth.plusMonths(1));

		return "AS010";
	}
}