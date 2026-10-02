package com.learntrack.app.domain.repository

import com.learntrack.app.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    // UI watches this. It updates automatically when data changes.
    fun observeCourses(): Flow<List<Course>>

    // Watch a single course (for the details screen)
    fun observeCourse(courseId: Int): Flow<Course?>

    // Fetch from JSON/API and save into Room
    suspend fun refresh(): Result<Unit>

    // Mark a lesson as done
    suspend fun markLessonCompleted(lessonId: Int)
}