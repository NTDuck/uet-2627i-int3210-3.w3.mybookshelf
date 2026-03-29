package com.github.ntduck.int3210_3.mybookshelf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.github.ntduck.int3210_3.mybookshelf.models.Book
import com.github.ntduck.int3210_3.mybookshelf.providers.BookProvider
import com.github.ntduck.int3210_3.mybookshelf.providers.RetrofitBookProvider
import kotlinx.coroutines.launch

class BookViewModel(private val bookProvider: BookProvider = RetrofitBookProvider()) : ViewModel() {
    sealed interface State {
        data class Success(val book: Book) : State
        object Error : State
        object Loading : State
    }

    var state: State by mutableStateOf(State.Loading)

    fun getBook(id: String) {
        viewModelScope.launch {
            state = State.Loading
            state = try {
                State.Success(book = bookProvider.getBook(id))
            } catch (_: Exception) {
                State.Error
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = viewModel.state) {
                is BookViewModel.State.Loading -> LoadingScreen()
                is BookViewModel.State.Error -> ErrorScreen { viewModel.getBook(id = bookId) }
                is BookViewModel.State.Success -> BookDetail(book = state.book)
            }
        }
    }
}

@Composable
fun BookDetail(book: Book) {
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
