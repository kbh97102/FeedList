package com.arakene.domain.repository

import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse

interface VideoRepository {

    suspend fun getVideos(): VideoListResponse?

    suspend fun getPopularVideo(): VideoListResponse?

    suspend fun getVideo(id: String): VideoListResponse

}