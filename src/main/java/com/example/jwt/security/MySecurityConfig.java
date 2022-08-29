package com.example.jwt.security;

import com.example.jwt.security.filter.JwtFilter;
import com.example.jwt.service.MyUserService;
import com.example.jwt.util.MyJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	//  creating our custom authorizations using spring security authorization
	@Bean
	public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests()
		 .antMatchers("/admin").hasRole("ADMIN")
		 .antMatchers("/manager").hasAnyRole("MANAGER", "ADMIN")
		 .antMatchers("/user").hasAnyRole("USER", "MANAGER", "ADMIN")
		 .and().sessionManagement()
		 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
