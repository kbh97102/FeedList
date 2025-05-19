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
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogD
import com.arakene.presentation.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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

    var startTime = remember {
        0L
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val mediaItems = remember(videoDto) {
        videoDto.videoFiles.map { videoFile ->
            videoFile.let { video ->
                MediaItem.Builder()
                    .setUri(video.link)
                    .setMediaId("Video_${video.id}") // TODO: 임시 테스트를 위한 값
                    .build()
            }
        }
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

    var currentPosition by remember {
        mutableStateOf(0L)
    }


    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    var currentUrl by remember {
        mutableStateOf(videoDto.videoFiles.firstOrNull()?.link)
    }

    LaunchedEffect(isPlaying) {
        LogD("videoIndex $videoIndex videoId ${videoDto.id} isPlaying $isPlaying")
    }

    DisposableEffect(Unit) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
//                    LogD("Ready ${videoDto.id} time ${System.currentTimeMillis() - startTime}ms  currentIndex $currentIndex videoIndex $videoIndex")
                    isPlaying = true
                }
            }
        })

        onDispose {
            LogD("Dispose ${videoDto.id}")
            releasePlayer()
        }
    }

    LaunchedEffect(videoDto) {
        mediaItems.forEachIndexed { index, mediaItem ->
            /**
             * 영상 화질 변경했을 때
             * preloading을 통해 미리 일정 부분 버퍼링한 경우 77 ~ 232 ms가 소모
             * STAGE_SOURCE_PREPARED로만 한 경우 126 ~ 236 ms 까지 있었고 중간에 466ms 정도로 튄 값이 있었음
             * 아예 버퍼링을 안한 경우 77 ~ 432 ms로 대부분 높은 편에 속했었음
             */
//            preloadManager.add(mediaItem, 0)
        }

//        preloadManager.invalidate()
    }

    LaunchedEffect(preloadManager, videoIndex, currentIndex) {

        if (currentIndex != videoIndex) {
            exoPlayer.playWhenReady = false
            exoPlayer.prepare()
            return@LaunchedEffect
        }

        val uri = currentUrl ?: return@LaunchedEffect


//        val playTarget = mediaItems.find { it.localConfiguration?.uri.toString() == currentUrl }
//            ?: return@LaunchedEffect
//
//        val mediaSource = preloadManager.getMediaSource(playTarget) ?: return@LaunchedEffect
//        startTime = System.currentTimeMillis()
//        LogD("prepare Start ${videoDto.id} Time $startTime")
//        exoPlayer.setMediaSource(mediaSource)
//        exoPlayer.playWhenReady = true
//        exoPlayer.prepare()
//
//        exoPlayer.seekTo(currentPosition)

        exoPlayer.playWhenReady = true
        exoPlayer.clearMediaItems()
//        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        val mediaSource = preloadManager.getMediaSource(MediaItem.Builder()
            .setMediaId("Video_${videoDto.id}")
            .setUri(videoDto.videoFiles.first().link)
            .build())

        /*
        TODO preload가 완료되기 전에 찾으면서 null이 오는 듯 한데 이걸 어떻게 하는게 좋을까 - 1단 해결
        TODO 섬네일을 먼저 보여주면되니 이건 preload에서 가져오지말고 videoDto에서 가져와서 사용해보자 exoPlayer image 기능을 활용해볼 수 있을까?
         */

        if (mediaSource == null) {
            exoPlayer.setMediaItem(
                MediaItem.fromUri(videoDto.videoFiles.first().link ?: "")
            )

            LogD("videoID ${videoDto.id} preload null")

        } else {
            exoPlayer.setMediaSource(mediaSource)
            LogD("videoID ${videoDto.id} preload Success")
        }

        exoPlayer.prepare()
    }

    var displayQuality by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.BottomEnd
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
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


        Box {
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Icon(
                    painter = painterResource(R.drawable.thumbs),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
//                            preloadManager.setCurrentPlayingIndex(3)
                        }
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
                        currentPosition = exoPlayer.currentPosition
                        currentUrl = it
                    }
                })
            }
        }


    }
}