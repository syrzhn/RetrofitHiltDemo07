package ru.syrzhn.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CourseResponse(val courses: List<Course>)

interface ApiService {
    @GET("courses")
    suspend fun getCourses(): CourseResponse
}

class LocalJsonInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Read JSON from assets
        val json = context.assets.open("courses.json").bufferedReader().use { it.readText() }

        return Response.Builder()
            .code(200)
            .message(json)
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .body(json.toResponseBody("application/json".toMediaType()))
            .build()
    }
}

fun createRetrofit(context: Context): Retrofit {
    val client = OkHttpClient.Builder()
        .addInterceptor(LocalJsonInterceptor(context))
        .build()

    return Retrofit.Builder()
        .baseUrl("https://mock.api/") // Fake URL, not used
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}