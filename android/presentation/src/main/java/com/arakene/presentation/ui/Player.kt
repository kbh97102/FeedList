package com.arakene.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Player(
    url: String,
    thumbnailUrl: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    var isPlaying by remember {
        mutableStateOf(exoPlayer.isPlaying)
    }

    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    DisposableEffect(url) {

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

        val mediaItem = MediaItem.Builder()
            .setUri(url)
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

        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }

    }


    /*
    TODO
     영상 화질 설정
     */

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Black), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = exoPlayer
                    useController = false
                }
            }
        )

        if (!isPlaying) {
            GlideImage(
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            ControlMenu(
                play = {
                    if (!exoPlayer.isPlaying) {
                        exoPlayer.play()
                        isPlaying = exoPlayer.isPlaying
                    }
                },
                stop = {
                    // TODO Stop 에 다시 Play를 하면 재생이 안된다 그 이유가 뭘까 release 되는걸까? - 닥스보면 release 한다고함
                    exoPlayer.pause()
                    isPlaying = exoPlayer.isPlaying
                }
            )
        }
    }

}