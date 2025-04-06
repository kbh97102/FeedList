package com.arakene.domain.responses
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    @SerialName("id") val id: Int,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("url") val url: String,
    @SerialName("image") val image: String,
    @SerialName("full_res") val fullRes: String? = null,
    @SerialName("tags") val tags: List<String>,
    @SerialName("duration") val duration: Int,
    @SerialName("user") val user: UserDto,
    @SerialName("video_files") val videoFiles: List<VideoFileDto>,
    @SerialName("video_pictures") val videoPictures: List<VideoPictureDto>
){
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
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
) {
    constructor(): this(0, "", "")
}

@Serializable
data class VideoFileDto(
    @SerialName("id") val id: Int,
    @SerialName("quality") val quality: String,
    @SerialName("file_type") val fileType: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("fps") val fps: Double,
    @SerialName("link") val link: String
)

@Serializable
data class VideoPictureDto(
    @SerialName("id") val id: Int,
    @SerialName("picture") val picture: String,
    @SerialName("nr") val nr: Int
)
