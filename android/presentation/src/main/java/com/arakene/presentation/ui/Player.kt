package com.arakene.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogD
import com.arakene.presentation.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Player(
    videoDto: VideoDto,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    var isPlaying by remember {
        mutableStateOf(exoPlayer.isPlaying)
    }

    val qualityList by remember {
        mutableStateOf(
            videoDto.videoFiles.map {
                Triple(it.quality, it.fps, it.link)
            }
        )
    }

    LaunchedEffect(qualityList) {
        LogD("list $qualityList")
    }

    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    DisposableEffect(videoDto) {

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
            .setUri(videoDto.videoFiles.firstOrNull()?.link ?: "")
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

    var displayQuality by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.BottomEnd
    ) {
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
                model = videoDto.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }


        Box {
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Icon(
                    painter = painterResource(R.drawable.thumbs),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Icon(
                    painter = painterResource(R.drawable.thumbs_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { displayQuality = true }
                )
            }

            if (displayQuality) {
                QualitySetting(qualityList, onClick = {
                    scope.launch {
                        // TODO: 영상을 멈추는게 아닌 이어서 재생할 방법은 없을까? 진행 시점을 찍고 거기서 이어서 진행해야하나
                        exoPlayer.stop()
                        val mediaItem = MediaItem.Builder()
                            .setUri(it)
                            .build()

                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.playWhenReady = true
                        exoPlayer.prepare()
                        isPlaying = true
                    }
                })
            }
        }


    }
}