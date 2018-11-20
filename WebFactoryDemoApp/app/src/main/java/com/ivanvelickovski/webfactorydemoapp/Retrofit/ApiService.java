package com.ivanvelickovski.webfactorydemoapp.Retrofit;

import com.ivanvelickovski.webfactorydemoapp.Model.VolumeData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("courseapi/books.json")
    Call<VolumeData> getVolumeData();
}