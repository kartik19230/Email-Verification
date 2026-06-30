package com.kk.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kk.entity.User;
import com.kk.entity.VerificationToken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepo userRepo;
	private final VerificationTokenRepo tokenRepo;
	private final JavaMailSender mailSender;
	
	@Transactional
	public void register(User user){
		
		userRepo.save(user);
		
		//Generating Token 
		String token = UUID.randomUUID().toString(); //static factory method for generating Universally Unique Identifier
		VerificationToken verificationToken = new VerificationToken(token,user);
		tokenRepo.save(verificationToken);
		
		//Sending Email 
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
}
