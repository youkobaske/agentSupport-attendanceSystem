package com.example.attendance.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AS005Dto;
import com.example.attendance.entity.DateState;
import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.MonthStateMapper;

@Service
public class AS005Service {

	private final DateStateMapper dateStateMapper;

	private final MonthStateMapper monthStateMapper;

	public AS005Service(DateStateMapper dateStateMapper, MonthStateMapper monthStateMapper) {
		this.dateStateMapper = dateStateMapper;
		this.monthStateMapper = monthStateMapper;
	}

	public AS005Dto getForm(
			String userId,
			LocalDate targetDate) {

		DateState state = dateStateMapper.selectByUserIdAndTargetDate(
				userId, targetDate);

		AS005Dto form = new AS005Dto();

		form.setTargetDate(targetDate);

		/*
		 * DBデータなし
		 * → 新規入力モード
		 */
		if (state == null) {

			form.setEditMode(false);

			return form;
		}

		/*
		 * DBデータあり
		 * → 編集モード
		 */
		form.setEditMode(true);

		form.setDailyId(
				state.getDailyId());

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

		if (state.getWorkStarttime() != null) {

			form.setClockInTarget(true);

			form.setWorkStarttime(
					formatter.format(
							state.getWorkStarttime()));
		}

		if (state.getWorkEndtime() != null) {

			form.setClockOutTarget(true);

			form.setWorkEndtime(
					formatter.format(
							state.getWorkEndtime()));
		}

		if (state.getEditReason() != null) {

			form.setReason(state.getEditReason());
		}

		if (state.getWorkBreaktime() != null) {

			form.setWorkBreaktime(
					formatMinutesForInput(
							state.getWorkBreaktime()));
		}

		return form;
	}

	@Transactional
	public void apply(String userId, AS005Dto form) {

		/*
		 * 休憩時間
		 */
		Integer workBreaktime = convertTimeToMinutes(
				form.getWorkBreaktime());

		/*
		 * 実働時間
		 */
		Integer workActualtime = calculateActualTime(
				form.getWorkStarttime(),
				form.getWorkEndtime(),
				form.getWorkBreaktime());

		/*
		 * 残業時間
		 */
		Integer workOvertime = calculateOvertime(workActualtime);

		/*
		 * 日次データ
		 */
		if (form.getDailyId() != null) {

			updateDateState(
					form,
					workBreaktime,
					workActualtime,
					workOvertime);

		} else {

			insertDateState(
					userId,
					form,
					workBreaktime,
					workActualtime,
					workOvertime);
		}

		/*
		 * 日次更新後に月次再集計
		 */
		updateMonthState(userId, form.getTargetDate());
	}

	private Integer calculateActualTime(
			String startTime,
			String endTime,
			String breakTime) {

		Integer startMinutes = convertTimeToMinutes(startTime);

		Integer endMinutes = convertTimeToMinutes(endTime);

		Integer breakMinutes = convertTimeToMinutes(breakTime);

		if (startMinutes == null || endMinutes == null) {
			return 0;
		}

		/*
		 * 日をまたいだ勤務に対応
		 * 例：22:00 ～ 02:00
		 */
		if (endMinutes < startMinutes) {
			endMinutes += 24 * 60;
		}

		int actualMinutes = endMinutes
				- startMinutes
				- breakMinutes;

		return Math.max(actualMinutes, 0);
	}

	private Integer calculateOvertime(
			Integer actualMinutes) {

		final int standardWorkMinutes = 8 * 60;

		if (actualMinutes == null) {
			return 0;
		}

		return Math.max(
				actualMinutes - standardWorkMinutes,
				0);
	}

	private String formatMinutesForInput(Integer minutes) {

		if (minutes == null) {
			return "";
		}

		int hour = minutes / 60;
		int minute = minutes % 60;

		return String.format(
				"%02d:%02d",
				hour,
				minute);
	}

	private void updateDateState(
			AS005Dto form,
			Integer workBreaktime,
			Integer workActualtime,
			Integer workOvertime) {

		Date workStarttime = convertTime(form.getWorkStarttime());
		Date workEndtime = convertTime(form.getWorkEndtime());

		dateStateMapper.updateAttendance(
				form.getDailyId(),
				workStarttime,
				workEndtime,
				workBreaktime,
				workActualtime,
				workOvertime,
				form.getReason());
	}

	private void insertDateState(
			String userId,
			AS005Dto form,
			Integer workBreaktime,
			Integer workActualtime,
			Integer workOvertime) {

		LocalDate targetDate = form.getTargetDate();

		Long monthlyId = monthStateMapper.selectMonthlyId(
				userId,
				targetDate.getYear(),
				targetDate.getMonthValue());

		if (monthlyId == null) {

			throw new IllegalStateException(
					"対象月の月次データが存在しません。");
		}

		Date workStarttime = convertTime(form.getWorkStarttime());
		Date workEndtime = convertTime(form.getWorkEndtime());

		dateStateMapper.insertAttendance(
				monthlyId,
				targetDate,
				workStarttime,
				workEndtime,
				workBreaktime,
				workActualtime,
				workOvertime,
				form.getReason());
	}

	private void updateMonthState(String userId, LocalDate targetDate) {

		Long monthlyId = monthStateMapper.selectMonthlyId(
				userId,
				targetDate.getYear(),
				targetDate.getMonthValue());

		if (monthlyId == null) {
			throw new IllegalStateException(
					"対象月の月次データが存在しません。");
		}

		monthStateMapper.recalculateMonthState(monthlyId);
	}

	private Date convertTime(String time) {

		if (time == null || time.isBlank()) {
			return null;
		}

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			return formatter.parse(time);

		} catch (ParseException e) {
			throw new IllegalArgumentException("時刻の形式が正しくありません。", e);
		}
	}

	private Integer convertTimeToMinutes(String time) {

		if (time == null || time.isBlank()) {
			return 0;
		}

		String[] parts = time.split(":");

		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]);

		return hour * 60 + minute;
	}
}