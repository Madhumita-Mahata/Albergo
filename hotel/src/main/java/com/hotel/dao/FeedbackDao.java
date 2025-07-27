package com.hotel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Feedback;

public interface FeedbackDao extends JpaRepository<Feedback, Long>{

}
