package com.github.ntduck.int3210_3.mybookshelf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun BookshelfScreen(
    booksUiState: BooksUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (booksUiState) {
        is BooksUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BooksUiState.Success -> BooksGridScreen(
            books = booksUiState.books,
            modifier = modifier.fillMaxWidth()
        )
        is BooksUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun BooksGridScreen(books: List<BookItem>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.padding(4.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            BookCard(book = book, modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(0.6f))
        }
    }
}

@Composable
fun BookCard(book: BookItem, modifier: Modifier = Modifier) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks?.httpsThumbnail)
                .crossfade(true)
                .build(),
            contentDescription = book.volumeInfo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Failed to load books.")
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}