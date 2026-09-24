package com.example.attendance.dto;

import java.time.LocalDate;

public class AS007Form {

	private LocalDate targetDate;

	private String vacationType;

	private String applicantRemark;

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public String getVacationType() {
		return vacationType;
	}

	public void setVacationType(String vacationType) {
		this.vacationType = vacationType;
	}

	public String getApplicantRemark() {
		return applicantRemark;
	}

	public void setApplicantRemark(String applicantRemark) {
		this.applicantRemark = applicantRemark;
	}
}