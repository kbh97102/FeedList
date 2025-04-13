package com.arakene.domain.usecases

import com.arakene.domain.entities.Like
import com.arakene.domain.repository.LikeRepository
import javax.inject.Inject

class DeleteLikeUseCase @Inject constructor(
    private val likeRepository: LikeRepository
) {

    suspend operator fun invoke(videoId: Int) = likeRepository.delete(videoId)

}