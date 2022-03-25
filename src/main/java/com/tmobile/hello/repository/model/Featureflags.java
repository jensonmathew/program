package com.tmobile.hello.repository.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class Featureflags {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String user;
	private String email;
	private String password;
	private boolean active;
	private String roles;
}
