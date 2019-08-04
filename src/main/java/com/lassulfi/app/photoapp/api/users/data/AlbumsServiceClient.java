package com.lassulfi.app.photoapp.api.users.data;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lassulfi.app.photoapp.api.users.data.fallbacks.AlbumsFallbackFactory;
import com.lassulfi.app.photoapp.api.users.ui.model.AlbumResponseModel;

@FeignClient(name="albums-ws", fallbackFactory=AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {
	
	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}