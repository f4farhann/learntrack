package com.learntrack.app.data.mapper

import com.learntrack.app.data.local.CourseEntity
import com.learntrack.app.data.local.CourseWithLessons
import com.learntrack.app.data.local.LessonEntity
import com.learntrack.app.data.remote.CourseDto
import com.learntrack.app.domain.model.Course
import com.learntrack.app.domain.model.Lesson

// Dto -> Entity
fun CourseDto.toEntity() = CourseEntity(id = id, title = title, instructor = instructor)

// keeps lessons the user already completed locally
fun CourseDto.toLessonEntities(locallyCompleted: Set<Int>): List<LessonEntity> =
    lessons.map {
        LessonEntity(
            id = it.id,
            courseId = id,
            title = it.title,
            completed = it.completed || it.id in locallyCompleted
        )
    }

// Entity -> Domain
fun CourseWithLessons.toDomain() = Course(
    id = course.id,
    title = course.title,
    instructor = course.instructor,
    lessons = lessons.sortedBy { it.id }.map { Lesson(it.id, it.title, it.completed) }
)