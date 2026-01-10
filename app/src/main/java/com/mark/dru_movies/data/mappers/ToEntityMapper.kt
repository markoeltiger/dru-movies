package com.mark.dru_movies.data.mappers

import com.mark.dru_movies.data.local.entity.MovieEntity
import com.mark.dru_movies.data.remote.dtos.MovieDto

fun MovieDto.toEntity(timestamp: Long) = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = poster_path,
    backdropPath = backdrop_path,
    releaseDate = release_date,
    voteAverage = vote_average,
    voteCount = vote_count,
    popularity = popularity,
    adult = adult,
    originalLanguage = original_language,
    originalTitle = original_title,
    lastUpdated = timestamp
)
