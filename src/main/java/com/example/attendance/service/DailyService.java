package com.example.attendance.service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.DailyDto;
import com.example.attendance.entity.Apply;
import com.example.attendance.entity.DateState;
import com.example.attendance.entity.UserInfo;
import com.example.attendance.mapper.ApplyMapper;
import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.UserInfoMapper;

@Service
public class DailyService {

	private final DateStateMapper dateStateMapper;

	private final UserInfoMapper userInfoMapper;

	private final ApplyMapper applyMapper;

	public DailyService(DateStateMapper dateStateMapper, UserInfoMapper userInfoMapper, ApplyMapper applyMapper) {
		this.dateStateMapper = dateStateMapper;
		this.userInfoMapper = userInfoMapper;
		this.applyMapper = applyMapper;
	}

	public UserInfo getUser(String userId) {
		return userInfoMapper.findByUserId(userId);
	}

	public List<DailyDto> getDailyList(String userId, Integer year, Integer month) {

		/*
		 * DBに存在する当月データを取得
		 */
		List<DateState> dateStateList = dateStateMapper.selectByUserIdAndYearMonth(userId, year, month);

		List<DailyDto> result = new ArrayList<>();

		LocalDate today = LocalDate.now();

		/*
		 * 対象月の1日
		 */
		LocalDate firstDay = LocalDate.of(year, month, 1);

		/*
		 * 対象月の末日
		 */
		LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

		/*
		 * 1日～月末までループ
		 */
		LocalDate currentDate = firstDay;

		while (!currentDate.isAfter(lastDay)) {

			/*
			 * その日のDBデータを探す
			 */
			DateState targetState = null;

			for (DateState state : dateStateList) {

				if (state.getTargetDate() == null) {
					continue;
				}

				LocalDate dbDate = toLocalDate(state.getTargetDate());

				if (dbDate.equals(currentDate)) {
					targetState = state;
					break;
				}
			}

			/*
			 * DTO作成
			 */
			DailyDto dto = new DailyDto();

			dto.setDate(String.format("%02d", currentDate.getDayOfMonth()));

			dto.setDayOfWeek(getJapaneseDayOfWeek(currentDate.getDayOfWeek()));

			dto.setFutureDate(currentDate.isAfter(today));

			dto.setTargetDate(currentDate);

			/*
			 * DBデータがある場合
			 */
			if (targetState != null) {
				setDateStateData(targetState, dto);

				/*
				 * DBデータがない場合
				 */
			} else {
				setEmptyData(dto);
			}

			result.add(dto);

			currentDate = currentDate.plusDays(1);
		}

		return result;
	}

	public List<Apply> getPendingApplications(String userId, int year, int month) {

		return applyMapper.selectPendingApplications(userId, year, month);
	}

	private void setDateStateData(
			DateState state,
			DailyDto dto) {

		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

		dto.setDailyId(state.getDailyId());

		dto.setWorkState(state.getWorkState());

		dto.setWorkStateName(getWorkStateName(state.getWorkState()));

		/*
		 * 出勤時間
		 */
		if (state.getWorkStarttime() != null) {
			dto.setStartTime(timeFormatter.format(state.getWorkStarttime()));
		} else {
			dto.setStartTime("");
		}

		/*
		 * 退勤時間
		 */
		if (state.getWorkEndtime() != null) {
			dto.setEndTime(timeFormatter.format(state.getWorkEndtime()));
		} else {
			dto.setEndTime("");
		}

		/*
		 * 実働
		 */
		dto.setActualTime(formatMinutes(state.getWorkActualtime()));

		/*
		 * 休憩
		 */
		dto.setBreakTime(formatMinutes(state.getWorkBreaktime()));

		/*
		 * 残業
		 */
		dto.setOvertime(formatMinutes(state.getWorkOvertime()));

		/*
		 * グラフ
		 */
		calculateGraph(state, dto);
	}

	private void setEmptyData(DailyDto dto) {

		dto.setDailyId(null);

		dto.setWorkState("");

		dto.setWorkStateName("");

		dto.setStartTime("");

		dto.setEndTime("");

		dto.setActualTime("");

		dto.setBreakTime("");

		dto.setOvertime("");

		dto.setGraphLeft(0);

		dto.setGraphWidth(0);

		dto.setBreakLeft(0);

		dto.setBreakWidth(0);

		dto.setOvertimeLeft(0);

		dto.setOvertimeWidth(0);
	}

	private void calculateGraph(
			DateState state,
			DailyDto dto) {

		if (state.getWorkStarttime() == null
				|| state.getWorkEndtime() == null) {

			return;
		}

		int startMinutes = getMinutes(state.getWorkStarttime());

		int endMinutes = getMinutes(state.getWorkEndtime());

		/*
		 * グラフ範囲
		 * 5:00 ～ 翌4:00
		 */
		final int graphStart = 5 * 60;
		final int graphMinutes = 23 * 60;

		/*
		 * 0:00～4:00は翌日として扱う
		 */
		if (endMinutes < graphStart) {
			endMinutes += 24 * 60;
		}

		/*
		 * 通常勤務
		 */
		double left = (startMinutes - graphStart)
				* 96.0
				/ graphMinutes;

		double width = (endMinutes - startMinutes)
				* 96.0
				/ graphMinutes;

		dto.setGraphLeft(left);
		dto.setGraphWidth(width);

		/*
		 * 休憩は12:00固定
		 */
		Integer breakMinutes = state.getWorkBreaktime();

		if (breakMinutes != null
				&& breakMinutes > 0) {

			final int breakStartMinutes = 12 * 60;

			double breakLeft = (breakStartMinutes - graphStart)
					* 96.0
					/ graphMinutes;

			double breakWidth = breakMinutes
					* 96.0
					/ graphMinutes;

			dto.setBreakLeft(breakLeft);
			dto.setBreakWidth(breakWidth);
		}

		/*
		 * 残業
		 */
		Integer overtime = state.getWorkOvertime();

		if (overtime != null
				&& overtime > 0) {

			double overtimeWidth = overtime
					* 96.0
					/ graphMinutes;

			double overtimeLeft = left
					+ width
					- overtimeWidth;

			dto.setOvertimeLeft(overtimeLeft);
			dto.setOvertimeWidth(overtimeWidth);
		}
	}

	private int getMinutes(Date time) {

		@SuppressWarnings("deprecation")
		int hour = time.getHours();

		@SuppressWarnings("deprecation")
		int minute = time.getMinutes();

		return hour * 60 + minute;
	}

	private String formatMinutes(Integer minutes) {

		if (minutes == null) {
			return "0:00";
		}

		int hour = minutes / 60;
		int minute = minutes % 60;

		return String.format(
				"%d:%02d",
				hour,
				minute);
	}

	private String getWorkStateName(String state) {

		if (state == null) {
			return "";
		}

		return switch (state) {

		case "00" -> "欠勤";

		case "01" -> "出勤";

		case "02" -> "公休";

		case "03" -> "全休";

		case "04" -> "半休";

		case "05" -> "代休";

		default -> "";
		};
	}

	private String getJapaneseDayOfWeek(
			DayOfWeek dayOfWeek) {

		return switch (dayOfWeek) {

		case MONDAY -> "月";
		case TUESDAY -> "火";
		case WEDNESDAY -> "水";
		case THURSDAY -> "木";
		case FRIDAY -> "金";
		case SATURDAY -> "土";
		case SUNDAY -> "日";
		};
	}

	private LocalDate toLocalDate(Date date) {

		return date.toInstant()
				.atZone(
						ZoneId.systemDefault())
				.toLocalDate();
	}
}