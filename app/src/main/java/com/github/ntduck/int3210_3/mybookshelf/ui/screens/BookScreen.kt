package com.github.ntduck.int3210_3.mybookshelf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.github.ntduck.int3210_3.mybookshelf.models.BookItem

class BookViewModel : ViewModel() {
    var selectedBook: BookItem? by mutableStateOf(null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    viewModel: BookViewModel,
    onBackClick: () -> Unit
) {
    val book = viewModel.selectedBook
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            book?.let {
                AsyncImage(
                    model = it.volumeInfo.imageLinks?.httpsThumbnail,
                    contentDescription = it.volumeInfo.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it.volumeInfo.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                it.volumeInfo.authors?.let { authors ->
                    Text(
                        text = "By ${authors.joinToString(", ")}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                it.volumeInfo.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
