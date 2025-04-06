package com.arakene.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arakene.domain.responses.VideoDto
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeedItem(
    videoDto: VideoDto,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier){

        GlideImage(
            model = videoDto.image,
            contentDescription = null
        )

    }

}


@Preview(showBackground = true)
@Composable
private fun FeedItemPreview() {
    FeedItem(
        videoDto = VideoDto.empty()
    )
}