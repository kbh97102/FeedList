package com.arakene.domain.responses

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class VideoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("full_res") val fullRes: String? = null,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("url") val url: String,
    @SerializedName("image") val image: String,
    @SerializedName("avg_color") val avgColor: String? = null,
    @SerializedName("user") val user: UserDto,
    @SerializedName("video_files") val videoFiles: List<VideoFileDto>,
    @SerializedName("video_pictures") val videoPictures: List<VideoPictureDto>
) {
    companion object {
        fun empty() = VideoDto(
            id = 0,
            width = 0,
            height = 0,
            url = "",
            image = "",
            fullRes = null,
            tags = emptyList(),
            duration = 0,
            user = UserDto(),
            videoFiles = emptyList(),
            videoPictures = emptyList()
        )
    }
}

@Serializable
data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    constructor() : this(0, "", "")
}

@Serializable
data class VideoFileDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("quality") val quality: String?,
    @SerializedName("file_type") val fileType: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?,
    @SerializedName("fps") val fps: Double?,
    @SerializedName("link") val link: String?,
)

@Serializable
data class VideoPictureDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nr") val nr: Int,
    @SerializedName("picture") val picture: String
)
