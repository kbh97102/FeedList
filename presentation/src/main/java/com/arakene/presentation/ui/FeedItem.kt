package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.arakene.domain.responses.VideoDto
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeedItem(
    videoDto: VideoDto,
    modifier: Modifier = Modifier
) {

    /*
     * TODO: 성능 개선이 필요함, 한번에 불러오는 아이템을 증가하고, 추가로 불러올때는 UI 로딩이 완료된 이후?, 스크롤시 만드는거라 큰 차이는 없을텐데
     */

    Box(modifier = modifier){

        GlideImage(
            model = videoDto.image,
            contentDescription = null,
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
        }

    }

}


@Preview(showBackground = true)
@Composable
private fun FeedItemPreview() {
    FeedItem(
        videoDto = VideoDto.empty()
    )
}