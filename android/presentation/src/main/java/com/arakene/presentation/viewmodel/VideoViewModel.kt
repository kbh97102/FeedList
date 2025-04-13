package com.arakene.presentation.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.arakene.domain.entities.Like
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.usecases.DeleteLikeUseCase
import com.arakene.domain.usecases.GetAllLikesUseCase
import com.arakene.domain.usecases.GetPopularVideoUseCase
import com.arakene.domain.usecases.GetSearchVideoUseCase
import com.arakene.domain.usecases.GetVideoUseCase
import com.arakene.domain.usecases.InsertLikeUseCase
import com.arakene.presentation.LogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getSearchVideoUseCase: GetSearchVideoUseCase,
    private val getPopularVideoUseCase: GetPopularVideoUseCase,
    private val getVideoUseCase: GetVideoUseCase,
    private val getAllLikesUseCase: GetAllLikesUseCase,
    private val insertLikeUseCase: InsertLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : BaseViewModel() {

    val videos = MutableStateFlow<PagingData<VideoDto>>(PagingData.empty())
    val testVideos: Flow<PagingData<VideoDto>> get() = videos

    var likes by mutableStateOf(listOf<Like>())

    var currentPlayingVideoId by mutableStateOf(-1)

    fun testMethod() = viewModelScope.launch {
        getPopularVideoUseCase()
            .collectLatest {
                videos.value = it
            }
    }

    fun searchVideos(search: String) = viewModelScope.launch {
        getResponse {
            getSearchVideoUseCase(search)
        }?.let {
            LogD(it.toString())
            // Do SomeThing
        }
    }

    fun getVideo(id: Int) = viewModelScope.launch {
        getResponse {
            getVideoUseCase(id)
        }?.let {
            // Do SomeThing
        }
    }

    fun getAllLikes() = viewModelScope.launch {
        likes = getAllLikesUseCase()
    }

    fun insertLike(videoDto: VideoDto) {
        viewModelScope.launch {
            insertLikeUseCase(Like(videoId = videoDto.id))
        }
    }

    /**
     * TODO: 나중에 MVI 구조로 변경
     */
    fun clickLike(videoDto: VideoDto) {
        viewModelScope.launch {
            if (likes.any { it.videoId == videoDto.id }) {
                deleteLikeUseCase(videoId = videoDto.id)
            } else {
                insertLike(videoDto)
            }
            getAllLikes()
        }
    }

}