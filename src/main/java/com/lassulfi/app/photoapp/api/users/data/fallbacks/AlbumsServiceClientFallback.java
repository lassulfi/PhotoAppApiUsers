package com.lassulfi.app.photoapp.api.users.data.fallbacks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lassulfi.app.photoapp.api.users.data.AlbumsServiceClient;
import com.lassulfi.app.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;

public class AlbumsServiceClientFallback implements AlbumsServiceClient {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Throwable cause;
	
	public AlbumsServiceClientFallback(Throwable cause) {
		super();
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		if(cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error took place when getAlbums was called with userId : " + id 
					+ ". Error message: " + cause.getLocalizedMessage());
		} else {
			logger.error("Other error took place " + cause.getLocalizedMessage());
		}
		
		return new ArrayList<>();
	}

}
