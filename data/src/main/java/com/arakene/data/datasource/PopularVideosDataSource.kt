package com.arakene.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arakene.data.Api
import com.arakene.domain.responses.VideoDto
import com.arakene.domain.responses.VideoListResponse

class PopularVideosDataSource(
    private val api: Api
) : PagingSource<Int, VideoDto>() {

    override fun getRefreshKey(state: PagingState<Int, VideoDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoDto> {
        return try {
            val currentPage = params.key ?: 1
            val response = api.getPopularVideos(
                page = currentPage
            )

            val videos = response.body()?.videos ?: emptyList()

            LoadResult.Page(
                data = videos,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.body() == null) null else response.body()?.nextPage?.toInt()
                    ?: (currentPage + 1)
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}