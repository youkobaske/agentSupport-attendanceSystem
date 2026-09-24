package com.example.attendance.dto;

public class AS008Dto {

	private int year;
	private int month;

	private String approverName;

	// 出勤日数
	private double workDays;

	// 総労働時間（分）
	private int actualMinutes;

	// 休憩時間（分）
	private int breakMinutes;

	// 残業時間（分）
	private int overtimeMinutes;

	// 有給残日数
	private double remainingPaidVacation;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public double getWorkDays() {
		return workDays;
	}

	public void setWorkDays(double workDays) {
		this.workDays = workDays;
	}

	public int getActualMinutes() {
		return actualMinutes;
	}

	public void setActualMinutes(int actualMinutes) {
		this.actualMinutes = actualMinutes;
	}

	public int getBreakMinutes() {
		return breakMinutes;
	}

	public void setBreakMinutes(int breakMinutes) {
		this.breakMinutes = breakMinutes;
	}

	public int getOvertimeMinutes() {
		return overtimeMinutes;
	}

	public void setOvertimeMinutes(int overtimeMinutes) {
		this.overtimeMinutes = overtimeMinutes;
	}

	public double getRemainingPaidVacation() {
		return remainingPaidVacation;
	}

	public void setRemainingPaidVacation(double remainingPaidVacation) {
		this.remainingPaidVacation = remainingPaidVacation;
	}

	public String getActualTimeText() {
		return formatMinutes(actualMinutes);
	}

	public String getBreakTimeText() {
		return formatMinutes(breakMinutes);
	}

	public String getOvertimeText() {
		return formatMinutes(overtimeMinutes);
	}

	private String formatMinutes(int minutes) {
		int hour = minutes / 60;
		int minute = minutes % 60;

		return String.format("%d時間%02d分", hour, minute);
	}
}