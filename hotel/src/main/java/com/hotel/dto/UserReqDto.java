package com.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserReqDto {
	@NotBlank(message = "please enter your name")
	private String name;
	@Email
	@NotBlank(message = "please enter your email")
	private String email;
	@Pattern(
		    regexp = "^[a-zA-Z0-9]{6,}$",
		    message = "Password must be at least 6 characters long and contain only letters and digits"
		)
	private String password;
	@NotBlank(message = "please enter your phone number")
	private String phone;
	@NotBlank(message = "gender cannot be blank")
	private String gender;
	@NotBlank(message = "please enter your address")
	private String address;
	@NotBlank(message = "please enter your IDcard number")
	private String Idcard;

}