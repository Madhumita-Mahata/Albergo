package com.hotel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.custom_exception.ApiException;
import com.hotel.custom_exception.ResourceNotFoundException;
import com.hotel.dao.RoomDao;
import com.hotel.dto.ApiResponse;
import com.hotel.dto.ChangePasswordDto;
import com.hotel.dto.RoomReqDto;
import com.hotel.dto.RoomRespDto;
import com.hotel.entities.Category;
import com.hotel.entities.Room;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

	private final ModelMapper modelMapper;
	
	private final RoomDao roomDao;
	
	@Override
	public RoomRespDto addRooms(RoomReqDto roomDto) {
		if(roomDao.existsByRoomNumber(roomDto.getRoomNumber()))
			throw new ApiException("Duplicate room");
		Room room = modelMapper.map(roomDto, Room.class);
		Category category = Category.valueOf(roomDto.getCategory().toUpperCase());
		room.setCategory(category);
		return modelMapper.map(roomDao.save(room), RoomRespDto.class);
	}

	@Override
	public List<RoomRespDto> getAllRooms() {
		return roomDao.findAll()
				.stream()
				.map(room->modelMapper.map(room, RoomRespDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public RoomRespDto getRoomById(Long id) {
		Room room = roomDao.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Invalid room no"));
		return modelMapper.map(room, RoomRespDto.class);
	}

	@Override
	public List<RoomRespDto> getRoomByCategory(String category) {
		Category cat = Category.valueOf(category.toUpperCase()) ;
		List<Room> rooms = roomDao.findByCategory(cat);
		return rooms.stream()
				.map(room->modelMapper.map(room, RoomRespDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Room updateRoom(Long id, Room room) {
		Room room1 = roomDao.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Invalid id"));
		return roomDao.save(room1);
	}

	@Override
	public ApiResponse deleteRoom(Long id) {
		Room room = roomDao.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Invalid id"));
		roomDao.delete(room);
		return new ApiResponse("Room deleted");
	}

	@Override
	public String changePassword(ChangePasswordDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomRespDto getRoomByRoomNumber(String roomNo) {
		Room room = roomDao.findByRoomNumber(roomNo)
				.orElseThrow(()->new ResourceNotFoundException("Invalid room no"));
		return modelMapper.map(room, RoomRespDto.class);
	}

	@Override
	public Room updateRoomByRoomNumber(String id, Room room) {
		Room room1 = roomDao.findByRoomNumber(id)
				.orElseThrow(()->new ResourceNotFoundException("Invalid room number"));
		return roomDao.save(room1);
	}

	@Override
	public ApiResponse deleteRoomByRoomNumber(String roomNo) {
		Room room = roomDao.findByRoomNumber(roomNo)
				.orElseThrow(()->new ResourceNotFoundException("Invalid id"));
		roomDao.delete(room);
		return new ApiResponse("Room deleted");
	}

}
