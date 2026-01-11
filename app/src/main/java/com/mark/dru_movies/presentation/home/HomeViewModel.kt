package com.mark.dru_movies.presentation.home

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
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getMovies().fold(
                onSuccess = { movies ->
                    _uiState.value = HomeUiState.Success(movies)
                },
                onFailure = { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun refreshMovies() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refreshMovies().fold(
                onSuccess = { movies ->
                    _uiState.value = HomeUiState.Success(movies)
                },
                onFailure = { error ->
                    if (_uiState.value !is HomeUiState.Success) {
                        _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                    }
                }
            )
            _isRefreshing.value = false
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            if (!repository.hasMorePages()) return@launch

            val currentState = _uiState.value
            if (currentState is HomeUiState.Success && !currentState.isLoadingMore) {
                _uiState.value = currentState.copy(isLoadingMore = true)

                repository.getNextPage().fold(
                    onSuccess = { newMovies ->
                        val currentMovies = (uiState.value as? HomeUiState.Success)?.movies ?: emptyList()
                        _uiState.value = HomeUiState.Success(
                            movies = currentMovies + newMovies,
                            isLoadingMore = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = currentState.copy(
                            isLoadingMore = false,
                            paginationError = error.message
                        )
                    }
                )
            }
        }
    }

    fun clearPaginationError() {
        val currentState = _uiState.value
        if (currentState is HomeUiState.Success && currentState.paginationError != null) {
            _uiState.value = currentState.copy(paginationError = null)
        }
    }
}