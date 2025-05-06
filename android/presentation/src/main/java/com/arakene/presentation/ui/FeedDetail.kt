package com.arakene.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.exoplayer.source.preload.PreloadException
import androidx.media3.exoplayer.source.preload.PreloadManagerListener
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogD
import com.arakene.presentation.viewmodel.VideoViewModel
import org.chromium.base.Log

/**
 *   * 영상 전체화면으로 전환
 *   * orientation 에 따른 화면 설정
 *   * 재생, 일시 정지, 멈춤
 *   * 뒤로 가기, 앞으로 가기
 *   * 다음 영상으로, 이전 영상으로
 */
@UnstableApi
@Composable
fun FeedDetail(
    videoDto: VideoDto,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel()
) {

    val videos = viewModel.videos.collectAsLazyPagingItems()

    val state = rememberPagerState {
        videos.itemCount
    }

    val test by remember(state.layoutInfo.visiblePagesInfo) {
        mutableStateOf(state.layoutInfo.visiblePagesInfo)
    }

    LaunchedEffect(viewModel) {
        LogD("request videos")
        viewModel.testMethod()
    }

    val context = LocalContext.current

    //    /**
//     * Preload하는 건 좋은데 이건 해상도 별로 진행됨
//     * TODO: 위아래 스크롤 시 보다 더 빠르고 자연스러운 이벤트를 위해서는 외부에서 preload된걸 줘야하지 않나
//     */
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
                        LogD("Preload Success ${mediaItem.mediaId}")
                    }

                    override fun onError(exception: PreloadException) {
                        super.onError(exception)
                        LogD("Preload exception $exception")
                    }
                })
            }
    }

    LaunchedEffect(videos) {
        LogD("뭐 안오니? ${videos.itemCount}")
    }

//    val testList by remember {
//        derivedStateOf { videos.itemSnapshotList }
//    }

    LaunchedEffect(videos.hashCode()) {

        val currentPage = state.currentPage

        if (currentPage % 10 != 0) {
            return@LaunchedEffect
        }

        val currentVideos = videos.itemSnapshotList

        LogD("preload start // size ${currentVideos.size}")

        if (currentVideos.isEmpty()) {
            LogD("videos empty ${currentVideos.size} ${currentPage}")
            return@LaunchedEffect
        }

        currentVideos.forEach { video ->
            video ?: return@forEach
            preloadManager.add(
                MediaItem.Builder()
                    .setMediaId("Video_${video.id}")
                    .setUri(video.videoFiles.first().link)
                    .build(),
                0
            )
        }

        preloadManager.invalidate()

    }


    VerticalPager(
        state,
    ) {
        val target = videos[it]

        Player(
            videoDto = target ?: return@VerticalPager,
            modifier = Modifier.fillMaxSize(),
            exoPlayer = viewModel.getPlayer(target.id.toString()),
            releasePlayer = {
                viewModel.releasePlayer(target.id.toString())
            },
            currentIndex = state.currentPage,
            videoIndex = it,
            preloadManager = preloadManager
        )

    }

}


@Composable
fun ControlMenu(
    play: () -> Unit,
    stop: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Text("Play", modifier = Modifier.clickable { play() }, color = Color.White)
        Text("Stop", modifier = Modifier.clickable { stop() }, color = Color.White)
    }

}