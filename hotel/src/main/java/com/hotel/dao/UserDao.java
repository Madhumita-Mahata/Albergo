package com.hotel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.User;

public interface UserDao extends JpaRepository<User, Long> {

}