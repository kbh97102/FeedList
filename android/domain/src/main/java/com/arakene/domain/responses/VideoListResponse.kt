package com.arakene.domain.responses

import kotlinx.serialization.SerialName


data class VideoListResponse(
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("videos") val videos: List<VideoDto>,
    @SerialName("total_results") val totalResults: Int,
    @SerialName("next_page") val nextPage: String,
    @SerialName("url") val url: String
)