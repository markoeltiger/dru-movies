package com.mark.dru_movies.domain.repo

import com.mark.dru_movies.domain.model.Movie

interface MovieRepository {
    suspend fun getMovies(): Result<List<Movie>>
    suspend fun getMovies(page: Int, forceRefresh: Boolean = false): Result<List<Movie>>
    suspend fun getNextPage(): Result<List<Movie>>
    suspend fun hasMorePages(): Boolean
    suspend fun refreshMovies(): Result<List<Movie>>
    suspend fun getMovieById(movieId: Int): Result<Movie>
}