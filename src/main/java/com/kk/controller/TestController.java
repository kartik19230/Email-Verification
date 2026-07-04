package com.kk.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/me")
	public String getMethodName() {
		return "Success";
	}
	
	@GetMapping("/second")
	public String secondTest() {
		return "Success";
	}
}
