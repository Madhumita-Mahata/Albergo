package com.hotel.dao;

<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 9ae400196ed3dfa7ab7220074a4c543dfae96560
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entities.Role;
import com.hotel.entities.User;


<<<<<<< HEAD
	boolean existsByEmail(String email);


	Optional<User> findByEmail(String email);


	Optional<User> findByUserId(Long id);
=======

public interface UserDao extends JpaRepository<User, Long> {
	
	boolean existsByEmail(String email);

	List<User> findByRole(Role role);
>>>>>>> 9ae400196ed3dfa7ab7220074a4c543dfae96560
}