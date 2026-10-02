package com.learntrack.app.ui.details

import com.learntrack.app.domain.model.Course
import com.learntrack.app.domain.repository.CourseRepository

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: CourseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // "courseId" is the argument name from the NavGraph
    private val courseId: Int = checkNotNull(savedStateHandle["courseId"])

    // Room emits again after a lesson is updated, so progress refreshes by itself
    val course: StateFlow<Course?> = repository.observeCourse(courseId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onLessonCompleted(lessonId: Int) {
        viewModelScope.launch { repository.markLessonCompleted(lessonId) }
    }
}