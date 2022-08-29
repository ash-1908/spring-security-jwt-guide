package com.example.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

	@GetMapping("/data")
	public ResponseEntity<?> helloWorld() {
		return ResponseEntity.ok("Hello World!");
	}
}
