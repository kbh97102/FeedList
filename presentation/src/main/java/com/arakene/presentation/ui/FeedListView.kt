package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.presentation.LogD
import com.arakene.presentation.viewmodel.VideoViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData


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
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
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

        if (items.loadState.append is LoadState.Loading) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LaunchedEffect(Unit) {
                        LogD("Indicator?")
                    }
                    CircularProgressIndicator()
                }
            }
        }

    }

}