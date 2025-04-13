package com.arakene.data

import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("videos/search")
    suspend fun getVideos(
        @Query("query") query: String
    ): Response<VideoListResponse>

    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
    ): Response<VideoListResponse>

    @GET("videos/videos/{id}")
    suspend fun getVideo(
        @Path("id") id: Int
    ): Response<VideoDto>

}