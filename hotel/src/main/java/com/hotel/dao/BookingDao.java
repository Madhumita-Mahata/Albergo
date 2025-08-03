package com.hotel.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Booking;
import com.hotel.entities.User;

public interface BookingDao extends JpaRepository<Booking, Long> {


}
