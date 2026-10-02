package com.learntrack.app.data.remote

interface CourseDataSource {
    suspend fun getCourses(): List<CourseDto>
}