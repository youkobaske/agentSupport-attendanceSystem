package com.example.attendance.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.MonthStateMapper;

@Service
public class AttendanceCreateService {

	private final MonthStateMapper monthStateMapper;
	private final DateStateMapper dateStateMapper;

	public AttendanceCreateService(MonthStateMapper monthStateMapper, DateStateMapper dateStateMapper) {

		this.monthStateMapper = monthStateMapper;
		this.dateStateMapper = dateStateMapper;
	}

	/**
	 * 月次データ作成
	 */
	@Transactional
	public void createMonthStates(int year, int month) {

		monthStateMapper.insertMonthStates(year, month);
	}

	/**
	 * 日次データ作成
	 */
	@Transactional
	public void createDateStates(LocalDate targetDate) {

		dateStateMapper.insertDateStates(targetDate);
	}
}