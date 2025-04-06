package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.presentation.LogD
import com.arakene.presentation.viewmodel.VideoViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedListView(
    viewModel: VideoViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.testMethod()
    }

    val items = viewModel.testVideos.collectAsLazyPagingItems()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {

        items(items.itemCount) {
            items.get(it)?.let { videoDto ->
                FeedItem(videoDto)
            }
        }

    }

}