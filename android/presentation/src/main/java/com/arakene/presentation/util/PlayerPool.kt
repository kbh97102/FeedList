package com.arakene.presentation.util

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

class PlayerPool(
    private val context: Context,
    private val maxSize: Int = 3
) {
    private val available = ArrayDeque<ExoPlayer>()
    private val inUse = mutableMapOf<String, ExoPlayer>()

    fun getPlayer(tag: String): ExoPlayer {
        // 이미 사용 중이면 그대로 반환
        inUse[tag]?.let { return it }

        val player = if (available.isNotEmpty()) {
            available.removeFirst()
        } else if (inUse.size < maxSize) {
            ExoPlayer.Builder(context).build()
        } else {
            // 오래된 것 회수
            val oldTag = inUse.keys.first()
            val oldPlayer = inUse.remove(oldTag)!!
            oldPlayer.stop()
            oldPlayer.clearMediaItems()
            oldPlayer
        }

        inUse[tag] = player
        return player
    }

    fun release(tag: String) {
        inUse.remove(tag)?.let {
            it.stop()
            it.clearMediaItems()
            available.addLast(it)
        }
    }

    fun releaseAll() {
        inUse.values.forEach { it.release() }
        available.forEach { it.release() }
        inUse.clear()
        available.clear()
    }
}