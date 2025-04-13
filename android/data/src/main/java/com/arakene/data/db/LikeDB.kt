package com.arakene.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arakene.domain.entities.Like

@Database(entities = [Like::class], version = 1)
abstract class LikeDB: RoomDatabase() {

    abstract fun getLikeDao(): LikeDao

}