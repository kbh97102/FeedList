package com.arakene.domain.usecases

import com.arakene.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(
    private val repository: VideoRepository
) {

    suspend operator fun invoke(id: Int) = repository.getVideo(id)

}