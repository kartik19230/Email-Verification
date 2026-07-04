package com.kk.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kk.entity.User;
import com.kk.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserService implements UserDetailsService{

	private final UserRepo repo;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
		
		return org.springframework.security.core.userdetails.
				User
				.withUsername(email)
				.password(user.getPassword())
				.build();
	}

}
