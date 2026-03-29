package com.github.ntduck.int3210_3.mybookshelf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.github.ntduck.int3210_3.mybookshelf.models.BookItem
import com.github.ntduck.int3210_3.mybookshelf.providers.RetrofitBookProvider
import kotlinx.coroutines.launch

sealed interface BookUiState {
    data class Success(val book: BookItem) : BookUiState
    object Error : BookUiState
    object Loading : BookUiState
}

class BookViewModel : ViewModel() {
    var bookUiState: BookUiState by mutableStateOf(BookUiState.Loading)
    var selectedBook: BookItem? by mutableStateOf(null)

    fun getBook(id: String) {
        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            try {
                val book = RetrofitBookProvider.retrofitService.getBook(id)
                bookUiState = BookUiState.Success(book)
            } catch (e: Exception) {
                bookUiState = BookUiState.Error
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    viewModel: BookViewModel,
    bookId: String,
    onBackClick: () -> Unit
) {
    LaunchedEffect(bookId) {
        viewModel.getBook(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = viewModel.bookUiState) {
                is BookUiState.Loading -> LoadingScreen()
                is BookUiState.Error -> ErrorScreen { viewModel.getBook(bookId) }
                is BookUiState.Success -> BookDetail(state.book)
            }
        }
    }
}

@Composable
fun BookDetail(book: BookItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = book.volumeInfo.imageLinks?.httpsThumbnail,
            contentDescription = book.volumeInfo.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.headlineSmall
        )
        book.volumeInfo.authors?.let { authors ->
            Text(
                text = "By ${authors.joinToString(", ")}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        book.volumeInfo.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
