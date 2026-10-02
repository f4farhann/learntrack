package com.learntrack.app.domain.model

fun calculateProgress(lessons: List<Lesson>): Int {
    if (lessons.isEmpty()) return 0
    return (lessons.count { it.completed } * 100) / lessons.size
}