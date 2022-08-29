package com.example.jwt.controller;

import com.example.jwt.model.MyRequest;
import com.example.jwt.model.MyResponse;
import com.example.jwt.security.MyAuthenticationManager;
import com.example.jwt.service.MyUserService;
import com.example.jwt.util.MyJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

	@Autowired
	private MyAuthenticationManager myAuthenticationManager;

	@Autowired
	private MyUserService myUserService;

	@Autowired
	private MyJwtUtil jwtUtil;

	@PostMapping("/signin")
	public  ResponseEntity<?> signIn(@RequestBody MyRequest req) {
		myAuthenticationManager.authenticate(
		 new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
		);

		UserDetails userDetails = myUserService.loadUserByUsername(req.getUsername());

		final String jwtToken = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new MyResponse(jwtToken));
	}

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
