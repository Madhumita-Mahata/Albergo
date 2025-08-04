package com.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.UserReqDto;
import com.hotel.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated

public class UserController {
	
	private final UserService userService;
	
	//USER REGISTERATION
	@PostMapping("/register")
	public ResponseEntity<?> userRregisteration(@RequestBody @Valid UserReqDto dto){
		
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(dto));
		
	}
	
	//USER LOGIN
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginReqDTO dto) {
		return ResponseEntity.ok(userService.loginUser(dto));
	}
	
	//CHANGE PASSWORD
		@PutMapping("/changePassword")
		public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto){
			return ResponseEntity.ok(userService.changePassword(dto));
		}
		
		//GET USER BY ID
		@GetMapping("/{userId}")
		public ResponseEntity<?> getUserById(@PathVariable Long userId){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.getUserById(userId));
		}
		
		//UPDATE USER
		@PutMapping("/{userId}")
		public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDTO dto){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.updateUserDetails(userId,dto));
		}
		
		
		//DELETE USER
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deleteUser(@PathVariable Long id){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.deleteUser(id));
		}
		
		//-----------REVIEWS--------
		
		//ADD REVIEW
		@PostMapping("/reviews/{userId}")
		public ResponseEntity<?> giveReview(@RequestBody @Valid ReviewReqDTO dto, 
				@PathVariable Long pgId, @PathVariable Long userId){
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(userService.giveReview(dto, userId));
			
		}
		
		//UPDATE
		@PutMapping("/reviews/{reviewId}")
		public ResponseEntity<?> updateReview(@PathVariable Long reviewId, 
				@RequestBody ReviewReqDTO dto){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.updateReview(reviewId,dto));
		}
		
		
		//GET REVIEW BY USERID
		@GetMapping("/{userId}/reviews")
		public ResponseEntity<?> getReviewByUserId(@PathVariable Long userId){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.getReviewById(userId));
		}
		
		
		//DELETE REIEW
		@DeleteMapping("/reviews/{reviewId}")
		public ResponseEntity<?> deleteReview(@PathVariable Long reviewId){
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.deleteReview(reviewId));
		}
		
		
		//---------BOOKING-----------
		
		
		//MAKE BOOKING
		@PostMapping("/bookings")
		public ResponseEntity<?> makeBooking(@Valid @RequestBody BookingReqDTO dto){
			return ResponseEntity.ok(userService.createBooking(dto));
		}
		
		//MAKE PAYMENT
		@PostMapping("/{bookingId}/payment")
	    public ResponseEntity<?> makePayment(
	            @PathVariable Long bookingId,
	            @Valid @RequestBody PaymentReqDTO paymentDTO) {
	        BookingRespDTO resp = userService.makePayment(bookingId, paymentDTO);
	        return ResponseEntity.ok(resp);
	    }
		
		//GET ALL BOOKINGS BY USERID
		@GetMapping("/bookings/users/{userId}")
	    public ResponseEntity<List<BookingRespDTO>> getBookingsByUserId(@PathVariable Long userId) {
	        List<BookingRespDTO> resp = userService.getBookingsByUserId(userId);
	        return ResponseEntity.ok(resp);
	    }
		
		//GET BOOKINGS BY BOOKINGID
			@GetMapping("/bookings/{bookingId}")
		    public ResponseEntity<?> getBookingsByBookingId(@PathVariable Long bookingId) {
		        return ResponseEntity.status(HttpStatus.OK)
		        		.body(userService.getBookingById(bookingId));
		    }

		//DELETE BY USERID
		@PutMapping("/bookings/cancel/{userId}/{bookingId}")
		public ResponseEntity<?> cancelBookingsByUserId(@PathVariable Long userId, @PathVariable Long bookingId) {
		    return ResponseEntity.status(HttpStatus.OK)
		    		.body(userService.cancelBookingsByUserId(userId,  bookingId));
		}
		
		@GetMapping("/bookings/update-status")
		public ResponseEntity<String> manuallyUpdateBookings() {
		    userService.updateCompletedBookings();
		    return ResponseEntity.ok("Bookings updated");
		}

		
		//--------SERVICES---------
		
		//REQUEST SERVICE
		@PostMapping("/services/request-service")
		public ResponseEntity<?> requestService(@RequestBody @Valid RequestServiceDTO dto){
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(userService.requestService(dto));
			
		}
		
	}

