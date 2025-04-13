package com.arakene.data.util

import com.arakene.domain.responses.ApiResult
import retrofit2.Response


suspend fun <T> safeApi(execute: suspend () -> Response<T>): ApiResult<T> {

    return try {

        val response = execute()

        return if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Fail("Data is null", code = "ErrorCode")
        } else {
            // 서버에서 내려주는 에러
            // 지금은 없지만 나중에 백엔드랑 협업할때는 에러 dto 파싱 해야함
            ApiResult.Fail("Test Error Message", code = "ErrorCode")
        }
    } catch (e: Exception) {
        /**
         * 404, internet connection 관련 에러 처리
         */
        ApiResult.Exception(e)
    }
}

