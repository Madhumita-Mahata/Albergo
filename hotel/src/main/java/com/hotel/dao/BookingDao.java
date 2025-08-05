package com.hotel.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Booking;
import com.hotel.entities.BookingStatus;

public interface BookingDao extends JpaRepository<Booking, Long> {

//	@Query("SELECT b FROM Booking b WHERE b.checkOutDate < :today AND b.status = 'BOOKED'")
//	List<Booking> findBookingsToMarkCompleted(@Param("today") LocalDate today);
	
	List<Booking> findByUserUserId(Long userId);
	
	//List<Booking> findByStatusNotAndCheckOutDateBefore(BookingStatus status, LocalDate date);
	
	List<Booking> findByBookingStatusNotAndCheckOutDateBefore(BookingStatus status, LocalDate date);
}
