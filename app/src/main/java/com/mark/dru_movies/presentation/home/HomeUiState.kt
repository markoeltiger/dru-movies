package com.mark.dru_movies.presentation.home

import com.mark.dru_movies.domain.model.Movie

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val movies: List<Movie>,
        val isLoadingMore: Boolean = false,
        val paginationError: String? = null
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}