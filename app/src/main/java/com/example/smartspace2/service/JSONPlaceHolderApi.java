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
import retrofit2.http.Query;

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

// API по получению данных

    // Метод получения данных. Если путь / - данные с микроконтроллера, если /rooms - данные с заглушки
    @GET("/rooms")
    public Call<RoomDto[]> getData();

    @GET("/history")
    public Call<RoomDto[]> getHistory(@Query("id") int id, @Query("start") String start, @Query("end") String end);
}
