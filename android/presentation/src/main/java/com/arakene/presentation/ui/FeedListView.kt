package com.arakene.presentation.ui

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.presentation.LogD
import com.arakene.presentation.viewmodel.VideoViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max


@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun FeedListView(
    viewModel: VideoViewModel = hiltViewModel()
) {

    val state = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        viewModel.testMethod()
    }

    val items = viewModel.testVideos.collectAsLazyPagingItems()

    LaunchedEffect(items.loadState) {

        LogD("State ${items.loadState}")

    }

    val preloadImage = rememberGlidePreloadingData(
        items.itemSnapshotList.items,
        androidx.compose.ui.geometry.Size(100f, 200f),
        numberOfItemsToPreload = 15,
        fixedVisibleItemCount = 30
    ) { dataItem, requestBUilder ->
        requestBUilder.load(dataItem.image)
    }


    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
        flingBehavior = remember { SmoothSlowFlingBehavior() }
    ) {

        items(preloadImage.size, key = {
            items.get(it)?.let { data ->
                "${data}_$it"
            } ?: "fallback_$it"
        }) {
            val (dataItem, preloadRequest) = preloadImage[it]
            GlideImage(model = dataItem.image, contentDescription = null) {
                it.thumbnail(preloadRequest)
            }
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