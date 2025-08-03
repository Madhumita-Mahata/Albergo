package com.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto){
		return ResponseEntity.ok(userService.changePassword(dto));
	}
}
