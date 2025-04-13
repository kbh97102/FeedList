package com.arakene.domain.responses

sealed interface ApiResult<T> {

    data class Success<T>(
        val data: T
    ) : ApiResult<T>

    /**
     * 서버에서 주는 실패
     */
    data class Fail<T>(
        val errorMessage: String,
        val code: String
    ): ApiResult<T>

    /**
     * HTTP Exception 같은 에러들
     */
    data class Exception<T>(
        val throwable: Throwable
    ): ApiResult<T>

}