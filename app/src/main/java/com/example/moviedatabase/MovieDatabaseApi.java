package com.example.moviedatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MovieDatabaseApi {

    @GET()
    Call<Post> getPost(@Url String url);
}
