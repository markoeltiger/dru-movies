package com.mark.dru_movies.di

import com.mark.dru_movies.data.local.dao.MovieDao
import com.mark.dru_movies.data.remote.remote.api.TMDbApi
import com.mark.dru_movies.data.repo.MovieRepositoryImpl
import com.mark.dru_movies.domain.repo.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: TMDbApi,
        movieDao: MovieDao,
    ): MovieRepository {
        return MovieRepositoryImpl(api, movieDao)
    }
}