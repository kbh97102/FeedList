package com.arakene.data.repositoryimpl

import com.arakene.data.Api
import com.arakene.domain.repository.VideoRepository
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse
import javax.inject.Inject


// TODO: Response Wrapper 생성해서 추가
// TODO: 임시로 빌드하려고 수정
class VideoRepositoryImpl @Inject constructor(
    private val api: Api
) : VideoRepository{

    override suspend fun getVideos(): VideoListResponse? {
        return api.getVideos().body()
    }

    override suspend fun getPopularVideo(): VideoListResponse? {
        return api.getPopularVideos().body()
    }

    override suspend fun getVideo(id: String): VideoListResponse {
        return api.getVideos(id).body()!!
    }
}