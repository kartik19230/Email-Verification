package com.kk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kk.entity.VerificationToken;
import java.util.Optional;


public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

	Optional<VerificationToken> findByToken(String token);
}
