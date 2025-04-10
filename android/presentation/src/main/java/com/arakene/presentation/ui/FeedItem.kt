package com.arakene.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.LogE
import com.arakene.presentation.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.chromium.net.CronetEngine
import java.util.concurrent.Executors

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeedItem(
    videoDto: VideoDto,
    onClick: (Int) -> Unit,
    currentPlayingID: Int,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false
) {

    val context = LocalContext.current

    var playVideo by remember(currentPlayingID) {
        mutableStateOf(videoDto.id == currentPlayingID)
    }

    val ratio by remember(videoDto) {
        mutableFloatStateOf(videoDto.width.toFloat() / videoDto.height.toFloat())
    }

    val dataSourceFactory = remember {
        val cronetEngine = CronetEngine.Builder(context).build()
        val executor = Executors.newSingleThreadExecutor()

        val cronetDataSourceFactory = CronetDataSource.Factory(cronetEngine, executor)
        DefaultDataSource.Factory(
            context, /* baseDataSourceFactory= */
            cronetDataSourceFactory
        )
    }

    val player = remember(videoDto) {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)
            )
            .build()
    }

    var controlPlayerVisible by remember {
        mutableStateOf(false)
    }

    DisposableEffect(player) {
        player.apply {
            setMediaItem(MediaItem.fromUri(videoDto.videoFiles.first().link ?: ""))
//            playWhenReady = true
            this.repeatMode = Player.REPEAT_MODE_ALL
        }

        onDispose {
            LogE("여기오니?")
            player.release()
        }
    }

    LaunchedEffect(playVideo) {

        if (playVideo) {
            player.prepare()
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_READY) {
                        player.play()
                        controlPlayerVisible = true
                    }
                }
            })
        } else {
            player.stop()
        }
    }

    LaunchedEffect(ratio) {
        LogE("id ${videoDto.id} Ratio? ${ratio}")
    }

    /*
     * TODO: 성능 개선이 필요함, 한번에 불러오는 아이템을 증가하고, 추가로 불러올때는 UI 로딩이 완료된 이후?, 스크롤시 만드는거라 큰 차이는 없을텐데
     */
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratio)
            .clickable {
                onClick(videoDto.id)
                if (currentPlayingID == videoDto.id) {
                    player.stop()
                }
            },
        contentAlignment = Alignment.TopEnd
    ) {

        GlideImage(
            model = videoDto.image,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
        }


        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .alpha(
                    if (controlPlayerVisible) {
                        1f
                    } else {
                        0f
                    }
                ),
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    useController = false
                }
            }
        )


        Icon(
            painter = painterResource(
                if (isFavorite) {
                    R.drawable.icn_heart
                } else {
                    R.drawable.icn_heart_outlined
                }
            ),
            modifier = Modifier
                .padding(end = 10.dp, top = 10.dp)
                .size(16.dp)
                .clickable {
                    onFavoriteClick()
                },
            contentDescription = null
        )


    }

}