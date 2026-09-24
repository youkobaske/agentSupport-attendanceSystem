package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AS006Controller {

	@GetMapping("/AS006")
	public String showAS006() {

		return "AS006";
	}
}