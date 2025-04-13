package com.arakene.domain.repository

import androidx.paging.PagingData
import com.arakene.domain.responses.ApiResult
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    suspend fun getVideos(search: String): ApiResult<VideoListResponse>

    suspend fun getPopularVideo(): Flow<PagingData<VideoDto>>

    suspend fun getVideo(id: Int): ApiResult<VideoDto>

}