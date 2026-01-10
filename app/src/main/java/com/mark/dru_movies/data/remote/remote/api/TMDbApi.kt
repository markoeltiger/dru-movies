package com.mark.dru_movies.data.remote.remote.api

import com.mark.dru_movies.data.remote.dtos.MovieDto
import com.mark.dru_movies.data.remote.dtos.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDto
}