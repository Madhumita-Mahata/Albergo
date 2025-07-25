package com.hotel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Admin;

public interface AdminDao extends JpaRepository<Admin, Long>{

}
