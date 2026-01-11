package com.mark.dru_movies.di

import android.content.Context
import androidx.room.Room
import com.mark.dru_movies.data.local.dao.CacheMetadataDao
import com.mark.dru_movies.data.local.dao.MovieDao
import com.mark.dru_movies.data.local.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        )   .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
    @Provides
    fun provideCachedMetaDataDao(database: MovieDatabase): CacheMetadataDao {
        return database.cacheMetadataDao()
    }
}
