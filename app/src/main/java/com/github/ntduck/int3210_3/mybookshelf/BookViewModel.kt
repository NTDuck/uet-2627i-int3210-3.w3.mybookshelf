package com.github.ntduck.int3210_3.mybookshelf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed interface BooksUiState {
    data class Success(val books: List<BookItem>) : BooksUiState
    object Error : BooksUiState
    object Loading : BooksUiState
}

class BooksViewModel : ViewModel() {
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            booksUiState = try {
                // Searching for books about "Android Compose" as a default
                val response = BooksApi.retrofitService.getBooks("Android Compose")
                BooksUiState.Success(response.items ?: emptyList())
            } catch (e: Exception) {
                BooksUiState.Error
            }
        }
    }
}