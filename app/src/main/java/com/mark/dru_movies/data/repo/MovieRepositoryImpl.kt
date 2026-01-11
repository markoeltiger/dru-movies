package com.mark.dru_movies.data.repo

import com.mark.dru_movies.data.local.dao.MovieDao
import com.mark.dru_movies.data.mappers.toDomain
import com.mark.dru_movies.data.mappers.toEntity
import com.mark.dru_movies.data.remote.remote.api.TMDbApi
import com.mark.dru_movies.domain.model.Movie
import com.mark.dru_movies.domain.repo.MovieRepository
import com.mark.dru_movies.data.utils.Constants
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TMDbApi,
    private val movieDao: MovieDao,
) : MovieRepository {

    override suspend fun getMovies(): Result<List<Movie>> {
        return try {
             val currentTime = System.currentTimeMillis()


                val response = api.getPopularMovies(Constants.API_KEY)
                val entities = response.results.map { it.toEntity(currentTime) }

                movieDao.deleteAllMovies()
                movieDao.insertMovies(entities)

                Result.success(entities.map { it.toDomain() })

        } catch (e: Exception) {
            val cachedMovies = movieDao.getAllMovies()
            if (cachedMovies.isNotEmpty()) {
                Result.success(cachedMovies.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMovieById(movieId: Int): Result<Movie> {
        return try {
            val cached = movieDao.getMovieById(movieId)
            if (cached != null) {
                Result.success(cached.toDomain())
            } else {
                val movie = api.getMovieDetails(movieId, Constants.API_KEY)
                Result.success(movie.toEntity(System.currentTimeMillis()).toDomain())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}