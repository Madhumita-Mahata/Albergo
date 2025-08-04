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
      
       //CHECK PRICE
       //USER WILL PAY FOR STAY
       Room room = booking.getRoom();
       double expectedAmount = room.getPricePerDay();
       
       if(paymentDTO.getAmount()!=expectedAmount) {
       	throw new ApiException("Invalid payment amount. Expected: " + expectedAmount);
       }
       
       Payment payment = new Payment();
       payment.setAmount(paymentDTO.getAmount());
       payment.setPaymentDate(LocalDateTime.now());
       payment.setPaymentStatus(paymentDTO.getPaymentStatus() != null ? paymentDTO.getPaymentStatus() : PaymentStatus.SUCCESS);
       payment.setBooking(booking);

       Payment savedPayment = paymentDao.save(payment);

       booking.setPayment(savedPayment);
       bookingDao.save(booking);

      
   //MAP to BookingRespDTO including payment info
       //Map to BookingRespDTO including payment info
       BookingRespDTO respDto = modelMapper.map(booking, BookingRespDTO.class);
       respDto.setRoomId(booking.getRoom().getRoomId());
       respDto.setUserId(booking.getUser().getUserId());
       respDto.setUserName(booking.getUser().getName());
       
     //Payment info
       respDto.setPaymentId(savedPayment.getPaymentId());
       respDto.setAmount(savedPayment.getAmount());
       respDto.setPaymentStatus(savedPayment.getPaymentStatus());
       respDto.setPaymentDate(savedPayment.getPaymentDate());

       return respDto;
	}
   
   //UPDATE COMPLETE BOOKING
   
   @Scheduled(cron = "0 0 1 * * ?") // Runs daily at 1 AM
   public void updateCompletedBookings() {
       LocalDate today = LocalDate.now();

       // Step 1: Mark expired bookings as COMPLETED
       List<Booking> bookingsToComplete = bookingDao.findBookingsToMarkCompleted(today);
       for (Booking booking : bookingsToComplete) {
           booking.setStatus(BookingStatus.COMPLETED);
           bookingDao.save(booking);
       }

       // Step 2: For all affected rooms, update their currentOccupancy based on today
       Set<Room> affectedRooms = bookingsToComplete.stream()
           .map(booking -> booking.getRoom())
           .collect(Collectors.toSet());

       for (Room room : affectedRooms) {
           int currentOccupants = bookingDao.countCurrentOccupants(room, today, BookingStatus.BOOKED);
           room.setCurrentOccupancy(currentOccupants);
           if (currentOccupants == 0) {
               room.setStatus(RoomStatus.AVAILABLE);
           } else {
               room.setStatus(RoomStatus.OCCUPIED);
           }
           roomDao.save(room);
       }

       //GET ALL BOOKINGS BY USERID
	@Override
	public List<BookingRespDto> getBookingsByUserId(Long userId) {
		User user = userDao.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		List<Booking> bookings = bookingDao.findByUserUserId(userId);
		
		if(bookings.isEmpty()) {
			throw new ResourceNotFoundException("No bookings found for this user");
		}
		
		return bookings.stream().map(booking -> {
			BookingRespDTO dto = modelMapper.map(booking, BookingRespDTO.class);
			
		//SET ROOM AND PGPROPERTY
	        if (booking.getRoom() != null) {
	            dto.setRoomId(booking.getRoom().getRoomId());
	     }
		
	      //SET USER
	        if (booking.getUser() != null) {
	            dto.setUserId(booking.getUser().getUserId());
	            dto.setUserName(booking.getUser().getName());
	        }
		}
	        //SET PAYMENT
	        if (booking.getPayment() != null) {
	            dto.setPaymentId(booking.getPayment().getPaymentId());
	            dto.setAmount(booking.getPayment().getAmount());
	            dto.setPaymentStatus(booking.getPayment().getPaymentStatus());
	            dto.setPaymentDate(booking.getPayment().getPaymentDate());
	        }
	        return dto;
	    }).collect(Collectors.toList());
	}

	@Override
	public BookingRespDto cancelBookingsByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//CANCEL BOOKING USING USERID AND BOOKINGID
	@Override
	public BookingRespDto getBookingById(Long bookingId) {
		User user = userDao.findByUserId(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	 
	 Booking booking = bookingDao.findById(bookingId)
	            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

	    
	  if(!booking.getUser().getUserId().equals(userId)) {
		  throw new ResourceNotFoundException("Booking does not belong to this user");
	  }
	  
	  if (booking.getStatus() == BookingStatus.CANCELLED) {
	        throw new ApiException("Booking is already cancelled");
	   }
	  
	  booking.setStatus(BookingStatus.CANCELLED);
	  
	  Room room = booking.getRoom();
	  if(room !=null) {
		  room.setStatus(RoomStatus.AVAILABLE);	
		  if(room.getCurrentOccupancy()>0) {
			  room.setCurrentOccupancy(room.getCurrentOccupancy()-1);
		  }
		  roomDao.save(room);
	 }
	    
	  bookingDao.save(booking);
	  
	  
	  BookingRespDTO dto = modelMapper.map(booking, BookingRespDTO.class);
	    dto.setUserId(user.getUserId());
	    dto.setUserName(user.getName());
	    if (booking.getRoom() != null) {
	        dto.setRoomId(booking.getRoom().getRoomId());
	    }
	    if (booking.getPayment() != null) {
	        dto.setPaymentId(booking.getPayment().getPaymentId());
	        dto.setAmount(booking.getPayment().getAmount());
	        dto.setPaymentStatus(booking.getPayment().getPaymentStatus());
	        dto.setPaymentDate(booking.getPayment().getPaymentDate());
	    }

	    return dto;
	    
	}

	

	@Override
	public RequestedServiceResponseDto requestService(@Valid RequestServiceDTO dto) {
		User user = userDao.findById(dto.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    HotelService service = serviceDao.findById(dto.getServiceId())
	            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
		
	    Room room = roomDao.findById(dto.getRoomId())
	            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

	 // Check if user has a valid booking for the given room
	    Optional<Booking> bookingOpt = bookingDao.findByUserUserIdAndRoomRoomIdAndStatusNot(
	        user.getUserId(), room.getRoomId(), BookingStatus.CANCELLED);

	    if (bookingOpt.isEmpty()) {
	        throw new ApiException("User does not have a valid booking for this room");
	    }
	    UserServiceRequest req = modelMapper.map(dto, UserServiceRequest.class);
		  req.setUser(user);
		  req.setService(service);
		  req.setStatus(ServiceStatus.REQUESTED);
		  req.setRequestDate(LocalDate.now());
	    
		  UserServiceRequest saved  = userServiceRequestDao.save(req);
		  
		  RequestedServiceResponseDTO respDto = modelMapper.map(saved, RequestedServiceResponseDTO.class);
		
		  respDto.setUserId(saved.getUser().getUserId());
		  respDto.setUserName(saved.getUser().getName());
		  
		  respDto.setServiceId(saved.getService().getServiceId());
		  respDto.setServiceName(saved.getService().getName());
		  respDto.setServiceDescription(saved.getService().getDescription());
		  respDto.setServicePrice(saved.getService().getPrice());
		  
		  respDto.setRoomId(room.getRoomId());
		  
		  return respDto;
		}
	    
		
	}

}
