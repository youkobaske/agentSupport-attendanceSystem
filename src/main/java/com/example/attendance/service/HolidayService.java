package com.example.attendance.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.example.attendance.mapper.HolidayMapper;

@Service
public class HolidayService {

	private final HolidayMapper holidayMapper;
	private final RestClient restClient;

	@Value("${holiday.api.url}")
	private String holidayApiUrl;

	public HolidayService(
			HolidayMapper holidayMapper,
			RestClient.Builder restClientBuilder) {

		this.holidayMapper = holidayMapper;
		this.restClient = restClientBuilder.build();
	}

	@Transactional
	public int syncCurrentAndNextYear() {

		int currentYear = LocalDate.now().getYear();

		int currentCount = importHolidays(currentYear);

		int nextCount = importHolidays(currentYear + 1);

		return currentCount + nextCount;
	}

	@Transactional
	public int importHolidays(int year) {

		Map<String, String> holidays = restClient
				.get()
				.uri(holidayApiUrl, year)
				.retrieve()
				.body(
						new ParameterizedTypeReference<Map<String, String>>() {
						});

		if (holidays == null || holidays.isEmpty()) {
			return 0;
		}

		for (Map.Entry<String, String> entry : holidays.entrySet()) {

			holidayMapper.upsertHoliday(
					LocalDate.parse(entry.getKey()),
					entry.getValue(),
					"API");
		}

		return holidays.size();
	}
}