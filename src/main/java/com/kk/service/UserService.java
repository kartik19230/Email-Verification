package com.kk.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kk.dto.LoginDTO;
import com.kk.dto.UserDTO;
import com.kk.entity.User;
import com.kk.entity.VerificationToken;
import com.kk.repository.UserRepo;
import com.kk.repository.VerificationTokenRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepo;
	private final VerificationTokenRepo tokenRepo;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authManager;
	private final JwtService jwtService;

	@Transactional
	public void register(UserDTO userDTO) {

		User user = new User();

		user.setEmail(userDTO.getEmail());
		user.setName(userDTO.getName());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

		userRepo.save(user);

		// Generating Token
		String token = UUID.randomUUID().toString(); // static factory method for generating Universally Unique
														// Identifier
		VerificationToken verificationToken = new VerificationToken(token, user);
		tokenRepo.save(verificationToken);

		// Sending Email
		String recipentAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = "http://localhost:8080/auth/verify?token=" + token;

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipentAddress);
		email.setSubject(subject);
		email.setText("Please click your verification link " + confirmationUrl);

		mailSender.send(email);

	}

	@Transactional
	public String verifyToken(String token) {

		Optional<VerificationToken> tokenOpt = tokenRepo.findByToken(token);

		if (tokenOpt.isEmpty()) {
			return "invalid token";
		}

		VerificationToken verificationToken = tokenOpt.get();
		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return "Token Expired";
		}

		User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepo.save(user);
		tokenRepo.delete(verificationToken);

		return "Email Verified Successfully";
	}

	public String login(LoginDTO dto) {

		System.out.println(dto.getEmail());

		Optional<User> opt = userRepo.findByEmail(dto.getEmail());

		System.out.println(opt.isPresent());

		try {
			
			System.out.println("inside try block");
			if (opt.isPresent()) {

				System.out.println("JWT");
				
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getEmail(),
						dto.getPassword());
				
				System.out.println(token);

				Authentication auth = authManager.authenticate(token);
				
				System.out.println(auth.isAuthenticated());

				if (auth.isAuthenticated()) {

					String jwtToken = jwtService.generateToken(dto.getEmail());

					System.out.println(jwtToken);

					return "Successfully logged in your token : " + jwtToken;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Not");

		return "User not found";
	}
}
