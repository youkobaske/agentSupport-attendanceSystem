package com.example.agentSupport_attendanceSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.agentSupport_attendanceSystem.mapper")
public class AgentSupportAttendanceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentSupportAttendanceSystemApplication.class, args);
	}

}
