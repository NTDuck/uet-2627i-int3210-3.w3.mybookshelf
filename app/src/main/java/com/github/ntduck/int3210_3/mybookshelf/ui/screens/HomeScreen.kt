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
import com.github.ntduck.int3210_3.mybookshelf.models.Book
import com.github.ntduck.int3210_3.mybookshelf.providers.BookProvider
import com.github.ntduck.int3210_3.mybookshelf.providers.RetrofitBookProvider
import kotlinx.coroutines.launch

class HomeViewModel(private val bookProvider: BookProvider = RetrofitBookProvider()) : ViewModel() {
    sealed interface State {
        data class Success(val books: List<Book>) : State
        object Error : State
        object Loading : State
    }

    var state: State by mutableStateOf(State.Loading)
    var query by mutableStateOf("cat")

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            state = State.Loading
            state = try {
                State.Success(books = bookProvider.getBooks(query).items ?: emptyList())
            } catch (_: Exception) {
                State.Error
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (Book) -> Unit
) {
    Column {
        Text(
            text = "mybookshelf",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = viewModel.query,
            onValueChange = { 
                viewModel.query = it
                viewModel.getBooks()
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        when (val state = viewModel.state) {
            is HomeViewModel.State.Loading -> LoadingScreen()
            is HomeViewModel.State.Success -> BooksGrid(state.books, onBookClick)
            is HomeViewModel.State.Error -> ErrorScreen { viewModel.getBooks() }
        }
    }
}

@Composable
fun BooksGrid(books: List<Book>, onBookClick: (Book) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
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
