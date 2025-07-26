package com.hotel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="users")
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "name", callSuper = false)

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long userId;
	@Column(name = "user_name", length = 20)
	private String name;
	@Column(length = 30, unique = true)
	private String email;
	@Column(length = 200, nullable = false)
	private String password;
	@Column(length = 10)
	private String phone;
	@Column(length = 15)
	private String gender;
	@Column(name = "user_address", length = 255)
	private String address;
	@Column(name = "user_id", length =12)
	private String IdCard;
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role = Role.USER;
	
	
	
	
}