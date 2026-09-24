package com.example.attendance.dto;

import java.time.LocalDate;

public class DailyDto {

	private Long dailyId;

	private String date;

	private String dayOfWeek;

	private String workState;

	private String workStateName;

	private String startTime;

	private String endTime;

	private String actualTime;

	private String breakTime;

	private String overtime;

	private double graphLeft;

	private double graphWidth;

	private double breakLeft;

	private double breakWidth;

	private double overtimeLeft;

	private double overtimeWidth;

	private boolean futureDate;

	private LocalDate targetDate;

	private String editReason;

	public boolean isEdited() {
		return editReason != null
				&& !editReason.isBlank();
	}

	public Long getDailyId() {
		return dailyId;
	}

	public void setDailyId(Long dailyId) {
		this.dailyId = dailyId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getWorkState() {
		return workState;
	}

	public void setWorkState(String workState) {
		this.workState = workState;
	}

	public String getWorkStateName() {
		return workStateName;
	}

	public void setWorkStateName(String workStateName) {
		this.workStateName = workStateName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public double getGraphLeft() {
		return graphLeft;
	}

	public void setGraphLeft(double graphLeft) {
		this.graphLeft = graphLeft;
	}

	public double getGraphWidth() {
		return graphWidth;
	}

	public void setGraphWidth(double graphWidth) {
		this.graphWidth = graphWidth;
	}

	public double getBreakLeft() {
		return breakLeft;
	}

	public void setBreakLeft(double breakLeft) {
		this.breakLeft = breakLeft;
	}

	public double getBreakWidth() {
		return breakWidth;
	}

	public void setBreakWidth(double breakWidth) {
		this.breakWidth = breakWidth;
	}

	public double getOvertimeLeft() {
		return overtimeLeft;
	}

	public void setOvertimeLeft(double overtimeLeft) {
		this.overtimeLeft = overtimeLeft;
	}

	public double getOvertimeWidth() {
		return overtimeWidth;
	}

	public void setOvertimeWidth(double overtimeWidth) {
		this.overtimeWidth = overtimeWidth;
	}

	public boolean isFutureDate() {
		return futureDate;
	}

	public void setFutureDate(boolean futureDate) {
		this.futureDate = futureDate;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public String getEditReason() {
		return editReason;
	}

	public void setEditReason(String editReason) {
		this.editReason = editReason;
	}
}