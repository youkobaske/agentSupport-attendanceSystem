package com.example.attendance.service;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.MonthState;
import com.example.attendance.mapper.MonthStateMapper;

@Service
public class MonthStateService {

	private final MonthStateMapper monthStateMapper;

	public MonthStateService(MonthStateMapper monthStateMapper) {
		this.monthStateMapper = monthStateMapper;
	}

	public MonthState getMonthState(String userId, short year, short month) {

		return monthStateMapper.selectByUserIdAndYearMonth(userId, year, month);
	}

	/**
	 * 
	 */
	public String formatMinutes(Integer minutes) {

		if (minutes == null) {
			return "0:00";
		}

		int hour = minutes / 60;
		int minute = minutes % 60;

		return String.format("%d:%02d", hour, minute);
	}
}