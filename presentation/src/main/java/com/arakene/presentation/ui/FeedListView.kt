package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.presentation.LogD
import com.arakene.presentation.viewmodel.VideoViewModel
import java.util.UUID
import kotlin.uuid.Uuid


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedListView(
    viewModel: VideoViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.testMethod()
    }

    val items = viewModel.testVideos.collectAsLazyPagingItems()

    LaunchedEffect(items.loadState) {

        LogD("State ${items.loadState}")

    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {

        items(items.itemCount, key = {
            items.get(it)?.let { data ->
                "${data}_$it"
            } ?: "fallback_$it"
        }) {
            items.get(it)?.let { videoDto ->
                FeedItem(videoDto)
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