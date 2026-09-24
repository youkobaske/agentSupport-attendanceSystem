package com.example.attendance.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.attendance.service.AttendanceCreateService;

@Component
public class AttendanceScheduler {

	private final AttendanceCreateService attendanceCreateService;

	public AttendanceScheduler(
			AttendanceCreateService attendanceCreateService) {

		this.attendanceCreateService = attendanceCreateService;
	}

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
	public void createDailyData() {

		LocalDate today = LocalDate.now();

		attendanceCreateService.createMonthStates(
				today.getYear(),
				today.getMonthValue());

		attendanceCreateService.createDateStates(
				today);
	}
}