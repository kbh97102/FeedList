package com.arakene.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto

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
) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    /*
    TODO
     화면 회전 시 exoPlayer 돌아가는가?
     */

    LaunchedEffect(videoDto) {

        /**
         *
         * Media Item
         *  Media Item 은 어떤 영상을 재생할지에 대한 정보
         *
         * Media Source
         *  영상을 어떻게 재생할지
         *
         *
         */

        val videoFile = videoDto.videoFiles.firstOrNull() ?: return@LaunchedEffect

        val mediaItem = MediaItem.Builder()
            .setUri(videoFile.link)
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.run {
            // TODO: 뭔지 궁금한 친구들
//            setImageOutput()
//            setCameraMotionListener()
//            setHandleAudioBecomingNoisy()
//            setImageOutput()
//            setPreferredAudioDevice()
//            setVideoEffects()
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = exoPlayer
                    useController = false
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            ControlMenu(
                play = {
                    exoPlayer.play()
                },
                stop = {
                    // TODO Stop 에 다시 Play를 하면 재생이 안된다 그 이유가 뭘까 release 되는걸까?
                    exoPlayer.stop()
                }
            )
        }
    }


}


@Composable
fun ControlMenu(
    play: () -> Unit,
    stop: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Text("Play", modifier = Modifier.clickable { play() })
        Text("Stop", modifier = Modifier.clickable { stop() })
    }

}