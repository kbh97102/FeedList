package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto

@Composable
fun FeedDetail(
    videoDto: VideoDto,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    LaunchedEffect(videoDto) {

        /**
         *
         * Media Item
         *  Media Item 은 어떤 영상을 재생할지에 대한 정보
         *
         * Media Source
         *  영상을 어떻게 재생할지
         *
         *
         */

        val videoFile = videoDto.videoFiles.firstOrNull() ?: return@LaunchedEffect

        val mediaItem = MediaItem.Builder()
            .setUri(videoFile.link)
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.run {
            // TODO: 뭔지 궁금한 친구들
//            setImageOutput()
//            setCameraMotionListener()
//            setHandleAudioBecomingNoisy()
//            setImageOutput()
//            setPreferredAudioDevice()
//            setVideoEffects()
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = exoPlayer
                    useController = false
                }
            }
        )
    }


}