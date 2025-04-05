package com.arakene.domain.repository

import com.arakene.domain.responses.VideoDto

interface VideoRepository {

    suspend fun getVideos(): VideoDto

    suspend fun getPopularVideo(): VideoDto

    suspend fun getVideo(id: String): VideoDto

}