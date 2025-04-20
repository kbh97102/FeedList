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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.viewmodel.VideoViewModel

/**
 *   * 영상 전체화면으로 전환
 *   * orientation 에 따른 화면 설정
 *   * 재생, 일시 정지, 멈춤
 *   * 뒤로 가기, 앞으로 가기
 *   * 다음 영상으로, 이전 영상으로
 */
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

    LaunchedEffect(viewModel) {
        viewModel.testMethod()
    }

    VerticalPager(state) {

        val target = videos[it]

        Player(
            url = target?.videoFiles?.firstOrNull()?.link ?: "",
            thumbnailUrl = target?.image ?: "",
            modifier = Modifier.fillMaxSize()
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