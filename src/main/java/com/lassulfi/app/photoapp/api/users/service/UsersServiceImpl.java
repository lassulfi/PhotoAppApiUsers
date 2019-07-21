package com.lassulfi.app.photoapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lassulfi.app.photoapp.api.users.data.UserEntity;
import com.lassulfi.app.photoapp.api.users.data.UserRepository;
import com.lassulfi.app.photoapp.api.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

	private UserRepository userRespository;
	
	@Autowired
	public UsersServiceImpl(UserRepository userRepository) {
		this.userRespository = userRepository;
	}
	
	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = mapper.map(userDetails, UserEntity.class);
		userEntity.setEncryptedPassword("test");

		userRespository.save(userEntity);
		
		return null;
	}

}
