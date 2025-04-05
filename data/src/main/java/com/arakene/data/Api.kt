package com.arakene.data

import com.arakene.domain.responses.VideoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("videos/search")
    suspend fun getVideos(): Response<VideoDto>

    @GET("videos/popular")
    suspend fun getPopularVideos(): Response<VideoDto>

    @GET("videos/videos/{id}")
    suspend fun getVideos(
        @Path("id") id: String
    ): Response<VideoDto>

}