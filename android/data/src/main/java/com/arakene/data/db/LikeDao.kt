package com.arakene.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.arakene.domain.entities.Like

@Dao
interface LikeDao {

    @Query("SELECT * FROM `Like`")
    suspend fun getAll(): List<Like>

    @Query("SELECT * FROM `like` WHERE uid IN (:uid)")
    suspend fun get(uid: String): Like

    @Insert
    suspend fun insert(like: Like)

    @Query("DELETE FROM `like` WHERE videoId in (:videoId)")
    suspend fun delete(videoId: Int)

}