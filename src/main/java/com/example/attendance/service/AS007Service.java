package com.example.attendance.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AS007Form;
import com.example.attendance.entity.Apply;
import com.example.attendance.entity.DateState;
import com.example.attendance.entity.VacationApplyDetail;
import com.example.attendance.mapper.ApplyMapper;
import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.MonthStateMapper;
import com.example.attendance.mapper.VacationApplyDetailMapper;

@Service
public class AS007Service {

	private final ApplyMapper applyMapper;

	private final VacationApplyDetailMapper vacationApplyDetailMapper;

	private final DateStateMapper dateStateMapper;

	private final MonthStateMapper monthStateMapper;

	public AS007Service(
			ApplyMapper applyMapper,
			VacationApplyDetailMapper vacationApplyDetailMapper,
			DateStateMapper dateStateMapper,
			MonthStateMapper monthStateMapper) {

		this.applyMapper = applyMapper;

		this.vacationApplyDetailMapper = vacationApplyDetailMapper;

		this.dateStateMapper = dateStateMapper;

		this.monthStateMapper = monthStateMapper;
	}

	@Transactional
	public void applyVacation(String userId, AS007Form form) {

		/*
		 * 入力チェック
		 */
		validate(form);

		/*
		 * ====================================
		 * 1. tbl_apply
		 * ====================================
		 */

		Apply apply = new Apply();

		apply.setUserId(userId);

		/*
		 * INSERT
		 *
		 * apply_type  = 01
		 * apply_state = 01
		 * apply_date  = CURRENT_TIMESTAMP
		 */
		applyMapper.insertApplyForVacation(apply);

		/*
		 * INSERT後に採番されたapply_id
		 */
		Long applyId = apply.getApplyId();

		if (applyId == null) {
			throw new IllegalStateException("申請IDの取得に失敗しました。");
		}

		/*
		 * ====================================
		 * 2. tbl_vacation_apply_detail
		 * ====================================
		 */

		VacationApplyDetail detail = new VacationApplyDetail();
		Date targetDate = Date.from(form.getTargetDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

		detail.setTargetDate(targetDate);
		detail.setApplyId(applyId);
		detail.setTargetDate(targetDate);
		detail.setVacationType(form.getVacationType());
		detail.setApplicantRemark(form.getApplicantRemark());
		detail.setApproverRemark(null);

		vacationApplyDetailMapper.insertVacationApply(detail);

		/*
		 * ====================================
		 * 3. tbl_date_state
		 * ====================================
		 */

		DateState dateState = dateStateMapper
				.selectByUserIdAndTargetDate(userId, form.getTargetDate());

		/*
		 * 既存データあり
		 */
		if (dateState != null) {
			dateStateMapper.updateVacationState(dateState.getDailyId(), form.getVacationType());

			/*
			 * date_stateなし
			 */
		} else {
			insertDateState(userId, form);
		}
	}

	/**
	 * date_state新規登録
	 */
	private void insertDateState(String userId, AS007Form form) {

		LocalDate targetDate = form.getTargetDate();

		Long monthlyId = monthStateMapper.selectMonthlyId(
				userId,
				targetDate.getYear(),
				targetDate.getMonthValue());

		if (monthlyId == null) {
			throw new IllegalStateException(
					"対象月の月次勤怠データが存在しません。");
		}

		dateStateMapper.insertVacationDateState(
				monthlyId,
				targetDate,
				form.getVacationType());
	}

	/**
	 * 入力チェック
	 */
	private void validate(AS007Form form) {

		if (form.getTargetDate() == null) {
			throw new IllegalStateException(
					"申請日を入力してください。");
		}

		if (form.getVacationType() == null || form.getVacationType().isBlank()) {
			throw new IllegalStateException(
					"休暇種類を選択してください。");
		}

		if (!"03".equals(form.getVacationType())
				&& !"04".equals(form.getVacationType())
				&& !"05".equals(form.getVacationType())) {
			throw new IllegalStateException(
					"休暇種類が正しくありません。");
		}
	}
}