package com.mark.dru_movies.data.remote.dtos

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val popularity: Double,
    val adult: Boolean,
    val original_language: String,
    val original_title: String
)