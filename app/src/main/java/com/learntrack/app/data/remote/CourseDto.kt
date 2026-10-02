package com.learntrack.app.data.remote

data class CourseDto(
    val id: Int,
    val title: String,
    val instructor: String,
    val lessons: List<LessonDto>
)

data class LessonDto(
    val id: Int,
    val title: String,
    val completed: Boolean
)