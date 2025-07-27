package com.hotel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Booking;

public interface BookingDao extends JpaRepository<Booking, Long> {

}
