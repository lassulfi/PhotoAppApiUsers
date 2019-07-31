package com.lassulfi.app.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lassulfi.app.photoapp.api.users.data.UserEntity;
import com.lassulfi.app.photoapp.api.users.data.UserRepository;
import com.lassulfi.app.photoapp.api.users.shared.UserDto;
import com.lassulfi.app.photoapp.api.users.ui.model.AlbumResponseModel;

@Service
public class UsersServiceImpl implements UsersService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private RestTemplate restTemplate;
	private Environment environment;
	
	@Autowired
	public UsersServiceImpl(UserRepository userRepository, 
			BCryptPasswordEncoder passwordEncoder,
			RestTemplate restTemplate,
			Environment environment) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.restTemplate = restTemplate;
		this.environment = environment;
	}
	
	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = mapper.map(userDetails, UserEntity.class);

		userRepository.save(userEntity);
		
		UserDto savedUser = mapper.map(userEntity, UserDto.class);
		
		return savedUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		
		if(userEntity == null) throw new UsernameNotFoundException(username);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = this.userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserById(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException("User not found");
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
		String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
				
		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate
				.exchange(albumsUrl, 
						HttpMethod.GET, 
						null,
						new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
		List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
		userDto.setAlbums(albumsList);
		
		return userDto;
	}
}
