package com.learntrack.app.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithLessons(
    @Embedded val course: CourseEntity,
    @Relation(parentColumn = "id", entityColumn = "courseId")
    val lessons: List<LessonEntity>
)