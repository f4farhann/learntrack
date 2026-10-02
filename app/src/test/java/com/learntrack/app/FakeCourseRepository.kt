package com.learntrack.app

import com.learntrack.app.domain.model.Course
import com.learntrack.app.domain.repository.CourseRepository
import kotlin.collections.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCourseRepository : CourseRepository {

    val courses = MutableStateFlow<List<Course>>(emptyList())
    var refreshResult: Result<Unit> = Result.success(Unit)

    override fun observeCourses(): Flow<List<Course>> = courses

    override fun observeCourse(courseId: Int): Flow<Course?> =
        courses.map { list -> list.find { it.id == courseId } }

    override suspend fun refresh(): Result<Unit> = refreshResult

    override suspend fun markLessonCompleted(lessonId: Int) {
        courses.value = courses.value.map { course ->
            course.copy(lessons = course.lessons.map {
                if (it.id == lessonId) it.copy(completed = true) else it
            })
        }
    }
}