package com.example.jwt.security;

import com.example.jwt.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationManager implements AuthenticationManager {

	@Autowired
	private MyUserService myUserService;

	@Bean
	public PasswordEncoder passwordEncoder () {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	public Authentication authenticate (Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = myUserService.loadUserByUsername(authentication.getName());

		if(passwordEncoder().matches(
			authentication.getCredentials().toString(),
			userDetails.getPassword()
		)) {
			return new UsernamePasswordAuthenticationToken(
			 userDetails.getUsername(),
			 userDetails.getPassword(),
			 userDetails.getAuthorities());
		}

		throw new BadCredentialsException("Wrong Password");
	}
}
