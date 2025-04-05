package com.arakene.data

import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("videos/search")
    suspend fun getVideos(): Response<VideoListResponse>

    @GET("videos/popular")
    suspend fun getPopularVideos(): Response<VideoListResponse>

    @GET("videos/videos/{id}")
    suspend fun getVideos(
        @Path("id") id: String
    ): Response<VideoListResponse>

}