package com.example.attendance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AS011Dto;
import com.example.attendance.mapper.ApplyMapper;
import com.example.attendance.mapper.DateStateMapper;
import com.example.attendance.mapper.MonthApplyDetailMapper;
import com.example.attendance.mapper.MonthStateMapper;
import com.example.attendance.mapper.VacationApplyDetailMapper;

@Service
public class AS011Service {

	private final ApplyMapper applyMapper;

	private final DateStateMapper dateStateMapper;

	private final MonthStateMapper monthStateMapper;

	private final MonthApplyDetailMapper monthApplyDetailMapper;

	private final VacationApplyDetailMapper vacationApplyDetailMapper;

	public AS011Service(ApplyMapper applyMapper, DateStateMapper dateStateMapper, MonthStateMapper monthStateMapper,
			MonthApplyDetailMapper monthApplyDetailMapper, VacationApplyDetailMapper vacationApplyDetailMapper) {
		this.applyMapper = applyMapper;
		this.dateStateMapper = dateStateMapper;
		this.monthStateMapper = monthStateMapper;
		this.monthApplyDetailMapper = monthApplyDetailMapper;
		this.vacationApplyDetailMapper = vacationApplyDetailMapper;
	}

	public AS011Dto getApplication(Long applyId) {

		AS011Dto application = applyMapper.selectApplication(applyId);

		if (application == null) {
			throw new IllegalArgumentException(
					"申請情報が存在しません。");
		}

		return application;
	}

	@Transactional
	public void approve(
			Long applyId,
			String approverId,
			String remark) {

		AS011Dto application = getApplication(applyId);

		/*
		 * 備考保存
		 */
		saveRemark(
				application,
				remark);

		/*
		 * tbl_apply
		 * 02 = 承認済
		 */
		int updated = applyMapper.approveApplication(
				applyId,
				approverId);

		if (updated == 0) {
			throw new IllegalStateException(
					"この申請はすでに処理されています。");
		}

		/*
		 * 個別状態
		 */
		if ("01".equals(application.getApplyType())) {

			// 休暇申請
			dateStateMapper.updateVacationDateState(
					applyId,
					"02");

		} else if ("02".equals(application.getApplyType())) {

			// 勤怠締め
			monthStateMapper.updateMonthState(
					applyId,
					"02");
		}
	}

	@Transactional
	public void returnApplication(
			Long applyId,
			String approverId,
			String remark) {

		/*
		 * 差戻しは備考必須
		 */
		if (remark == null
				|| remark.isBlank()) {

			throw new IllegalArgumentException(
					"差戻し理由を入力してください。");
		}

		AS011Dto application = getApplication(applyId);

		saveRemark(
				application,
				remark);

		/*
		 * tbl_apply
		 * 04 = 差戻し
		 */
		int updated = applyMapper.returnApplication(
				applyId,
				approverId);

		if (updated == 0) {
			throw new IllegalStateException(
					"この申請はすでに処理されています。");
		}

		if ("01".equals(application.getApplyType())) {

			dateStateMapper.updateVacationDateState(
					applyId,
					"04");

		} else if ("02".equals(application.getApplyType())) {

			monthStateMapper.updateMonthState(
					applyId,
					"04");
		}
	}

	private void saveRemark(
			AS011Dto application,
			String remark) {

		String approverRemark = (remark == null || remark.isBlank())
				? null
				: remark.trim();

		if ("01".equals(application.getApplyType())) {

			vacationApplyDetailMapper.updateVacationRemark(
					application.getApplyId(),
					approverRemark);

		} else if ("02".equals(application.getApplyType())) {

			monthApplyDetailMapper.updateMonthRemark(
					application.getApplyId(),
					approverRemark);
		}
	}
}