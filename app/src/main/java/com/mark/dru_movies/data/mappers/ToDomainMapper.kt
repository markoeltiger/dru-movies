package com.mark.dru_movies.data.mappers

import com.mark.dru_movies.data.local.entity.MovieEntity
import com.mark.dru_movies.domain.model.Movie

fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle
)