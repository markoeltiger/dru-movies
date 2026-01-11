package com.mark.dru_movies.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mark.dru_movies.domain.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: 0

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            repository.getMovieById(movieId).fold(
                onSuccess = { movie ->
                    _uiState.value = DetailsUiState.Success(movie)
                },
                onFailure = { error ->
                    _uiState.value = DetailsUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
