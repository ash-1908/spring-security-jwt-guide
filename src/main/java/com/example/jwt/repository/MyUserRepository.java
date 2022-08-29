package com.example.jwt.repository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class MyUserRepository {
	public User getUser(String username) {
		if(username.equals("USER"))
			return new User("USER", "USER", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		else if(username.equals("MANAGER"))
			return new User("MANAGER", "MANAGER", Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority("ROLE_USER")));
		else if(username.equals("ADMIN"))
			return new User("ADMIN", "ADMIN", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority("ROLE_USER")));

		return null;
	}





}
