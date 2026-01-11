package com.mark.dru_movies.presentation.details

import com.mark.dru_movies.domain.model.Movie

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val movie: Movie) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}