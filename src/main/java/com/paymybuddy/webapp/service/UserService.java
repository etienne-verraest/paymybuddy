package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findUserById(Integer id) {
		return userRepository.findUserById(id);
	}
}
