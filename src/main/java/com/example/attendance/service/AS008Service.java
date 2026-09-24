package com.example.attendance.service;

import java.time.YearMonth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AS008Dto;
import com.example.attendance.dto.MonthApplyInsertDto;
import com.example.attendance.mapper.ApplyMapper;
import com.example.attendance.mapper.MonthApplyDetailMapper;
import com.example.attendance.mapper.MonthStateMapper;

@Service
public class AS008Service {

	private final ApplyMapper applyMapper;

	private final MonthStateMapper monthStateMapper;

	private final MonthApplyDetailMapper monthApplyDetailMapper;

	public AS008Service(ApplyMapper applyMapper, MonthStateMapper monthStateMapper,
			MonthApplyDetailMapper monthApplyDetailMapper) {
		this.applyMapper = applyMapper;
		this.monthStateMapper = monthStateMapper;
		this.monthApplyDetailMapper = monthApplyDetailMapper;
	}

	public AS008Dto getPreviousMonthSummary(String userId) {

		YearMonth previousMonth = YearMonth.now().minusMonths(1);

		return monthStateMapper.selectMonthlySummary(
				userId,
				previousMonth.getYear(),
				previousMonth.getMonthValue());
	}

	@Transactional
	public void applyPreviousMonth(String userId) {

		// 必ずサーバー側で前月を決定
		YearMonth previousMonth = YearMonth.now().minusMonths(1);

		Long monthlyId = monthStateMapper.selectMonthlyId(
				userId,
				previousMonth.getYear(),
				previousMonth.getMonthValue());

		if (monthlyId == null) {
			throw new IllegalStateException(
					"申請対象月の勤怠情報が存在しません。");
		}

		// 二重申請防止
		int exists = applyMapper.existsPendingMonthApplication(
				userId,
				monthlyId);

		if (exists > 0) {
			throw new IllegalStateException(
					"対象月はすでに勤怠締め申請済みです。");
		}

		// tbl_apply
		MonthApplyInsertDto apply = new MonthApplyInsertDto();

		apply.setUserId(userId);
		apply.setApplyType("02");

		applyMapper.insertApply(apply);

		// tbl_month_apply_detail
		monthApplyDetailMapper.insertMonthApplyDetail(
				apply.getApplyId(),
				monthlyId);

		// tbl_month_state
		monthStateMapper.updateMonthApplyState(
				monthlyId);
	}
}