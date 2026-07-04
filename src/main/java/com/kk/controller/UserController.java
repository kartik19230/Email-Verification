package com.kk.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kk.dto.LoginDTO;
import com.kk.dto.UserDTO;
import com.kk.entity.User;
import com.kk.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
		
		System.out.println("Request Recieved");
		
		userService.register(userDTO);
		return ResponseEntity.ok("Registration Successfull. Check your mail to verify");
		
	}
	
	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam String token) {

		String result = userService.verifyToken(token);
		
		if (result.equals("Email Verified Successfully")) {
			return ResponseEntity.ok(result);
		}
		
		return ResponseEntity.badRequest().body(result);
	}
	
	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody LoginDTO dto){
		
		String message = userService.login(dto);
		
		return ResponseEntity.ok(message);
	}

}
