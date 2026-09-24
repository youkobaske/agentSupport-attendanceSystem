package com.example.attendance.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.NoticeDto;
import com.example.attendance.entity.MonthState;
import com.example.attendance.entity.UserInfo;
import com.example.attendance.service.MonthStateService;
import com.example.attendance.service.NoticeService;
import com.example.attendance.service.UserInfoService;
import com.example.attendance.utils.DayUtils;

@Controller
public class AS002Controller {

	private final MonthStateService monthStateService;

	private final UserInfoService userInfoService;

	private final NoticeService noticeService;

	AS002Controller(MonthStateService monthStateService, UserInfoService userInfoService, NoticeService noticeService) {
		this.monthStateService = monthStateService;
		this.userInfoService = userInfoService;
		this.noticeService = noticeService;
	}

	@GetMapping("/AS002")
	public String home(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			Authentication authentication,
			Model model) {

		// 年月が指定されていない場合は現在年月
		YearMonth targetMonth;
		Short sYear = 0;
		Short sMonth = 0;

		if (year != null && month != null) {
			targetMonth = YearMonth.of(year, month);
			sYear = year.shortValue();
			sMonth = month.shortValue();
		} else {
			targetMonth = YearMonth.now();
			Integer iYear = targetMonth.getYear();
			sYear = iYear.shortValue();
			Integer iMonth = targetMonth.getMonthValue();
			sMonth = iMonth.shortValue();
		}

		// 年月
		model.addAttribute("year", targetMonth.getYear());
		model.addAttribute("month", targetMonth.getMonthValue());

		// 表示用年月
		model.addAttribute(
				"monthDisplay",
				targetMonth.format(DateTimeFormatter.ofPattern("yyyy年M月")));

		// 前月
		YearMonth previousMonth = targetMonth.minusMonths(1);

		model.addAttribute("previousYear", previousMonth.getYear());
		model.addAttribute("previousMonth", previousMonth.getMonthValue());

		// 翌月
		YearMonth nextMonth = targetMonth.plusMonths(1);

		model.addAttribute("nextYear", nextMonth.getYear());
		model.addAttribute("nextMonth", nextMonth.getMonthValue());

		// 対象月の日付一覧を作成
		List<DayUtils> attendanceDays = new ArrayList<>();

		for (int day = 1; day <= targetMonth.lengthOfMonth(); day++) {

			LocalDate date = targetMonth.atDay(day);

			DayUtils attendanceDay = new DayUtils();

			attendanceDay.setDate(date);
			attendanceDay.setDay(day);
			attendanceDay.setWeekDay(getJapaneseWeekDay(date));

			attendanceDays.add(attendanceDay);
		}

		model.addAttribute("attendanceDays", attendanceDays);

		String userId = authentication.getName();

		// ユーザ情報テーブル
		UserInfo userInfo = userInfoService.getUserState(userId);
		// 取得有給休暇
		if (userInfo.getPaidVacation() != null) {
			model.addAttribute("paidVacation", userInfo.getPaidVacation());
		}
		// 残り有給休暇
		if (userInfo.getRestPaidVacation() != null) {
			model.addAttribute("restPaidVacation", userInfo.getRestPaidVacation());
		}
		// 月次状況テーブル
		MonthState monthState = monthStateService.getMonthState(userId, sYear, sMonth);
		model.addAttribute("monthState", monthState);
		// 実働時間
		String formatActualTime = "";
		if (monthState.getMonthActualtime() != null) {
			formatActualTime = monthStateService.formatMinutes(monthState.getMonthActualtime());
			model.addAttribute("actualTime", formatActualTime);
		}
		// 残業時間
		String formatOverTime = "";
		if (monthState.getMonthOvertime() != null) {
			formatOverTime = monthStateService.formatMinutes(monthState.getMonthOvertime());
			model.addAttribute("overtime", formatOverTime);
		}

		// お知らせ通知
		List<NoticeDto> noticeList = noticeService.getNoticeList(userId);

		model.addAttribute("noticeList", noticeList);

		return "/AS002";
	}

	/**
	 * 曜日を日本語に変換
	 */
	private String getJapaneseWeekDay(LocalDate date) {

		switch (date.getDayOfWeek()) {

		case MONDAY:
			return "月";

		case TUESDAY:
			return "火";

		case WEDNESDAY:
			return "水";

		case THURSDAY:
			return "木";

		case FRIDAY:
			return "金";

		case SATURDAY:
			return "土";

		case SUNDAY:
			return "日";

		default:
			return "";
		}
	}
}