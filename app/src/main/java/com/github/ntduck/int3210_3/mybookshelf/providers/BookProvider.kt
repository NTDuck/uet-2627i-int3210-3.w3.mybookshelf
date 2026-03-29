package com.github.ntduck.int3210_3.mybookshelf.providers

import com.github.ntduck.int3210_3.mybookshelf.models.Book
import com.github.ntduck.int3210_3.mybookshelf.models.Books
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookProvider {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): Books

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): Book
}

class RetrofitBookProvider(
    private val retrofit: BookProvider = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://www.googleapis.com/books/v1/")
        .build()
        .create(BookProvider::class.java)
): BookProvider {
    override suspend fun getBooks(query: String): Books {
        return retrofit.getBooks(query)
    }

    override suspend fun getBook(id: String): Book {
        return retrofit.getBook(id)
    }
}
