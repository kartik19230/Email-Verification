package com.kk.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kk.service.JwtService;
import com.kk.service.MyUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	private final MyUserService customerService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = null;
		String userName = null;

		// take request header
		String authHeader = request.getHeader("Authorization");

		// check token is present or not
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userName = jwtService.extractUsername(token);
		}

		// verify token is valid or not
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetials = customerService.loadUserByUsername(userName);

			Boolean isValidate = jwtService.validateToken(token, userDetials);//validating the token

			if (isValidate) {
				//validate username and pwd
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetials,
						null, userDetials.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);//token is authenticated and can be processed
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
