package com.hotel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.controller.ChangePasswordDTO;
import com.hotel.controller.LoginReqDTO;
import com.hotel.custom_exception.ApiException;
import com.hotel.custom_exception.ResourceNotFoundException;
import com.hotel.dao.BookingDao;
import com.hotel.dao.UserDao;
import com.hotel.dto.ApiResponse;
import com.hotel.dto.UserReqDto;
import com.hotel.dto.UserRespDto;
import com.hotel.entities.Role;
import com.hotel.entities.User;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;




@Service
@Transactional
@AllArgsConstructor


public class UserServiceImpl implements UserService {
	
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserDao userDao;
	private final ModelMapper modelMapper;
	private final ReviewDao reviewDao;
	private final BookingDao bookingDao;
	
	
	//---------USER-----------
	
//REGISTER USER
	@Override
	public UserRespDto registerUser(UserReqDto dto) {
		// check duplicate email
		if(userDao.existsByEmail(dto.getEmail())) {
			throw new ApiException("Email already exists!");	
		}
		User entity = modelMapper.map(dto, User.class);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity.setRole(Role.CUSTOMER);
		return modelMapper.map(userDao.save(entity), UserRespDto.class);
	}

	//USER SIGNIN
	@Override
	public UserRespDto loginUser(LoginReqDTO loginDto) {
		User user = userDao.findByEmail(loginDto.getEmail())
				.orElseThrow(()->new ApiException("Invalid email or password"));
		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			throw new ApiException("Invalid email or password");
		}
		return modelMapper.map(user, UserRespDto.class);
	}

	//CHANGE PASSWORD
	@Override
	public String changePassword(ChangePasswordDTO dto) {
		User user = userDao.findByEmail(dto.getEmail())
				 .orElseThrow(() -> new ApiException("User not found"));
		
		//validate old password
		   // Validate old password
	    if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
	        throw new ApiException("Old password is incorrect");
	    }

	    // Encode and update new password
	    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    userDao.save(user);

	    return "Password updated successfully";
	}
	
	//UPDATE USER DETAILS
	@Override
	public UserRespDto getUserById(Long id) {

		return userDao.findByUserId(id)
				.map(user->modelMapper.map(user, UserRespDto.class))
				.orElseThrow(()->new ApiException("User Not Found!"));
	}
 
	
	//--------REVIEW-------
	//G
	@Override
	public UserRespDto updateUserDetails(Long id, UpdateUserDTO dto) {
		User user = userDao.findByUserId(id)
				.orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
		modelMapper.map(dto, user);
		
		return modelMapper.map(user, UserRespDto.class);
	}

	@Override
	public ApiResponse deleteUser(Long id) {
		User user = userDao.findByUserId(id)
				.orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
		userDao.delete(user);
		return new ApiResponse("User Deleted Successfully!");
	}

	@Override
	public ReviewRespDTO giveReview(ReviewReqDTO dto, Long userId) {
		User user = userDao.findByUserId(userId)
				.orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
		if(reviewDao.existsByUser(user)) {
			throw new ApiException("You have already reviewed this hostel");
		}
		
		Review review = modelMapper.map(dto, Review.class);
		review.setUser(user);
		
		return modelMapper.map(reviewDao.save(review), ReviewRespDTO.class);
	}

	//UPDATE REVIEW
	@Override
	public ReviewRespDTO updateReview(Long reviewId, ReviewReqDTO dto) {
	    Review review = reviewDao.findByReviewId(reviewId)
	        .orElseThrow(() -> new ResourceNotFoundException("No Review Found!"));

	    review.setRating(dto.getRating());
	    review.setComment(dto.getComment());

	    Review updated = reviewDao.save(review);

	    ReviewRespDTO res = modelMapper.map(updated, ReviewRespDTO.class);
	    res.setUserId(updated.getUser().getUserId());
	    return res;
	}

	@Override
	public List<ReviewRespDTO> getReviewById(Long reviewId) {
		User user = userDao.findByUserId(userId)
				.orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
		List<Review> reviews = reviewDao.findByUser(user);
		return reviews.stream().map(review -> {
			ReviewRespDTO dto = modelMapper.map(review, ReviewRespDTO.class);
			dto.setUserId(review.getUser().getUserId());
			dto.setUserName(review.getUser().getName());
			
			return dto;
			
		}).collect(Collectors.toList());
	}

	@Override
	public List<ReviewRespDTO> getAllReviews() {
		 List<Review> reviews = reviewDao.findAll();

		    return reviews.stream().map(review -> {
		        ReviewRespDTO dto = modelMapper.map(review, ReviewRespDTO.class);

		        dto.setUserId(review.getUser().getUserId());
		        dto.setUserName(review.getUser().getName());
		        return dto;
		    }).collect(Collectors.toList());
	}

	//DELETE REVIEW BY REVIEWID
	@Override
	public ApiResponse deleteReview(Long reviewId) {
		Review review = reviewDao.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("Review not found"));
		reviewDao.delete(review);
		return new ApiResponse("Review deleted successfully");
	}

	//---------------BOOKING--------------
	
	//CREATE BOOKING
	@Override
	public AddBookingResDto createBooking(BookingReqDto dto) {
		User user = userDao.findByUserId(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
				.orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
		Room room = roomDao.findById(dto.getRoomId())
				.orElseThrow(() -> new ResourceNotFoundException("Room not found"));
		
		LocalDate today = LocalDate.now();
        
        if (dto.getCheckInDate().isBefore(today)) {
            throw new ApiException("Check-in date cannot be in the past");
        }

        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new ApiException("Check-out date must be after check-in date");
        }
     // Check room occupancy limit
        int activeBookings = bookingDao.countActiveBookingsForRoom(
        	    room,
        	    dto.getCheckInDate(),
        	    dto.getCheckOutDate(),
        	    BookingStatus.BOOKED // or CONFIRMED if that's your "active"
        	);
        
        if (activeBookings >= room.getCapacity()) {
            throw new ApiException("Room has reached its maximum occupancy for the selected dates");
        }
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(dto.getStatus() != null ? dto.getStatus() : BookingStatus.CONFIRMED);
        booking.setBookingDate(LocalDate.now());
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());

        Booking savedBooking = bookingDao.save(booking);

        int current = bookingDao.countCurrentOccupants(room, LocalDate.now(), BookingStatus.BOOKED);
        room.setCurrentOccupancy(current);

        //roomDao.save(room);
        
        roomDao.saveAndFlush(room);
        
