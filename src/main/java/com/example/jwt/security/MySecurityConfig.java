package com.example.jwt.security;

import com.example.jwt.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {
	//  creating our custom users using spring security authentication manager
	@Autowired
	private MyUserService myUserService;

	@Bean
	public AuthenticationManager configAuthentication (HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(myUserService).and().build();
	}

	//  creating a password encoder bean which is necessary otherwise we will get errors
	@Bean
	public PasswordEncoder passwordEncoder () {
		return NoOpPasswordEncoder.getInstance();
	}

	//  creating our custom authorizations using spring security authorization
	@Bean
	public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/manager").hasAnyRole("MANAGER", "ADMIN").antMatchers("/user").hasAnyRole("USER", "MANAGER", "ADMIN").and().formLogin();
		return http.build();
	}
}
