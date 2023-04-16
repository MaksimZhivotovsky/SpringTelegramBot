package com.example.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.max.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
