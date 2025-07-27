package com.hotel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Room;

public interface RoomDao extends JpaRepository<Room, Long> {

}
