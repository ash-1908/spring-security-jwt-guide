package com.example.jwt.service;

import com.example.jwt.model.MyUserModel;
import com.example.jwt.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

	@Autowired
	MyUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		User user = userRepository.getUser(username);

		if(user == null) throw new UsernameNotFoundException("User does not exist");

		return new MyUserModel(user);
	}
}