// Map to response DTO
        
        AddBookingResDTO respDto = modelMapper.map(savedBooking, AddBookingResDTO.class);
        respDto.setRoomId(savedBooking.getRoom().getRoomId());
        respDto.setUserId(savedBooking.getUser().getUserId());
        respDto.setUserName(savedBooking.getUser().getName());
        
        
		return respDto;
	}


	 //MAKE PAYMENT FOR EXISTING BOOKING
   public BookingRespDto makePayment(Long bookingId, PaymentReqDto paymentDto) {
       Booking booking = bookingDao.findById(bookingId)
               .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

       if(booking.getPayment() != null) {
           throw new ApiException("Payment already exists for this booking");}
       Room room = booking.getRoom();
       LocalDate checkIn = booking.getCheckInDate();
       LocalDate checkOut = booking.getCheckOutDate();

       if (room == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
           throw new ApiException("Invalid booking details for price calculation.");
       }

       long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
       double expectedAmount = nights * room.getPricePerNight(); // assuming price is per night

       final double TOLERANCE = 0.01;

       if (Math.abs(paymentDTO.getAmount() - expectedAmount) > TOLERANCE) {
           throw new ApiException("Invalid payment amount. Expected: " + expectedAmount);
       }

       
	}

	@Override
	public List<BookingRespDto> getBookingsByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingRespDto cancelBookingsByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingRespDto getBookingById(Long bookingId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCompletedBookings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RequestedServiceResponseDto requestService(@Valid RequestServiceDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
