package com.example.attendance.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.AS009SummaryDto;
import com.example.attendance.dto.AS009UserDto;
import com.example.attendance.service.AS009Service;

@Controller
public class AS009Controller {

	private final AS009Service as009Service;

	public AS009Controller(AS009Service as009Service) {
		this.as009Service = as009Service;
	}

	@GetMapping("/AS009")
	public String display(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			Authentication authentication,
			Model model) {

		YearMonth current = YearMonth.now();

		if (year == null || month == null) {
			year = current.getYear();
			month = current.getMonthValue();
		}

		YearMonth target = YearMonth.of(year, month);

		String approverId = authentication.getName();

		List<AS009UserDto> users = as009Service.getUsers(
				approverId,
				year,
				month);

		AS009SummaryDto summary = as009Service.createSummary(users);

		model.addAttribute("users", users);
		model.addAttribute("summary", summary);

		model.addAttribute("year", year);
		model.addAttribute("month", month);

		model.addAttribute(
				"previousYear",
				target.minusMonths(1).getYear());

		model.addAttribute(
				"previousMonth",
				target.minusMonths(1).getMonthValue());

		model.addAttribute(
				"nextYear",
				target.plusMonths(1).getYear());

		model.addAttribute(
				"nextMonth",
				target.plusMonths(1).getMonthValue());

		return "AS009";
	}
}