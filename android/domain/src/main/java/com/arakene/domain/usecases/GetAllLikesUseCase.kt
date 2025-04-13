package com.arakene.domain.usecases

import com.arakene.domain.repository.LikeRepository
import javax.inject.Inject

class GetAllLikesUseCase @Inject constructor(
    private val likeRepository: LikeRepository
) {

    suspend operator fun invoke() = likeRepository.getAll()

}