package com.example.attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.attendance.dto.ClockRequest;
import com.example.attendance.entity.DateState;
import com.example.attendance.service.ClockService;

@Controller
public class AS003Controller {

	private final ClockService clockService;

	public AS003Controller(ClockService clockService) {
		this.clockService = clockService;
	}

	/**
	 * 打刻画面表示
	 */
	@GetMapping("/AS003")
	public String display(
			Authentication authentication,
			Model model) {

		String userId = authentication.getName();

		DateState todayState = clockService.getTodayState(userId);

		model.addAttribute(
				"todayState",
				todayState);

		return "AS003";
	}

	/**
	 * 出勤
	 */
	@PostMapping("/AS003/clock-in")
	public ResponseEntity<String> clockIn(
			@RequestBody ClockRequest request,
			Authentication authentication) {

		try {
			String userId = authentication.getName();

			clockService.clockIn(
					userId,
					request.getLatitude(),
					request.getLongitude());

			return ResponseEntity.ok("出勤打刻しました。");
		} catch (IllegalStateException e) {
			return ResponseEntity
					.badRequest()
					.body(e.getMessage());
		}
	}

	/**
	 * 退勤
	 */
	@PostMapping("/AS003/clock-out")
	public ResponseEntity<String> clockOut(
			@RequestBody ClockRequest request,
			Authentication authentication) {

		try {
			String userId = authentication.getName();

			clockService.clockOut(
					userId,
					request.getLatitude(),
					request.getLongitude());

			return ResponseEntity.ok("退勤打刻しました。");
		} catch (IllegalStateException e) {
			return ResponseEntity
					.badRequest()
					.body(e.getMessage());
		}
	}
}