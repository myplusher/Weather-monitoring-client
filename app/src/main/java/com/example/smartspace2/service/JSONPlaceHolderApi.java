package com.example.smartspace2.service;

import com.example.smartspace2.dto.LocationDto;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.dto.RoomDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
// API по микроконтроллерам

    @GET("/controllers")
    public Call<MCDto[]> getMCList();

    @PUT("/controllers/{id}")
    public Call<MCDto> updateMC(@Path("id") int id, @Body MCDto data);

    @POST("/controllers")
    public Call<MCDto> createMC(@Body MCDto data);

// API по расположениям
    @POST("/locations")
    public Call<LocationDto> createLocation(@Body LocationDto location);

    @GET("/")
    public Call<RoomDto[]> getData();
}
