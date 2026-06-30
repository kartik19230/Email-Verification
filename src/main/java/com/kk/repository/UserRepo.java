package com.kk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kk.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
