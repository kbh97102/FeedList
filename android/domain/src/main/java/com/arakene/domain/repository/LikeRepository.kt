package com.arakene.domain.repository

import com.arakene.domain.entities.Like

interface LikeRepository {

    suspend fun getAll(): List<Like>

    suspend fun get(uid: String): Like?

    suspend fun insert(like: Like)

    suspend fun delete(videoId: Int)
}