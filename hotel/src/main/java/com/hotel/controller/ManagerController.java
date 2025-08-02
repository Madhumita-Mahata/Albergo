package com.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.RoomReqDto;
import com.hotel.entities.Room;
import com.hotel.service.ManagerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
@Validated
public class ManagerController {
	
	private final ManagerService managerService;
	
	@PostMapping("/room")
	public ResponseEntity<?> addRoom(@RequestBody RoomReqDto roomDto)
	{
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(managerService.addRooms(roomDto));
	}
	
	@GetMapping("/rooms")
	public ResponseEntity<?> getAllRooms()
	{
		return ResponseEntity
				.ok(managerService.getAllRooms());
	}
	
	@GetMapping("/rooms/id/{roomid}")
	public ResponseEntity<?> getRoomById(@PathVariable("roomid") Long roomid)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.getRoomById(roomid));
	}
	
	@GetMapping("/rooms/no/{no}")
	public ResponseEntity<?> getRoomByRoomNumber(@PathVariable("no") String no)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.getRoomByRoomNumber(no));
	}

	@GetMapping("/rooms/category/{category}")
	public ResponseEntity<?> getRoomByCategory(@PathVariable("category") String category)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.getRoomByCategory(category));
	}
	
	@PutMapping("/rooms/{roomid}")
	public ResponseEntity<?> updateRoom(@PathVariable("roomid") Long roomid, @RequestBody Room room)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.updateRoom(roomid, room));
	}
	
	@PutMapping("/rooms/no/{no}")
	public ResponseEntity<?> updateByRoomNo(@PathVariable("no") String no, @RequestBody Room room)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.updateRoomByRoomNumber(no, room));
	}
	
	@DeleteMapping("/rooms/{roomid}")
	public ResponseEntity<?> deleteRoom(@PathVariable("roomid") Long roomid)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.deleteRoom(roomid));
		
	}
	
	@DeleteMapping("/rooms/no/{roomNo}")
	public ResponseEntity<?> deleteByRoomNo(@PathVariable("roomNo") Long roomNo)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(managerService.deleteRoom(roomNo));
		
	}
	
}
