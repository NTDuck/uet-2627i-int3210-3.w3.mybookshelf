package com.github.ntduck.int3210_3.mybookshelf.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.github.ntduck.int3210_3.mybookshelf.models.BookItem
import com.github.ntduck.int3210_3.mybookshelf.providers.RetrofitBookProvider
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val books: List<BookItem>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
    var searchQuery by mutableStateOf("cat")

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            try {
                val response = RetrofitBookProvider.retrofitService.getBooks(searchQuery)
                homeUiState = HomeUiState.Success(response.items ?: emptyList())
            } catch (e: Exception) {
                homeUiState = HomeUiState.Error
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (BookItem) -> Unit
) {
    Column {
        Text(
            text = "My Bookshelf",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { 
                viewModel.searchQuery = it
                viewModel.getBooks()
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        when (val state = viewModel.homeUiState) {
            is HomeUiState.Loading -> LoadingScreen()
            is HomeUiState.Success -> BooksGrid(state.books, onBookClick)
            is HomeUiState.Error -> ErrorScreen { viewModel.getBooks() }
        }
    }
}

@Composable
fun BooksGrid(books: List<BookItem>, onBookClick: (BookItem) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(books) { book ->
            AsyncImage(
                model = book.volumeInfo.imageLinks?.httpsThumbnail,
                contentDescription = book.volumeInfo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(0.7f)
                    .clickable { onBookClick(book) }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error loading books")
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}