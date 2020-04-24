package com.example.loginapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("bhej")
    Call<List<Post>> getPosts();

    @GET("call")
    Call<List<Post>> getPostAgain();

    @POST("bhej")
    Call<Post> createPost(@Body Post post);
}
