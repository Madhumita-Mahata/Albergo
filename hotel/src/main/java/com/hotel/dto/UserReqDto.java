package com.hotel.dto;

import com.hotel.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class UserReqDto {
	@NotBlank(message = "please enter first name")
	private String firstName;
	
	@NotBlank(message = "please enter last name")
	private String lastName;
	
	@Email
	@NotBlank(message = "please enter email")
	private String email;
	
	@Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,8})",
			message = "Invalid password format !!!")
	private String password;
	
	@NotBlank(message = "please enter your phone number")
	@Max(value = 10)
	private String phone;
	
	@NotBlank(message = "gender cannot be blank")
	private String gender;
	
	@NotBlank(message = "please enter your IDcard number")
	private String IdcardNumber;

	private Role role = Role.CUSTOMER;
}