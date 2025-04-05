package com.arakene.data.repositoryimpl

import com.arakene.data.Api
import com.arakene.domain.repository.VideoRepository
import com.arakene.domain.responses.VideoDto
import javax.inject.Inject


// TODO: Response Wrapper 생성해서 추가
// TODO: 임시로 빌드하려고 수정
class VideoRepositoryImpl @Inject constructor(
    private val api: Api
) : VideoRepository{

    override suspend fun getVideos(): VideoDto {
        return api.getVideos().body()!!
    }

    override suspend fun getPopularVideo(): VideoDto {
        return api.getPopularVideos().body()!!
    }

    override suspend fun getVideo(id: String): VideoDto {
        return api.getVideos(id).body()!!
    }
}