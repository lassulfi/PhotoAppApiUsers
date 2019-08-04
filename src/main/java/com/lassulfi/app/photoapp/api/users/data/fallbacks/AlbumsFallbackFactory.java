package com.lassulfi.app.photoapp.api.users.data.fallbacks;

import org.springframework.stereotype.Component;

import com.lassulfi.app.photoapp.api.users.data.AlbumsServiceClient;

import feign.hystrix.FallbackFactory;

@Component
public class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

	@Override
	public AlbumsServiceClient create(Throwable cause) {
		return new AlbumsServiceClientFallback(cause);
	}


}
