package mateustorres.biblioteca.network


import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class OpenLibraryResponse(
    val docs: List<BookDoc>
)

data class BookDoc(
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?
)

interface OpenLibraryService {
    @GET("search.json")
    suspend fun buscarLivros(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): OpenLibraryResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://openlibrary.org/"

    val api: OpenLibraryService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryService::class.java)
    }
}