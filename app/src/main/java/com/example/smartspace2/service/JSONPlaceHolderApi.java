package com.example.smartspace2.service;

import com.example.smartspace2.dto.MCDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @PUT("/controllers/{id}")
    public Call<MCDto> updateMC(@Path("id") int id, @Body MCDto data);
}
