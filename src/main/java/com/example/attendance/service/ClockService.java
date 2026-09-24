package com.example.attendance.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.entity.DateState;
import com.example.attendance.mapper.DateStateMapper;

@Service
public class ClockService {

	private final DateStateMapper dateStateMapper;

	public ClockService(DateStateMapper dateStateMapper) {
		this.dateStateMapper = dateStateMapper;
	}

	public DateState getTodayState(String userId) {

		return dateStateMapper.selectTodayByUserId(userId);
	}

	@Transactional
	public void clockIn(
			String userId,
			Double latitude,
			Double longitude) {

		DateState dateState = dateStateMapper.selectTodayByUserId(userId);

		if (dateState == null) {
			throw new IllegalStateException(
					"本日の勤怠データが存在しません。");
		}

		if (dateState.getWorkStarttime() != null) {
			throw new IllegalStateException(
					"すでに出勤打刻されています。");
		}

		LocalTime now = LocalTime.now().withNano(0);

		dateStateMapper.updateClockIn(
				dateState.getDailyId(),
				now,
				latitude,
				longitude);
	}

	@Transactional
	public void clockOut(
			String userId,
			Double latitude,
			Double longitude) {

		DateState dateState = dateStateMapper.selectTodayByUserId(userId);

		if (dateState == null) {
			throw new IllegalStateException(
					"本日の勤怠データが存在しません。");
		}

		if (dateState.getWorkStarttime() == null) {
			throw new IllegalStateException(
					"先に出勤打刻を行ってください。");
		}

		if (dateState.getWorkEndtime() != null) {
			throw new IllegalStateException(
					"すでに退勤打刻されています。");
		}

		LocalTime now = LocalTime.now().withNano(0);

		dateStateMapper.updateClockOut(
				dateState.getDailyId(),
				now,
				latitude,
				longitude);
	}
}