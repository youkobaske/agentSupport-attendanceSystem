package com.example.attendance.initialize;

import java.time.LocalDate;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.attendance.service.AttendanceCreateService;
import com.example.attendance.service.HolidayService;

@Component
public class AttendanceInitializer {

	private final HolidayService holidayService;
	private final AttendanceCreateService attendanceCreateService;

	public AttendanceInitializer(
			HolidayService holidayService,
			AttendanceCreateService attendanceCreateService) {

		this.holidayService = holidayService;
		this.attendanceCreateService = attendanceCreateService;
	}

	/**
	 * Spring Boot起動完了後に実行
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void initializeAttendanceData() {

		LocalDate today = LocalDate.now();

		/*
		 * ① 祝日マスタを同期
		 */
		holidayService.syncCurrentAndNextYear();

		/*
		 * ② 当月のmonth_stateを作成
		 * すでに存在する場合は何もしない
		 */
		attendanceCreateService.createMonthStates(
				today.getYear(),
				today.getMonthValue());

		/*
		 * ③ 当月1日～今日までのdate_stateを補完
		 */
		LocalDate targetDate = today.withDayOfMonth(1);

		while (!targetDate.isAfter(today)) {

			attendanceCreateService.createDateStates(targetDate);

			targetDate = targetDate.plusDays(1);
		}
	}
}