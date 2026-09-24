package com.example.attendance.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.NoticeDto;
import com.example.attendance.entity.DateState;
import com.example.attendance.entity.MonthState;
import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.MonthStateMapper;

@Service
public class NoticeService {

	private final DateStateMapper dateStateMapper;
	private final MonthStateMapper monthStateMapper;

	public NoticeService(
			DateStateMapper dateStateMapper,
			MonthStateMapper monthStateMapper) {

		this.dateStateMapper = dateStateMapper;
		this.monthStateMapper = monthStateMapper;
	}

	/**
	 * お知らせ一覧取得
	 *
	 * @param userId ユーザID
	 * @return お知らせ一覧
	 */
	public List<NoticeDto> getNoticeList(String userId) {

		List<NoticeDto> noticeList = new ArrayList<>();

		// 未打刻通知
		noticeList.addAll(
				getUnstampedNotices(userId));

		// 日次勤怠の差戻通知
		noticeList.addAll(
				getDateStateNotices(userId));

		// 月次勤怠の承認・差戻通知
		noticeList.addAll(
				getMonthStateNotices(userId));

		// 通知日の新しい順
		noticeList.sort(
				Comparator.comparing(
						NoticeDto::getNoticeDate,
						Comparator.nullsLast(
								Comparator.reverseOrder())));

		return noticeList;
	}

	/**
	 * 未打刻通知取得
	 */
	private List<NoticeDto> getUnstampedNotices(String userId) {

		List<DateState> dateStateList = dateStateMapper.selectCurrentAndPreviousMonth(userId);

		List<NoticeDto> noticeList = new ArrayList<>();

		LocalDate today = LocalDate.now();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		for (DateState dateState : dateStateList) {

			/*
			 * 01：出勤
			 * 04：半休
			 *
			 * 上記だけ打刻確認対象
			 */
			if (!"01".equals(dateState.getWorkState())
					&& !"04".equals(dateState.getWorkState())) {

				continue;
			}

			if (dateState.getTargetDate() == null) {
				continue;
			}

			LocalDate targetDate = toLocalDate(dateState.getTargetDate());

			/*
			 * 対象日から7日以上経過したものだけ通知
			 *
			 */
			if (targetDate.isAfter(today.minusDays(7))) {
				continue;
			}

			/*
			 * 出勤・退勤両方入力済なら通知不要
			 */
			if (dateState.getWorkStarttime() != null
					&& dateState.getWorkEndtime() != null) {

				continue;
			}

			NoticeDto notice = new NoticeDto();

			notice.setDailyId(
					dateState.getDailyId());

			String date = formatter.format(
					dateState.getTargetDate());

			/*
			 * 両方未入力
			 */
			if (dateState.getWorkStarttime() == null
					|| dateState.getWorkEndtime() == null) {

				notice.setMessage(
						date
								+ "の出勤・退勤が未打刻です。");
			}

			/*
			 * 現在日を通知日として表示
			 */
			notice.setNoticeDate(today);

			noticeList.add(notice);
		}

		return noticeList;
	}

	/**
	 * 日次勤怠通知取得
	 *
	 * date_apply_state = 04 の
	 * 差戻通知を作成する
	 */
	private List<NoticeDto> getDateStateNotices(String userId) {

		List<DateState> dateStateList = dateStateMapper.selectCurrentAndPreviousMonth(userId);

		List<NoticeDto> noticeList = new ArrayList<>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		for (DateState dateState : dateStateList) {

			/*
			 * 04：差戻
			 */
			if (!"04".equals(
					dateState.getDateApplyState())) {

				continue;
			}

			if (dateState.getTargetDate() == null) {
				continue;
			}

			NoticeDto notice = new NoticeDto();

			notice.setDailyId(
					dateState.getDailyId());

			String targetDate = formatter.format(
					dateState.getTargetDate());

			notice.setMessage(
					targetDate
							+ "の勤怠が差し戻されました。");

			/*
			 * 管理者が差戻した日時を通知日にする
			 */
			if (dateState.getDateApplyDate() != null) {

				notice.setNoticeDate(
						toLocalDate(
								dateState.getDateApplyDate()));
			}

			noticeList.add(notice);
		}

		return noticeList;
	}

	/**
	 * 月次勤怠通知取得
	 *
	 * 02：承認
	 * 04：差戻
	 */
	private List<NoticeDto> getMonthStateNotices(String userId) {

		List<MonthState> monthStateList = monthStateMapper.selectCurrentAndPreviousMonth(userId);

		List<NoticeDto> noticeList = new ArrayList<>();

		LocalDate today = LocalDate.now();

		for (MonthState monthState : monthStateList) {

			String applyState = monthState.getMonthApplyState();

			/*
			 * 承認・差戻以外は通知対象外
			 */
			if (!"02".equals(applyState)
					&& !"04".equals(applyState)) {

				continue;
			}

			/*
			 * 処理日がない場合は通知できない
			 */
			if (monthState.getMonthApplyDate() == null) {
				continue;
			}

			LocalDate applyDate = toLocalDate(
					monthState.getMonthApplyDate());

			NoticeDto notice = new NoticeDto();

			String targetMonth = monthState.getTargetYear()
					+ "年"
					+ monthState.getTargetMonth()
					+ "月";

			/*
			 * 02：承認
			 *
			 * 承認日から3日前まで表示
			 */
			if ("02".equals(applyState)) {

				if (applyDate.isBefore(
						today.minusDays(3))) {

					continue;
				}

				notice.setMessage(
						targetMonth
								+ "の勤怠締申請が承認されました。");

				/*
				 * 04：差戻
				 *
				 * 再申請されて状態が01になるまで
				 * お知らせに表示し続ける
				 */
			} else {

				notice.setMessage(
						targetMonth
								+ "の勤怠締申請が差し戻されました。");
			}

			notice.setNoticeDate(applyDate);

			noticeList.add(notice);
		}

		return noticeList;
	}

	/**
	 * java.util.Date → LocalDate変換
	 */
	private LocalDate toLocalDate(Date date) {

		return date.toInstant()
				.atZone(
						ZoneId.systemDefault())
				.toLocalDate();
	}
}