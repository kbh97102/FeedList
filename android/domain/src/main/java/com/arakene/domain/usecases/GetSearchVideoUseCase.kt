package com.arakene.domain.usecases

import com.arakene.domain.repository.VideoRepository
import javax.inject.Inject

class GetSearchVideoUseCase @Inject constructor(
    private val repository: VideoRepository
) {

    suspend operator fun invoke(search: String) = repository.getVideos(search)

}