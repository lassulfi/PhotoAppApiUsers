package com.lassulfi.app.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.lassulfi.app.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService {

	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
	UserDto getUserById(String userId);
}
