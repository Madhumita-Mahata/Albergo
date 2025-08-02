package com.hotel.dto;

import com.hotel.entities.Status;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomReqDto {

	@NotBlank(message = "Please enter room number")
	private String roomNumber;
	
	@NotBlank
	private String occupacy;

	@NotBlank(message = "Enter the category of the room")
	private String category;
	
	@NotBlank(message = "Enter the price of the room")
	private double price;


}
