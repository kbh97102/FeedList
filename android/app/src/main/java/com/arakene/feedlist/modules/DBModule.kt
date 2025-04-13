package com.arakene.feedlist.modules

import android.content.Context
import androidx.room.Room
import com.arakene.data.db.LikeDB
import com.arakene.data.db.LikeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationContext::class)
@Module
class DBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LikeDB::class.java,
            "user_db"
        ).build()


    @Provides
    fun provideUserDao(appDatabase: LikeDB) = appDatabase.getLikeDao()

}