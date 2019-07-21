package com.lassulfi.app.photoapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lassulfi.app.photoapp.api.users.data.UserEntity;
import com.lassulfi.app.photoapp.api.users.data.UserRepository;
import com.lassulfi.app.photoapp.api.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

	private UserRepository userRespository;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRespository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = mapper.map(userDetails, UserEntity.class);

		userRespository.save(userEntity);
		
		UserDto savedUser = mapper.map(userEntity, UserDto.class);
		
		return savedUser;
	}

}
