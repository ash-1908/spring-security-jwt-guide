package com.example.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

	@GetMapping("/admin")
	public ResponseEntity<?> helloAdmin() {
		return ResponseEntity.ok("Hello Admin!");
	}

	@GetMapping("/user")
	public ResponseEntity<?> helloUser() {
		return ResponseEntity.ok("Hello User!");
	}

	@GetMapping("/manager")
	public ResponseEntity<?> helloManager() {
		return ResponseEntity.ok("Hello Manager!");
	}
}
