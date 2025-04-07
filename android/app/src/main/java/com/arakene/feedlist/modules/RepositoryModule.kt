package com.arakene.feedlist.modules

import com.arakene.data.Api
import com.arakene.data.repositoryimpl.VideoRepositoryImpl
import com.arakene.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun bindVideoRepository(
        api: Api
    ): VideoRepository {
        return VideoRepositoryImpl(api)
    }

}