package com.hotel.dto;

import java.time.LocalDateTime;

public class ApiResponse {

	private LocalDateTime timestamp;
	private String message;
	public ApiResponse() {
		this.timestamp = LocalDateTime.now();
		this.message = message;
	}
	
}
