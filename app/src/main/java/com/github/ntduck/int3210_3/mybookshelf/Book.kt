package com.github.ntduck.int3210_3.mybookshelf

import com.google.gson.annotations.SerializedName

data class BookQueryResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String
) {
    // The Google Books API often returns HTTP instead of HTTPS,
    // which Coil blocks by default. This fixes it.
    val httpsThumbnail: String
        get() = thumbnail.replace("http:", "https:")
}