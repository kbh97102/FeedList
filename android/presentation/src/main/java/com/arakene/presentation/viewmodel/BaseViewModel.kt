package com.arakene.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arakene.domain.responses.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
open class BaseViewModel : ViewModel() {

    private var _error = MutableLiveData<Pair<String, String>?>(null)
    val error: LiveData<Pair<String, String>?> get() = _error

    private var _exception = MutableLiveData<Throwable?>(null)
    val exception: LiveData<Throwable?> get() = _exception

    suspend fun <T> getResponse(execute: suspend () -> ApiResult<T>): T? {
        return when (val response = execute()) {
            is ApiResult.Success -> {
                response.data
            }

            is ApiResult.Fail -> {
                _error.value = Pair(response.errorMessage, response.code)
                null
            }

            is ApiResult.Exception -> {
                _exception.value = response.throwable
                null
            }
        }
    }

}