package com.example.attendance.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.attendance.service.HolidayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HolidayScheduler {

	private final HolidayService holidayService;

	public HolidayScheduler(
			HolidayService holidayService) {

		this.holidayService = holidayService;
	}

	@Scheduled(cron = "0 0 3 1 * *", zone = "Asia/Tokyo")
	public void syncHolidays() {

		log.info("祝日情報同期処理を開始します。");

		int count = holidayService.syncCurrentAndNextYear();

		log.info("祝日情報同期処理が終了しました。count={}", count);
	}
}