package com.arakene.presentation.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.exoplayer.source.preload.PreloadException
import androidx.media3.exoplayer.source.preload.PreloadManagerListener
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogD
import com.arakene.presentation.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Player(
    videoDto: VideoDto,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    val context = LocalContext.current


    val preloadManager = remember {

        DefaultPreloadManager.Builder(
            context
        ) { rankingData ->
            when (rankingData) {
                0 -> DefaultPreloadManager.Status(
                    DefaultPreloadManager.Status.STAGE_LOADED_FOR_DURATION_MS, 5000
                )

                1 -> DefaultPreloadManager.Status(
                    DefaultPreloadManager.Status.STAGE_SOURCE_PREPARED
                )

                else -> null
            }
        }
            .setMediaSourceFactory(DefaultMediaSourceFactory(context))
            .build()
            .apply {
                addListener(object : PreloadManagerListener {
                    override fun onCompleted(mediaItem: MediaItem) {
                        super.onCompleted(mediaItem)
                        LogD("Preload Complete ${mediaItem.mediaMetadata}")
                    }

                    override fun onError(exception: PreloadException) {
                        super.onError(exception)
                        LogD("Preload exception $exception")
                    }
                })
            }
    }

    val exoPlayer = remember(context) {
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


    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    var currentUrl by remember {
        mutableStateOf(videoDto.videoFiles.firstOrNull()?.link)
    }

    DisposableEffect(Unit) {
        currentUrl ?: return@DisposableEffect onDispose {
            exoPlayer.release()
        }

        onDispose {
            LogD("Dispose")
            exoPlayer.stop()
            exoPlayer.release()
        }

    }

    LaunchedEffect(videoDto) {

        var count = 0

        val mediaItems = videoDto.videoFiles.map { videoFile ->
            count++
            videoFile.link?.let { videoLink ->
                MediaItem.Builder()
                    .setUri(videoLink)
                    .setMediaId("Video_$count") // TODO: 임시 테스트를 위한 값
                    .build()
            }
        }

        mediaItems.filterNotNull().forEachIndexed { index, mediaItem ->
            preloadManager.add(mediaItem, index)
        }

        preloadManager.invalidate()

        // TEST CODE
        val mediaSource = preloadManager.getMediaSource(mediaItems.first()!!)
        exoPlayer.setMediaSource(mediaSource!!)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()

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
                    useController = false
                    player = exoPlayer
                }
            }
        )

//        if (!isPlaying) {
//            GlideImage(
//                model = videoDto.image,
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .fillMaxSize()
//            )
//        }


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
                    if (!isPlaying) {
                        isPlaying = true
                    }

                    scope.launch {
                        // TODO: 영상을 멈추는게 아닌 이어서 재생할 방법은 없을까? 진행 시점을 찍고 거기서 이어서 진행해야하나
                        currentUrl = it
                    }
                })
            }
        }


    }
}