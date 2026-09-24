package com.example.attendance.dto;

public class AS009SummaryDto {

	private int overtime45Count;
	private int overtime75Count;
	private int pendingCount;
	private int missingClockCount;
	private int lowPaidVacationCount;

	public int getOvertime45Count() {
		return overtime45Count;
	}

	public void setOvertime45Count(int overtime45Count) {
		this.overtime45Count = overtime45Count;
	}

	public int getOvertime75Count() {
		return overtime75Count;
	}

	public void setOvertime75Count(int overtime75Count) {
		this.overtime75Count = overtime75Count;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getMissingClockCount() {
		return missingClockCount;
	}

	public void setMissingClockCount(int missingClockCount) {
		this.missingClockCount = missingClockCount;
	}

	public int getLowPaidVacationCount() {
		return lowPaidVacationCount;
	}

	public void setLowPaidVacationCount(int lowPaidVacationCount) {
		this.lowPaidVacationCount = lowPaidVacationCount;
	}
}