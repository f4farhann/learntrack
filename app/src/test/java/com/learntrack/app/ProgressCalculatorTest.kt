package com.learntrack.app

import com.learntrack.app.domain.model.Lesson
import com.learntrack.app.domain.model.calculateProgress
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressCalculatorTest {

    @Test
    fun `progress updates after completing a lesson`() {
        val lessons = listOf(
            Lesson(1, "A", true),
            Lesson(2, "B", false),
            Lesson(3, "C", false),
            Lesson(4, "D", false)
        )
        assertEquals(25, calculateProgress(lessons))

        val updated = lessons.map { if (it.id == 2) it.copy(completed = true) else it }
        assertEquals(50, calculateProgress(updated))
    }

    @Test
    fun `progress is zero when course has no lessons`() {
        assertEquals(0, calculateProgress(emptyList()))
    }
}