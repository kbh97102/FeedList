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

    /**
     * Preload하는 건 좋은데 이건 해상도 별로 진행됨
     * TODO: 위아래 스크롤 시 보다 더 빠르고 자연스러운 이벤트를 위해서는 외부에서 preload된걸 줘야하지 않나
     */
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

    var startTime by remember {
        mutableStateOf(0L)
    }

    DisposableEffect(Unit) {

        exoPlayer.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                if (playbackState == Player.STATE_READY) {
                    LogD("Ready ${System.currentTimeMillis() - startTime}")
                }

            }
        })

        onDispose {
            LogD("Dispose ${videoDto.id}")
            exoPlayer.stop()
            exoPlayer.release()
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
            preloadManager.add(mediaItem, 0)
        }

        preloadManager.invalidate()
    }

    LaunchedEffect(currentUrl) {

        currentUrl ?: return@LaunchedEffect


        val playTarget = mediaItems.find { it.localConfiguration?.uri.toString() == currentUrl }
            ?: return@LaunchedEffect

        val mediaSource = preloadManager.getMediaSource(playTarget) ?: return@LaunchedEffect
        startTime = System.currentTimeMillis()
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.playWhenReady = true
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
                        // TODO: 영상을 멈추는게 아닌 이어서 재생할 방법은 없을까? 진행 시점을 찍고 거기서 이어서 진행해야하나
                        currentUrl = it
                    }
                })
            }
        }


    }
}