package com.mark.dru_movies.data.repo

import com.mark.dru_movies.data.local.dao.CacheMetadataDao
import com.mark.dru_movies.data.local.dao.MovieDao
import com.mark.dru_movies.data.local.entity.CacheMetadata
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
    private val cacheMetadataDao: CacheMetadataDao
) : MovieRepository {

    private var currentPage = 1
    private var totalPages = 1
    private val loadedMovieIds = mutableSetOf<Int>()

    override suspend fun getMovies(page: Int, forceRefresh: Boolean): Result<List<Movie>> {
        return try {
            if (forceRefresh) {
                currentPage = 1
                loadedMovieIds.clear()
                movieDao.deleteAllMovies()
            }

            val currentTime = System.currentTimeMillis()
            val response = api.getPopularMovies(Constants.API_KEY, page)

            totalPages = response.total_pages ?: 1
            currentPage = page

            val entities = response.results.map { it.toEntity(currentTime) }

            val newMovies = entities.filter { !loadedMovieIds.contains(it.id) }
            loadedMovieIds.addAll(newMovies.map { it.id })

            movieDao.insertMovies(newMovies)

            if (page == 1) {
                cacheMetadataDao.insertCacheMetadata(CacheMetadata(0, currentTime))
            }

            Result.success(newMovies.map { it.toDomain() })

        } catch (e: Exception) {
            if (page == 1) {
                val cachedMovies = movieDao.getAllMovies()
                if (cachedMovies.isNotEmpty()) {
                    Result.success(cachedMovies.map { it.toDomain() })
                } else {
                    Result.failure(e)
                }
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMovies(): Result<List<Movie>> {
        return getMovies(page = 1, forceRefresh = false)
    }

    override suspend fun hasMorePages(): Boolean {
        return currentPage < totalPages
    }

    override suspend fun getNextPage(): Result<List<Movie>> {
        return if (hasMorePages()) {
            getMovies(page = currentPage + 1, forceRefresh = false)
        } else {
            Result.success(emptyList())
        }
    }

    override suspend fun refreshMovies(): Result<List<Movie>> {
        return getMovies(page = 1, forceRefresh = true)
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