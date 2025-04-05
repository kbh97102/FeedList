package com.arakene.domain.usecases

import com.arakene.domain.repository.VideoRepository
import javax.inject.Inject

class GetPopularVideoUseCase @Inject constructor(
    private val repository: VideoRepository
) {

    suspend operator fun invoke() = repository.getPopularVideo()

}