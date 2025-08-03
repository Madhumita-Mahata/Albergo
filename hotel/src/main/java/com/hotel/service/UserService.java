package com.hotel.service;
import java.util.List;

import com.hotel.controller.ChangePasswordDTO;
import com.hotel.controller.LoginReqDTO;
import com.hotel.dto.ApiResponse;
import com.hotel.dto.UserReqDto;
import com.hotel.dto.UserRespDto;

import jakarta.validation.Valid;


public interface UserService {
	
	UserRespDto registerUser(UserReqDto dto);
	
	UserRespDto loginUser(LoginReqDTO loginDto);
	
	String changePassword(ChangePasswordDTO dto);
	
	UserRespDto getUserById(Long id);
	
	UserRespDto updateUserDetails(Long id,UpdateUserDTO dto);
	
	ApiResponse deleteUser(Long id);
	
	ReviewRespDTO giveReview(ReviewReqDTO dto, Long userId);
	
	ReviewRespDTO updateReview(Long reviewId, ReviewReqDTO dto);
	
	List<ReviewRespDTO> getReviewById(Long reviewId);
	
	List<ReviewRespDTO> getAllReviews();
	
	ApiResponse deleteReview(Long reviewId);
	
	//------------BOOKING--------------
	
	AddBookingResDto createBooking(BookingReqDto dto);
	
	BookingRespDto makePayment(Long bookingId, PaymentReqDto paymentDto);
	
	List<BookingRespDto> getBookingsByUserId(Long userId);
	
	BookingRespDto cancelBookingsByUserId(Long userId);
	
	BookingRespDto getBookingById(Long bookingId);
	
	void updateCompletedBookings();
	
	//------------SERVICE-----------
	RequestedServiceResponseDto requestService(@Valid RequestServiceDTO dto);

}

