package com.arakene.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Like(
    val videoId: Int,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
)
