package com.arakene.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData


import com.arakene.domain.responses.VideoDto
import com.arakene.domain.usecases.GetPopularVideoUseCase
import com.arakene.domain.usecases.GetSearchVideoUseCase
import com.arakene.domain.usecases.GetVideoUseCase
import com.arakene.presentation.LogD
import com.arakene.presentation.LogE
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
    private val getVideoUseCase: GetVideoUseCase
) : ViewModel(){

    val videos = MutableStateFlow<PagingData<VideoDto>>(PagingData.empty())
    val testVideos: Flow<PagingData<VideoDto>> get() = videos

    fun testMethod() = viewModelScope.launch {
        getPopularVideoUseCase()
            .collectLatest {
                videos.value = it
            }
    }

    fun searchVideos(search: String) = viewModelScope.launch {
        getSearchVideoUseCase(search)?.let { result ->
//            LogD(result.toString())
        }
    }

    fun getVideo(id: Int) = viewModelScope.launch {
        getVideoUseCase(id)?.let { result ->
            LogE(result.toString())
        }
    }

}