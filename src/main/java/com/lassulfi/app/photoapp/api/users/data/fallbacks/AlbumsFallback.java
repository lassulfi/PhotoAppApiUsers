package com.lassulfi.app.photoapp.api.users.data.fallbacks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lassulfi.app.photoapp.api.users.data.AlbumsServiceClient;
import com.lassulfi.app.photoapp.api.users.ui.model.AlbumResponseModel;

@Component
public class AlbumsFallback implements AlbumsServiceClient {

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {

		return new ArrayList<>();
	}

}
