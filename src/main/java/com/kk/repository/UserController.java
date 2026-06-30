package com.kk.repository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kk.entity.User;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("register")
	public ResponseEntity<String> postMethodName(@RequestBody User user) {
		
		userService.register(user);
		return ResponseEntity.ok("Registration Successfull. Check your mail to verify");
		
	}
	
	@GetMapping("/verify")
	public ResponseEntity<String> getMethodName(@RequestParam("token") String token) {

		String result = userService.verifyToken(token);
		
		if (result.equals("Email Verified Successfully")) {
			return ResponseEntity.ok(result);
		}
		
		return ResponseEntity.badRequest().body(result);
	}
	
	

}
