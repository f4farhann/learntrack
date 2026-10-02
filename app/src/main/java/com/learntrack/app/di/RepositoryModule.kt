package com.learntrack.app.di

import com.learntrack.app.data.remote.CourseDataSource
import com.learntrack.app.data.remote.LocalCourseDataSource
import com.learntrack.app.data.repository.AuthRepositoryImpl
import com.learntrack.app.data.repository.CourseRepositoryImpl
import com.learntrack.app.domain.repository.AuthRepository
import com.learntrack.app.domain.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCourseDataSource(impl: LocalCourseDataSource): CourseDataSource

    @Binds
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}