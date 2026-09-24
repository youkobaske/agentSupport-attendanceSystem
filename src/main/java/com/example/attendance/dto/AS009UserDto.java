package com.example.attendance.dto;

import java.math.BigDecimal;

public class AS009UserDto {

	private String userId;
	private String userName;

	// 分単位
	private Integer actualMinutes;
	private Integer overtimeMinutes;

	private BigDecimal paidVacationDays;

	private Integer missingClockDays;

	// 01:休暇申請 02:勤怠締申請
	private String applyType;

	// 01:承認待 02:承認済 04:差戻し
	private String applyState;

	private Long applyId;

	public String getActualTimeText() {
		return formatMinutes(actualMinutes);
	}

	public String getOvertimeText() {
		return formatMinutes(overtimeMinutes);
	}

	public String getApplyStateText() {

		if (applyType == null || applyState == null) {
			return "";
		}

		String typeText = switch (applyType) {
		case "01" -> "休暇申請";
		case "02" -> "勤怠締申請";
		default -> "";
		};

		String stateText = switch (applyState) {
		case "01" -> "承認待";
		case "02" -> "承認済";
		case "04" -> "差戻し";
		default -> "";
		};

		return typeText + stateText;
	}

	private String formatMinutes(Integer minutes) {

		if (minutes == null) {
			return "0:00";
		}

		return String.format(
				"%d:%02d",
				minutes / 60,
				minutes % 60);
	}

	// getter / setter

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getActualMinutes() {
		return actualMinutes;
	}

	public void setActualMinutes(Integer actualMinutes) {
		this.actualMinutes = actualMinutes;
	}

	public Integer getOvertimeMinutes() {
		return overtimeMinutes;
	}

	public void setOvertimeMinutes(Integer overtimeMinutes) {
		this.overtimeMinutes = overtimeMinutes;
	}

	public BigDecimal getPaidVacationDays() {
		return paidVacationDays;
	}

	public void setPaidVacationDays(BigDecimal paidVacationDays) {
		this.paidVacationDays = paidVacationDays;
	}

	public Integer getMissingClockDays() {
		return missingClockDays;
	}

	public void setMissingClockDays(Integer missingClockDays) {
		this.missingClockDays = missingClockDays;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
}