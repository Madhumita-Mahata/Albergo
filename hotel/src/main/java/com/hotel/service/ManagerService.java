package com.hotel.service;

import java.util.List;

import com.hotel.dto.ApiResponse;
import com.hotel.dto.ChangePasswordDto;
import com.hotel.dto.RoomReqDto;
import com.hotel.dto.RoomRespDto;
import com.hotel.entities.Room;

public interface ManagerService {
	
	RoomRespDto addRooms(RoomReqDto roomdto);
	
	List<RoomRespDto> getAllRooms();
	
	RoomRespDto getRoomById(Long id);
	
	RoomRespDto getRoomByRoomNumber(String roomNo);
	
	List<RoomRespDto> getRoomByCategory(String category);
	
	Room updateRoom(Long id, Room room);
	
	Room updateRoomByRoomNumber(String id, Room room);
	
	ApiResponse deleteRoom(Long id);
	
	ApiResponse deleteRoomByRoomNumber(String roomNo);
	
	String changePassword(ChangePasswordDto dto);

}
