package com.kk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kk.filter.AppFilter;
import com.kk.service.MyUserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AppFilter appFilter;
	
	private final MyUserService myUserService;
	
	
	@Bean 
	public PasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) {
		
		System.out.println("fitler chain activited");
		
		http.csrf(c -> c.disable())
			.authorizeHttpRequests(auth ->
			         auth.requestMatchers("/auth/**")
			             .permitAll()
			             .anyRequest()
			             .authenticated())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(provider())
			.addFilterBefore(appFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider provider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(myUserService);
		provider.setPasswordEncoder(encode());
		return provider;
	}
}
