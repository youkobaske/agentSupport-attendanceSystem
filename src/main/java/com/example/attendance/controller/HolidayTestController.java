package com.example.attendance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.service.HolidayService;

@RestController
public class HolidayTestController {

	private final HolidayService holidayService;

	public HolidayTestController(
			HolidayService holidayService) {

		this.holidayService = holidayService;
	}

	@GetMapping("/test/holiday-import")
	public String importHoliday(
			@RequestParam int year) {

		int count = holidayService.importHolidays(year);

		return year
				+ "年の祝日を"
				+ count
				+ "件取り込みました。";
	}
}