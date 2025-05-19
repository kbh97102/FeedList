package com.arakene.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogD
import com.arakene.presentation.R
import com.arakene.presentation.util.noEffectClickable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Player(
    videoDto: VideoDto,
    exoPlayer: ExoPlayer,
    releasePlayer: () -> Unit,
    currentIndex: Int,
    videoIndex: Int,
    preloadManager: DefaultPreloadManager,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

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

    var currentPosition by remember {
        mutableStateOf(0L)
    }

    var displayPlayButton by remember {
        mutableStateOf(false)
    }

    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    var currentUrl by remember {
        mutableStateOf(videoDto.videoFiles.firstOrNull()?.link)
    }

    DisposableEffect(Unit) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    isPlaying = true
                }
            }
        })

        onDispose {
            LogD("Dispose ${videoDto.id}")
            releasePlayer()
        }
    }

    LaunchedEffect(preloadManager, videoIndex, currentIndex) {

        if (currentIndex != videoIndex) {
            exoPlayer.playWhenReady = false
            exoPlayer.prepare()
            return@LaunchedEffect
        }

        exoPlayer.playWhenReady = true
        exoPlayer.clearMediaItems()
        val mediaSource = preloadManager.getMediaSource(
            MediaItem.Builder()
                .setMediaId("Video_${videoDto.id}")
                .setUri(videoDto.videoFiles.first().link)
                .build()
        )

        if (mediaSource == null) {
            exoPlayer.setMediaItem(
                MediaItem.fromUri(videoDto.videoFiles.first().link ?: "")
            )
        } else {
            exoPlayer.setMediaSource(mediaSource)
        }

        exoPlayer.prepare()
    }

    var displayQuality by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .noEffectClickable {
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                    displayPlayButton = true
                    scope.launch {
                        delay(1000L)
                        displayPlayButton = false
                    }
                },
            factory = { context ->
                PlayerView(context).apply {
                    useController = false
                    player = exoPlayer
                }
            },
            update = { view ->
                if (view.player != exoPlayer) {
                    view.player = exoPlayer
                }
            }
        )

        if (!isPlaying) {
            GlideImage(
                model = videoDto.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // TODO: 애니메이션은 차후에 변경
        AnimatedVisibility(
            visible = displayPlayButton,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter =
                    if (exoPlayer.isPlaying) {
                        painterResource(R.drawable.icn_play)
                    } else {
                        painterResource(R.drawable.icn_pause)
                    },
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Column(
                modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.thumbs),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp),
                    tint = Color.White
                )

                Icon(
                    painter = painterResource(R.drawable.thumbs_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { displayQuality = true },
                    tint = Color.White
                )
            }

            if (displayQuality) {
                QualitySetting(qualityList, onClick = {
                    if (!isPlaying) {
                        isPlaying = true
                    }

                    scope.launch {
                        currentPosition = exoPlayer.currentPosition
                        currentUrl = it
                    }
                })
            }
        }


    }
}