package com.arakene.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arakene.data.Api
import com.arakene.data.datasource.PopularVideosDataSource
import com.arakene.domain.repository.VideoRepository
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// TODO: Response Wrapper 생성해서 추가
// TODO: 임시로 빌드하려고 수정
class VideoRepositoryImpl @Inject constructor(
    private val api: Api
) : VideoRepository {

    override suspend fun getVideos(): VideoListResponse? {
        return api.getVideos().body()
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

    override suspend fun getVideo(id: String): VideoListResponse {
        return api.getVideos(id).body()!!
    }
}