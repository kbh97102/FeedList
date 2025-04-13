package com.arakene.presentation.ui

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.presentation.viewmodel.VideoViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.math.abs


@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun FeedListView(
    viewModel: VideoViewModel = hiltViewModel()
) {

    val state = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        viewModel.testMethod()
        viewModel.getAllLikes()
//        viewModel.searchVideos("Ocean")
//        viewModel.getVideo(1093652)
    }

    val items = viewModel.testVideos.collectAsLazyPagingItems()

    val preloadImage = rememberGlidePreloadingData(
        items.itemSnapshotList.items,
        androidx.compose.ui.geometry.Size(100f, 200f),
        numberOfItemsToPreload = 15,
        fixedVisibleItemCount = 30
    ) { dataItem, requestBuilder ->
        requestBuilder.load(dataItem.image)
    }


//    items.itemSnapshotList.firstOrNull()?.let { video ->
//        FeedItem(video, onFavoriteClick = {})
//    }


    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        flingBehavior = remember { SmoothSlowFlingBehavior() }
    ) {

        items(preloadImage.size, key = {
            items.get(it)?.let { data ->
                "${data}_$it"
            } ?: "fallback_$it"
        }) {
            val (dataItem, preloadRequest) = preloadImage[it]
            FeedItem(
                videoDto = dataItem,
                onFavoriteClick = {
                    viewModel.clickLike(it)
                },
                onClick = {
                    viewModel.currentPlayingVideoId = it
                },
                currentPlayingID = viewModel.currentPlayingVideoId,
                preloadRequest = preloadRequest,
                currentFavorite = viewModel.likes.any { it.videoId == dataItem.id }
            )
        }
    }

}

class SmoothSlowFlingBehavior(
    private val decelerationFactor: Float = 0.92f, // 감속률 (1에 가까울수록 천천히 감속)
    private val minVelocity: Float = 10f           // 최소 속도 (이하일 경우 멈춤)
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        var velocity = initialVelocity
        coroutineScope {
            while (abs(velocity) > minVelocity) {
                val delta = velocity * 0.016f // 16ms당 이동 거리 (60fps 기준)
                scrollBy(delta)
                velocity *= decelerationFactor
                delay(16)
            }
        }
        return velocity
    }

}