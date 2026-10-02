package com.learntrack.app.domain.model

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val lessons: List<Lesson>
) {
    val totalLessons: Int get() = lessons.size

    val progress: Int get() = calculateProgress(lessons)
}