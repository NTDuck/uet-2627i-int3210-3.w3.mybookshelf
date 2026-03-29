package com.github.ntduck.int3210_3.mybookshelf.models

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
    val httpsThumbnail: String
        get() = thumbnail.replace("http:", "https:")
}