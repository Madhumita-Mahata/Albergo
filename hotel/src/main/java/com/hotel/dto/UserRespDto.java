package com.hotel.dto;

import com.hotel.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRespDto {
	private String name;
	private String email;
	private String phone;
	private String gender;
	private String address;
	private String Idcard;
	private Role role;

}