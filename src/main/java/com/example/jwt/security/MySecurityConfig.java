package com.example.jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

	@Bean
	public InMemoryUserDetailsManager configAuthentication () {
		// list of users
		List<UserDetails> userList= new ArrayList<>();
		// list of authorities for admin
		List<GrantedAuthority> adminAuthority = new ArrayList<>();
		// list of authorities for user
		List<GrantedAuthority> userAuthority = new ArrayList<>();
		// list of authorities for manager
		List<GrantedAuthority> managerAuthority = new ArrayList<>();

		// add authorities to admin
		adminAuthority.add(new SimpleGrantedAuthority("ADMIN"));
		adminAuthority.add(new SimpleGrantedAuthority("USER"));
		// add authorities to user
		userAuthority.add(new SimpleGrantedAuthority("USER"));
		// add authorities to manager
		managerAuthority.add(new SimpleGrantedAuthority("MANAGER"));

		// create users
		UserDetails admin = new User("ADMIN", "ADMIN", adminAuthority);

		UserDetails user = new User("ANMOL", "ANMOL", userAuthority);

		UserDetails manager = new User("MANAGER", "MANAGER", managerAuthority);

		// add users to user list
		userList.add(admin);
		userList.add(user);
		userList.add(manager);

		// adding list of users to spring
		return new InMemoryUserDetailsManager(userList);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
