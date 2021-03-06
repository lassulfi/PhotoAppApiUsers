package com.lassulfi.app.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lassulfi.app.photoapp.api.users.service.UsersService;
import com.lassulfi.app.photoapp.api.users.shared.UserDto;
import com.lassulfi.app.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.lassulfi.app.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.lassulfi.app.photoapp.api.users.ui.model.UserResponseModel;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	private UsersService usersService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port") + 
				", with token = " + env.getProperty("token.secret");
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = mapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = usersService.createUser(userDto);
		
		CreateUserResponseModel returnValue = mapper.map(createdUser, CreateUserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}
	
	
	@GetMapping(value="/{userId}",
			produces = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE
			})
//	@PreAuthorize("principal == #userId")
	@PostAuthorize("principal == returnObject.body.userId()")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
		UserDto userDto = usersService.getUserById(userId);
		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
		
		return ResponseEntity.ok().body(returnValue);
	}
}
