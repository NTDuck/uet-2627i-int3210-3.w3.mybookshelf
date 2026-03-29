package com.github.ntduck.int3210_3.mybookshelf.models

data class Books(
    val items: List<Book>?
)

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String
) {
    val httpsThumbnail: String
        get() = thumbnail.replace("http:", "https:")
}
