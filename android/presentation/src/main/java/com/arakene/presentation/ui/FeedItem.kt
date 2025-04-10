package com.arakene.presentation.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.R
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedItem(
    videoDto: VideoDto,
    preloadRequest: RequestBuilder<Drawable>,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false
) {

    /*
     * TODO: 성능 개선이 필요함, 한번에 불러오는 아이템을 증가하고, 추가로 불러올때는 UI 로딩이 완료된 이후?, 스크롤시 만드는거라 큰 차이는 없을텐데
     */

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {

        GlideImage(
            model = videoDto.image,
            contentDescription = null,
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .thumbnail(preloadRequest)
        }

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
                }
            ,
            contentDescription = null
        )


    }

}