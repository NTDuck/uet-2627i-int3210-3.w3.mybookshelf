package com.github.ntduck.int3210_3.mybookshelf.providers

import com.github.ntduck.int3210_3.mybookshelf.models.BookItem
import com.github.ntduck.int3210_3.mybookshelf.models.BookQueryResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BookProvider {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BookQueryResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): BookItem
}

object RetrofitBookProvider {
    val retrofitService: BookProvider by lazy {
        retrofit.create(BookProvider::class.java)
    }
}
