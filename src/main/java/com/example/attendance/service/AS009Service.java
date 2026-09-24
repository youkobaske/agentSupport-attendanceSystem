package com.example.attendance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.AS009SummaryDto;
import com.example.attendance.dto.AS009UserDto;
import com.example.attendance.mapper.UserInfoMapper;

@Service
public class AS009Service {

	private final UserInfoMapper userInfoMapper;

	public AS009Service(UserInfoMapper userInfoMapper) {
		this.userInfoMapper = userInfoMapper;
	}

	public List<AS009UserDto> getUsers(
			String approverId,
			int year,
			int month) {

		YearMonth ym = YearMonth.of(year, month);

		LocalDate startDate = ym.atDay(1);
		LocalDate endDate = ym.atEndOfMonth();

		return userInfoMapper.selectAdminUsers(
				approverId,
				year,
				month,
				startDate,
				endDate);
	}

	public AS009SummaryDto createSummary(
			List<AS009UserDto> users) {

		AS009SummaryDto summary = new AS009SummaryDto();

		summary.setOvertime45Count(
				(int) users.stream()
						.filter(u -> nvl(u.getOvertimeMinutes()) >= 45 * 60)
						.count());

		summary.setOvertime75Count(
				(int) users.stream()
						.filter(u -> nvl(u.getOvertimeMinutes()) >= 75 * 60)
						.count());

		summary.setPendingCount(
				(int) users.stream()
						.filter(u -> "01".equals(u.getApplyState()))
						.count());

		summary.setMissingClockCount(
				(int) users.stream()
						.filter(u -> nvl(u.getMissingClockDays()) >= 5)
						.count());

		summary.setLowPaidVacationCount(
				(int) users.stream()
						.filter(u -> u.getPaidVacationDays() != null
								&& u.getPaidVacationDays()
										.compareTo(new BigDecimal("5")) < 0)
						.count());

		return summary;
	}

	private int nvl(Integer value) {
		return value == null ? 0 : value;
	}
}