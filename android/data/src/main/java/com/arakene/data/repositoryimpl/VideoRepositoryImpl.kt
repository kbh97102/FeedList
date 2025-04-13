package com.arakene.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arakene.data.Api
import com.arakene.data.datasource.PopularVideosDataSource
import com.arakene.data.util.safeApi
import com.arakene.domain.repository.VideoRepository
import com.arakene.domain.responses.ApiResult
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class VideoRepositoryImpl @Inject constructor(
    private val api: Api
) : VideoRepository {

    override suspend fun getVideos(search: String): ApiResult<VideoListResponse> {
        return safeApi {
            api.getVideos(search)
        }
    }

    override suspend fun getPopularVideo(): Flow<PagingData<VideoDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PopularVideosDataSource(api)
            }
        ).flow
    }

    override suspend fun getVideo(id: Int): ApiResult<VideoDto> {
        return safeApi {
            api.getVideo(id)
        }
    }
}