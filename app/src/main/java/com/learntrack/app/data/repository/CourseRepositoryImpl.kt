package com.learntrack.app.data.repository

import com.learntrack.app.data.local.CourseDao
import com.learntrack.app.data.mapper.toDomain
import com.learntrack.app.data.mapper.toEntity
import com.learntrack.app.data.mapper.toLessonEntities
import com.learntrack.app.data.remote.CourseDataSource
import com.learntrack.app.domain.model.Course
import com.learntrack.app.domain.repository.CourseRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val dataSource: CourseDataSource,
    private val dao: CourseDao
) : CourseRepository {

    override fun observeCourses(): Flow<List<Course>> =
        dao.observeCourses().map { list -> list.map { it.toDomain() } }

    override fun observeCourse(courseId: Int): Flow<Course?> =
        dao.observeCourse(courseId).map { it?.toDomain() }

    override suspend fun refresh(): Result<Unit> = try {
        val remote = dataSource.getCourses()
        val completedIds = dao.getCompletedLessonIds().toSet()

        dao.replaceAll(
            courses = remote.map { it.toEntity() },
            lessons = remote.flatMap { it.toLessonEntities(completedIds) }
        )
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e) // old cached data stays in Room
    }

    override suspend fun markLessonCompleted(lessonId: Int) {
        dao.markLessonCompleted(lessonId)
    }
}