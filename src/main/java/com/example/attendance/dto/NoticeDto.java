package com.example.attendance.dto;

import java.time.LocalDate;

public class NoticeDto {

	private Long dailyId;

	private String message;

	private LocalDate noticeDate;

	public Long getDailyId() {
		return dailyId;
	}

	public void setDailyId(Long dailyId) {
		this.dailyId = dailyId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(LocalDate noticeDate) {
		this.noticeDate = noticeDate;
	}
}