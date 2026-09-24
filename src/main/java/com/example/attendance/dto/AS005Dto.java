package com.example.attendance.dto;

import java.time.LocalDate;

public class AS005Dto {

	private Long dailyId;

	private LocalDate targetDate;

	private boolean clockInTarget;

	private boolean clockOutTarget;

	private String workStarttime;

	private String workEndtime;

	private String reason;

	private boolean editMode;

	private String workBreaktime;

	public Long getDailyId() {
		return dailyId;
	}

	public void setDailyId(Long dailyId) {
		this.dailyId = dailyId;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public boolean isClockInTarget() {
		return clockInTarget;
	}

	public void setClockInTarget(boolean clockInTarget) {
		this.clockInTarget = clockInTarget;
	}

	public boolean isClockOutTarget() {
		return clockOutTarget;
	}

	public void setClockOutTarget(boolean clockOutTarget) {
		this.clockOutTarget = clockOutTarget;
	}

	public String getWorkStarttime() {
		return workStarttime;
	}

	public void setWorkStarttime(String workStarttime) {
		this.workStarttime = workStarttime;
	}

	public String getWorkEndtime() {
		return workEndtime;
	}

	public void setWorkEndtime(String workEndtime) {
		this.workEndtime = workEndtime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getWorkBreaktime() {
		return workBreaktime;
	}

	public void setWorkBreaktime(String workBreaktime) {
		this.workBreaktime = workBreaktime;
	}
}