package com.hotel.service;

import java.util.List;

import com.hotel.dto.ApiResponse;
import com.hotel.dto.UserReqDto;
import com.hotel.dto.UserRespDto;
import com.hotel.entities.User;

public interface AdminService {

	List<UserRespDto> getAllUser();
	
	UserRespDto addUser(UserReqDto UserDto);
	
	UserRespDto getUserById(Long id);
	
	User updateDetails(Long id, User user);
	
	ApiResponse deleteUser(Long id);
	
	List<UserRespDto> getUserByRole(String role);
}
