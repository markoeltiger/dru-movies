package com.mark.dru_movies.data.remote.dtos

data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)