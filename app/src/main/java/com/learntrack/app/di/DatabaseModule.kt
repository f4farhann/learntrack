package com.learntrack.app.di

import com.learntrack.app.data.local.AppDatabase
import com.learntrack.app.data.local.CourseDao
import kotlin.jvm.java
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "learntrack.db").build()

    @Provides
    fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
}