package com.arakene.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arakene.domain.usecases.GetPopularVideoUseCase
import com.arakene.domain.usecases.GetVideoUseCase
import com.arakene.presentation.LogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVideoUseCase: GetVideoUseCase,
    private val getPopularVideoUseCase: GetPopularVideoUseCase
) : ViewModel(){


    fun testMethod() = viewModelScope.launch {
        getPopularVideoUseCase()
            .let {
                LogD("Video Response\n$it")
            }
    }


}