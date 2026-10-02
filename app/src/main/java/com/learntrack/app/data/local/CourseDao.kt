package com.learntrack.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Transaction
    @Query("SELECT * FROM courses")
    fun observeCourses(): Flow<List<CourseWithLessons>>

    @Transaction
    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun observeCourse(courseId: Int): Flow<CourseWithLessons?>

    @Query("SELECT id FROM lessons WHERE completed = 1")
    suspend fun getCompletedLessonIds(): List<Int>

    @Query("UPDATE lessons SET completed = 1 WHERE id = :lessonId")
    suspend fun markLessonCompleted(lessonId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("DELETE FROM courses")
    suspend fun clearCourses() // lessons are deleted too (CASCADE)

    @Transaction
    suspend fun replaceAll(courses: List<CourseEntity>, lessons: List<LessonEntity>) {
        clearCourses()
        insertCourses(courses)
        insertLessons(lessons)
    }
}