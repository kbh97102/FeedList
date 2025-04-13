package com.arakene.data.repositoryimpl

import com.arakene.data.db.LikeDao
import com.arakene.domain.entities.Like
import com.arakene.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val dao: LikeDao
): LikeRepository {

    override suspend fun getAll(): List<Like> {
        return dao.getAll()
    }

    override suspend fun get(uid: String): Like? {
        return dao.get(uid)
    }

    override suspend fun insert(like: Like) {
        return dao.insert(like)
    }

    override suspend fun delete(videoId: Int) {
        return dao.delete(videoId)
    }
}